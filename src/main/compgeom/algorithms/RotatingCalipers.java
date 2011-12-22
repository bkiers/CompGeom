/*
 * Copyright (c) 2010, Bart Kiers
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * Project      : CompGeom; a computational geometry library using
 *                arbitrary-precision arithmetic where possible,
 *                written in Java.
 * Developed by : Bart Kiers, bart@big-o.nl
 */
package compgeom.algorithms;

import compgeom.*;

import java.util.*;

/**
 * <p>A class that finds the minimum bounding rectangle, or all bounding
 * rectangles, of a convex polygon in <code>O(n)</code> time, where <code>n</code>
 * is the number of points in the polygon, using the
 * <a href="http://cgm.cs.mcgill.ca/~godfried/research/calipers.html">rotating calipers algorithm</a>.</p>
 * <h2>Outline of the algorithm</h2>
 * <p>
 * <ol>
 * <li>
 * find the vertexes with the minimum and maximum x an y coordinates. These
 * vertexes (points) will be denoted by <code>p_I</code>, <code>p_J</code>,
 * <code>p_K</code> and <code>p_L</code>;
 * </li>
 * <li>
 * construct <code>cal_L</code> and <code>cal_J</code>
 * as the first set of calipers parallel to the x-axis, and
 * <code>cal_I</code> and <code>cal_K</code> as the second set of
 * calipers parallel to the y-axis;
 * </li>
 * <li>
 * create for every <code>cal_X</code> a next caliper, <code>next_cal_X</code>,
 * based on the next point in the convex hull and calculate the tangent of
 * <code>cal_X</code> and it&apos;s <code>next_cal_X</code>;
 * </li>
 * <li>
 * get the smallest positive tangent between all
 * <code>cal_X</code> and it&apos;s <code>next_cal_X</code>
 * and rotate every caliper by that smallest gradient;
 * </li>
 * <li>
 * repeat step 3 and 4 until the calipers have turned more then 90 degrees.
 * </li>
 * </ol>
 * </p>
 * <p>
 * <pre>
 *                    p_J
 *            +--o-----o-----+
 *            |              |
 *            |              o p_K
 *            |              |
 *        p_I o              |
 *            +----o------o--+
 *                p_L
 *
 *            /\ cal_I
 *            |              |
 *            |       p_J    |     cal_J
 *      ------+--o-----o-----+------>
 *            |              |
 *            |              o p_K
 *            |              |
 *        p_I o              |
 *     <------+----o------o--+------
 *  cal_L     |   p_L        |
 *            |              |
 *                          \/ cal_K
 * </pre>
 * <p>For a detailed explanation of the algorithm, see: Toussaint, Godfried T.
 * (1983). Solving geometric problems with the rotating calipers. Proc.
 * MELECON '83, Athens.
 * <a href="http://citeseer.ist.psu.edu/toussaint83solving.html">http://citeseer.ist.psu.edu/toussaint83solving.html</a>.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class RotatingCalipers {

    /**
     * the convex hull of the polygon to get the bounding rectangles from
     */
    private static List<RPoint2D> convexHull;

    /**
     * the array that holds the four calipers to be rotated around the convex hull
     */
    private static Caliper[] cals;

    /**
     * some final numbers used in this class
     */
    private final static Rational zero = Rational.ZERO;
    private final static Rational minOne = Rational.MINUS_ONE;
    private final static Rational inf = Rational.POSITIVE_INFINITY;

    /**
     * No need to instantiate this class
     */
    private RotatingCalipers() {
    }

    /**
     * Finds all bounding rectangles of a polygon denoted by a list of points.
     * The bounding rectangles are found in <code>O(n)</code> time if the
     * polygon is convex. If the polygon is not convex, the convex hull is
     * first found in <code>O(n*log(n))</code> time.
     *
     * @param points a list of points that represents the polygon.
     * @return all bounding rectangles of a polygon denoted by a
     *         list of points.
     * @throws IllegalArgumentException when the polygon has less than 3 points, or
     *                                  if all points are collinear.
     * @see compgeom.algorithms.GrahamScan
     */
    public static List<RRectangle> getBoundingRectangles(List<RPoint2D> points)
            throws IllegalArgumentException {
        return getBoundingRectangles(new RPolygon2D(points));
    }

    /**
     * Find all bounding rectangles of a polygon. The bounding rectangles
     * are found in <code>O(n)</code> time if the <code>polygon</code> is
     * convex. If the <code>polygon</code> is not convex, the convex hull
     * is first found in <code>O(n*log(n))</code> time.
     *
     * @param polygon the polygon of which to get all bounding rectangles.
     * @return all bounding rectangles of the <code>polygon</code>.
     * @see compgeom.algorithms.GrahamScan#getConvexHull(RPolygon2D)
     */
    public static List<RRectangle> getBoundingRectangles(RPolygon2D polygon) {

        convexHull = GrahamScan.getConvexHull(polygon);

        // We want a clockwise 'walk'
        Collections.reverse(convexHull);

        LinkedHashSet<RRectangle> rectangles = new LinkedHashSet<RRectangle>();
        initCalipers();

        // Keep looping while unique rectangles are added to the set.
        while (rectangles.add(new RRectangle(cals[0].line, cals[1].line,
                cals[2].line, cals[3].line))) {

            // Get the next smallest positive tangent from the calipers.
            Rational tangent = getSmallestPositiveTangent();

            // Are there still more rectangles? Note that a polygon with n points has at most
            // n bounding boxes: 1 aligned with the x- and y-axis at the start, and n-1 boxes
            // that have a side along one of the polygon's edges.
            if (tangent == null || rectangles.size() == convexHull.size()) {
                break;
            }

            // Rotate all calipers by the smallest positive tangent.
            rotateCalipersBy(tangent);
        }
        return new ArrayList<RRectangle>(rectangles);
    }

    /**
     * Find the minimum bounding rectangle of a polygon denoted by a list of points.
     * The minimum bounding rectangle is found in <code>O(n)</code> time if the
     * polygon is convex. If the polygon is not convex, the convex hull is first
     * found in <code>O(n*log(n))</code> time.
     *
     * @param points a list of points that represents the polygon
     * @return the minimum bounding rectangle of a polygon
     *         denoted by a list of points.
     * @throws IllegalArgumentException when the polygon has less than 3 points, or
     *                                  if all points are collinear.
     * @see compgeom.algorithms.GrahamScan
     */
    public static RRectangle getMinimumBoundingRectangle(List<RPoint2D> points)
            throws IllegalArgumentException {
        return getMinimumBoundingRectangle(new RPolygon2D(points));
    }

    /**
     * Find the minimum bounding rectangle of a polygon. The minimum bounding
     * rectangle is found in <code>O(n)</code> time if the polygon is convex.
     * If the polygon is not convex, the convex hull is first found in
     * <code>O(n*log(n))</code> time.
     *
     * @param polygon the polygon of which to get the minimum bounding
     *                rectangle.
     * @return the minimum bounding rectangles of the
     *         <code>polygon</code>
     * @see compgeom.algorithms.GrahamScan
     */
    public static RRectangle getMinimumBoundingRectangle(RPolygon2D polygon) {
        List<RRectangle> boxes = getBoundingRectangles(polygon);
        RRectangle minimum = null;
        for (RRectangle rectangle : boxes) {
            if (minimum == null || rectangle.areaSquared().isLessThan(minimum.areaSquared())) {
                minimum = rectangle;
            }
        }
        return minimum;
    }

    /**
     * Find the smallest positive tangent of the caliper with the smallest positive tangent
     *
     * @return the smallest positive tangent
     */
    private static Rational getSmallestPositiveTangent() {
        Rational smallestTangent = null;
        for (Caliper c : cals) {
            Rational temp = c.getTangentNextCaliper();
            if (temp != null && temp.isMoreThan(Rational.ZERO)) {
                if (smallestTangent == null || temp.isLessThan(smallestTangent)) {
                    smallestTangent = temp;
                }
            }
        }
        return smallestTangent;
    }

    /**
     * i - The 'turnPoint' of caliper I is the one with the smallest x value,
     * and in case of a tie, the biggest y value
     * <p/>
     * ii - The 'turnPoint' of caliper J is the one with the biggest y value,
     * and in case of a tie, the biggest x value
     * <p/>
     * iii - The 'turnPoint' of caliper K is the one with the biggest x value,
     * and in case of a tie, the smallest y value
     * <p/>
     * iv - The 'turnPoint' of caliper L is the one with the smallest y value,
     * and in case of a tie, the smallest x value
     */
    private static void initCalipers() {
        RPoint2D pI, pJ, pK, pL;
        int iI, iJ, iK, iL;
        pI = pJ = pK = pL = convexHull.get(0);
        iI = iJ = iK = iL = 0;
        for (int i = 1; i < convexHull.size(); i++) {
            RPoint2D p = convexHull.get(i);

            if (p.x.isLessThan(pI.x) || (p.x.equals(pI.x) && p.y.isMoreThan(pI.y))) { // i
                pI = p;
                iI = i;
            }
            if (p.y.isMoreThan(pJ.y) || (p.y.equals(pJ.y) && p.x.isMoreThan(pJ.x))) { // ii
                pJ = p;
                iJ = i;
            }
            if (p.x.isMoreThan(pK.x) || (p.x.equals(pK.x) && p.y.isLessThan(pK.y))) { // iii
                pK = p;
                iK = i;
            }
            if (p.y.isLessThan(pL.y) || (p.y.equals(pL.y) && p.x.isLessThan(pL.x))) { // iv
                pL = p;
                iL = i;
            }
        }
        cals = new Caliper[]{
                new Caliper(new RPoint2D(pI.x, pI.y.minusOne()), pI, iI, convexHull), // I
                new Caliper(new RPoint2D(pJ.x.minusOne(), pJ.y), pJ, iJ, convexHull), // J
                new Caliper(new RPoint2D(pK.x, pK.y.plusOne()), pK, iK, convexHull),  // K
                new Caliper(new RPoint2D(pL.x.plusOne(), pL.y), pL, iL, convexHull)   // L
        };
    }

    /**
     * Rotate all cals
     *
     * @param minTangent the tangent of the rotation
     */
    private static void rotateCalipersBy(Rational minTangent) {
        Rational slopeIK = null;
        Rational slopeJL = null;
        Stack<Integer> todo = new Stack<Integer>();

        // First handle all calipers that have the smallest 'tangent': can be more than one!
        for (int i = 0; i < cals.length; i++) {
            if (minTangent.equals(cals[i].getTangentNextCaliper())) {
                cals[i] = cals[i].nextCaliper();
                if (i == 0 || i == 2) { // I or K
                    slopeIK = cals[i].getSlope();
                } else {                 // J or L
                    slopeJL = cals[i].getSlope();
                }
            } else {
                todo.push(i);
            }
        }

        // Set the slopes that have not yet been set. One is definitely assigned, but
        // it is possible both have been set already.
        if (slopeIK == null) slopeIK = zero.equals(slopeJL) ? inf : minOne.divide(slopeJL);
        if (slopeJL == null) slopeJL = zero.equals(slopeIK) ? inf : minOne.divide(slopeIK);

        // Handle the calipers that have not yet been rotated (could even be none!).
        while (!todo.isEmpty()) {
            int index = todo.pop();
            if (index == 0 || index == 2) { // I or K
                cals[index] = cals[index].nextCaliper(slopeIK);
            } else {                         // J or L
                cals[index] = cals[index].nextCaliper(slopeJL);
            }
        }
    }

    /**
     * A class that represents a caliper used by the {@link RotatingCalipers} class.
     * <p>
     * Author: Bart Kiers, bart@big-o.nl <br />
     * Date: Apr 3, 2010
     * </p>
     */
    private static final class Caliper {

        /**
         * the point to which this caliper turns around.
         */
        RPoint2D turnPoint;

        /**
         * the line
         */
        RLine2D line;

        /**
         * the convex hull this caliper is going to turn around.
         */
        private final List<RPoint2D> convexHull;

        /**
         * the index of the 'turnPoint' in the convex hull
         */
        private int index;

        /**
         * flag that denotes whether this caliper is pointing up (calipers I and L)
         * or is pointing down (calipers J and K).
         */
        //private final boolean pointingUp;

        /**
         * Creates a new caliper going through the points <code>p1</code>
         * and <code>p2</code>.
         *
         * @param p1  the fist point.
         * @param p2  the second point.
         * @param idx the index of <code>turnPoint</code> of this caliper.
         * @param ch  the convex hull this caliper is going to turn around.
         */
        Caliper(RPoint2D p1, RPoint2D p2, int idx, List<RPoint2D> ch) {
            turnPoint = p2;
            line = new RLine2D(p1, p2);
            convexHull = ch;
            setIndex(idx);
        }

        /**
         * Creates a new caliper going through the point <code>p</code>
         * and with a slope <code>slope</code>.
         *
         * @param m   the slope of this caliper.
         * @param p   the point this caliper is going through.
         * @param idx the index of <code>turnPoint</code> of this caliper.
         * @param ch  the convex hull this caliper is going to turn around.
         */
        private Caliper(Rational m, RPoint2D p, int idx, List<RPoint2D> ch) {
            turnPoint = p;
            line = new RLine2D(m, turnPoint);
            convexHull = ch;
            setIndex(idx);
        }

        /**
         * Returns the slope of this caliper.
         *
         * @return the slope of this caliper.
         */
        Rational getSlope() {
            return line.slope;
        }

        /**
         * Returns the tangent of the next caliper that is formed. Returns null when
         * the sign of this caliper is different than the sign of the next caliper, in
         * which case the next caliper made an overall turn of 90 degrees or more.
         *
         * @return the tangent of the next caliper that is formed.
         */
        Rational getTangentNextCaliper() {
            Caliper next = this.nextCaliper();

            // Only the first rectangle formed by the calipers is aligned with the
            // x- and y-axis, so the next calipers cannot be horizontal or vertical.
            if (next.line.isHorizontal() || next.line.isVertical()) {
                return null;
            }

            // The sign of the slope should not change, if it does, it means the
            // caliper made an overall turn of more than 90 degrees.
            if (!line.isHorizontal() && !line.isVertical()) {
                if ((line.slope.isPositive() && next.line.slope.isNegative()) ||
                        (line.slope.isNegative() && next.line.slope.isPositive())) {
                    return null;
                }
            }

            return this.line.tangent(next.line);
        }

        /**
         * Returns the next "turning-point" of this caliper.
         *
         * @return the next "turning-point" of this caliper.
         */
        private RPoint2D nextHead() {
            return convexHull.get(index + 1);
        }

        /**
         * Returns a new <code>Caliper</code> which is the the next <code>Caliper</code>
         * after <code>this</code> one. The <code>turnPoint</code> becomes the
         * <code>nextHead()</code>.
         *
         * @return a new <code>Caliper</code> which is the the next <code>Caliper</code>
         *         after <code>this</code> one.
         */
        Caliper nextCaliper() {
            return new Caliper(turnPoint, nextHead(), index + 1, convexHull);
        }

        /**
         * Returns a new <code>Caliper</code> which is the the next <code>Caliper</code>
         * after <code>this</code> one. The <code>turnPoint</code> is the same as
         * <code>this</code> caliper, only the slope of the caliper is changed.
         *
         * @param slope the new slope of this caliper.
         * @return a new <code>Caliper</code> which is the the next <code>Caliper</code>
         *         after <code>this</code> one.
         */
        Caliper nextCaliper(Rational slope) {
            return new Caliper(slope, turnPoint, index, convexHull);
        }

        /**
         * Set the index of the <code>turnPoint</code> in the <code>convexHull</code>.
         *
         * @param idx the index of the <code>turnPoint</code> in the <code>convexHull</code>.
         */
        private void setIndex(int idx) {
            // Note that the last point in the convex hull is the same as the first,
            // that's why the last element, or the index of that element, is skipped
            index = idx == convexHull.size() - 1 ? 0 : idx;
        }

        /**
         * Returns a String representation of this object.
         *
         * @return a String representation of this object.
         */
        @Override
        public String toString() {
            return String.format("Caliper[line=%s, rotation-point=%s]", line, turnPoint);
        }
    }
}