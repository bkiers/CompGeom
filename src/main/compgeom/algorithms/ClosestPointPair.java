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

import compgeom.RPoint2D;
import compgeom.Rational;
import compgeom.util.CGUtil;
import compgeom.util.Extremal;
import compgeom.util.Pair;

import java.util.*;

/**
 * <p>
 * A class used to get the closest pair of points from a given
 * collection. It finds the closest pair from the collection of
 * size <code>n</code> in <code>O(n * log(n))</code> time using
 * a straightforward <i>"divide-and-conquer"</i> strategy.
 * </p>
 * <p>Below follows the pseudo code of the implementation:</p>
 * <pre>
 * find(points): Pair
 *   set := Set(points)
 *   x   := set as an array, sorted by their x coordinates
 *   y   := set as an array, sorted by their y coordinates
 *   return findRecursive(x, y)
 * end
 *
 * findRecursive(xSorted, ySorted): Pair
 *   if xSorted.length <= 3
 *     return bruteForce(xSorted)
 *   else
 *     xLeft      := the left side of xSorted divided in two
 *     xRight     := the right side of xSorted divided in two
 *     midPoint   := the point in the middle (end of xLeft or start of xRight)
 *     yLeft      := the points from xLeft, sorted by their y coordinates in O(n)
 *     yRight     := the points from xRight, sorted by their y coordinates in O(n)
 *     pairLeft   := findRecursive(xLeft, yLeft)
 *     pairRight  := findRecursive(xRight, yRight)
 *     pairMin    := pairLeft or pairRight, the one with the smallest distance between its points
 *     candidates := the points that are within bounds of pairMin.distance from midPoint
 *     closest    := pairMin
 *
 *     for i := 0 to candidates.length-1
 *       j := i + 1
 *       while j < candidates.length && |candidates[j].y - candidates[i].y| < pairMin.distance
 *         closest := closest or Pair(candidates[j],candidates[i]), the one with the smallest
 *                    distance between its points
 *         j := j + 1
 *       end while
 *     end for
 *
 *     return closest
 *   end if
 * end
 * </pre>
 * <p>
 * For detailed explanations, see:
 * <ul>
 * <li>Cormen, Leiserson and Rivest, Introduction to Algorithms, MIT Press,
 * 1999. Pages 908-912 of section 35.4: Finding the closest pair of points.</li>
 * <li>Sedgewick, Algorithms, second ed., Addison-Wesley 1988. Pages 401-408 of
 * chapter 28: Closest-Point Problems.</li>
 * </ul>
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 28, 2010
 * </p>
 */
public final class ClosestPointPair {

    /**
     * No need to instantiate this class
     */
    private ClosestPointPair() {
    }

    /**
     * Finds the closest pair of points from a given collection. It finds the
     * closest pair from the collection of size 'n' in O(n^2) time. This method
     * is package private since it is used by {@link #find(Collection)} for
     * small sized collections, and is used by the unit tests.
     *
     * @param points the list of points.
     * @return the closest pair of points from a list of points.
     * @throws IllegalArgumentException if <code>points.size()</code> has
     *                                  less than 3 unique points.
     */
    static Pair<RPoint2D> findNaive(Collection<RPoint2D> points)
            throws IllegalArgumentException {
        if (points.size() < 3) {
            throw new IllegalArgumentException("the collection of points should contain at least 3 points");
        }
        List<RPoint2D> lst = new ArrayList<RPoint2D>(new HashSet<RPoint2D>(points));
        Pair<RPoint2D> closestPair = null;
        Rational closestDist = Rational.POSITIVE_INFINITY;

        for (int i = 0; i < lst.size() - 1; i++) {
            RPoint2D a = lst.get(i);

            for (int j = i + 1; j < lst.size(); j++) {
                RPoint2D b = lst.get(j);

                Rational temp = a.distanceSquared(b);

                if (temp.isLessThan(closestDist)) {
                    closestPair = new Pair<RPoint2D>(a, b);
                    closestDist = temp;
                }
            }
        }
        return closestPair;
    }

    /**
     * <p>
     * Finds the closest pair of points from a given collection. It
     * finds the closest pair from the collection of size <code>n</code>
     * in <code>O(n * log(n))</code> time using a straightforward
     * <i>"divide-and-conquer"</i> strategy.
     * </p>
     *
     * @param points the collection of points.
     * @return the closest pair of points from a collection of points.
     * @throws IllegalArgumentException if <code>points.size()</code> has
     *                                  less than 3 unique points.
     */
    public static Pair<RPoint2D> find(Collection<RPoint2D> points)
            throws IllegalArgumentException {
        Set<RPoint2D> set = new HashSet<RPoint2D>(points);
        if (set.size() < 3) {
            throw new IllegalArgumentException("the collection of points should contain at least 3 points");
        }
        RPoint2D[] x = CGUtil.sortAndGetArray(set, Extremal.LEFT_LOWER);
        RPoint2D[] y = CGUtil.sortAndGetArray(set, Extremal.LOWER_LEFT);
        PPair pair = findRecursive(x, y);
        return new Pair<RPoint2D>(pair.m, pair.n);
    }

    /**
     * Recursively find the pair of points closest to each other. Note that
     * 'xSorted' and 'ySorted' contain the same points.
     *
     * @param xSorted the array of points sorted on their x coordinate from left
     *                to right. In case of a tie, the point with the lower y
     *                coordinate is placed before the other point.
     * @param ySorted the array of points sorted on their y coordinate from bottom
     *                to top. In case of a tie, the point with the lower x
     *                coordinate is placed before the other point.
     * @return the pair of points closest to each other from 'xSorted'.
     */
    private static PPair findRecursive(RPoint2D[] xSorted, RPoint2D[] ySorted) {
        // Brute force if the number of points is less than 4 
        if (xSorted.length <= 3) {
            return min(xSorted);
        }

        // Divide 'xSorted' into two approximately the same sized arrays.
        RPoint2D[] xLeft = getX(xSorted, 0, xSorted.length / 2);
        RPoint2D[] xRight = getX(xSorted, xSorted.length / 2, xSorted.length);

        // Get the point that is more or less in the middle.
        RPoint2D midPoint = xRight[0];

        // Divide 'ySorted' into two approximately the same sized arrays.
        RPoint2D[] yLeft = getY(ySorted, xLeft.length, midPoint, true);
        RPoint2D[] yRight = getY(ySorted, xRight.length, midPoint, false);

        // Recursively find the left- and right closest pair of points.
        PPair pairLeft = findRecursive(xLeft, yLeft);
        PPair pairRight = findRecursive(xRight, yRight);
        PPair pairMin = pairLeft.min(pairRight);

        // Get the points that are within bounds of 'pairMin.deltaSquared' from the 'midPoint'
        RPoint2D[] candidates = getCandidates(ySorted, midPoint, pairMin);

        // Initialize the closest pair to the minimum of 'pairLeft' and 'pairRight': 'pairMin'
        PPair closest = pairMin;

        for (int i = 0; i < candidates.length - 1; i++) {
            int j = i + 1;

            // Continue looping until 'j' exceeds candidates.length OR
            // (candidates[j].y - candidates[i].y)^2 is less than 'pairMin.deltaSquared'
            while (j < candidates.length &&
                    candidates[i].y.subtract(candidates[j].y).pow(2).isLessThan(pairMin.deltaSquared)) {

                // Create a new PPair and compare it to the current 'closest' pair.
                closest = closest.min(new PPair(candidates[i], candidates[j]));
                j++;
            }
        }
        return closest;
    }

    /**
     * Returns all points that are within bounds of 'min.deltaSquared' from
     * the 'midPoint'.
     *
     * @param ySorted  the points sorted by their y coordinate.
     * @param midPoint the mid point.
     * @param min      the present closest point pair.
     * @return all points that are within bounds of 'min.deltaSquared' from
     *         the 'midPoint'.
     */
    private static RPoint2D[] getCandidates(RPoint2D[] ySorted, RPoint2D midPoint, PPair min) {
        List<RPoint2D> candidates = new ArrayList<RPoint2D>();
        for (RPoint2D p : ySorted) {
            Rational delta = midPoint.x.subtract(p.x);
            if (delta.pow(2).isLessThan(min.deltaSquared)) {
                candidates.add(p);
            }
        }
        return candidates.toArray(new RPoint2D[candidates.size()]);
    }

    /**
     * Returns a copy of a part of the array 'xSorted'.
     *
     * @param xSorted the original array.
     * @param from    the index from where to start copying.
     * @param to      the index from where to stop copying.
     * @return a copy of a part of the array 'xSorted'.
     */
    private static RPoint2D[] getX(RPoint2D[] xSorted, int from, int to) {
        RPoint2D[] array = new RPoint2D[to - from];
        System.arraycopy(xSorted, from, array, 0, array.length);
        return array;
    }

    /**
     * All points to the left or right of 'midPoint' sorted by their y coordinate.
     * Note that the 'ySorted' is already sorted.
     *
     * @param ySorted  the original array.
     * @param size     the size of the array being returned.
     * @param midPoint the mid point.
     * @param left     if 'true', this method returns all points left
     *                 of the 'midPoint', else all point right of the
     *                 'midPoint' (including the 'midPoint' itself!).
     * @return points to the left or right of 'midPoint' sorted by their
     *         y coordinate.
     */
    private static RPoint2D[] getY(RPoint2D[] ySorted, int size, RPoint2D midPoint, boolean left) {
        RPoint2D[] array = new RPoint2D[size];

        for (int i = 0, j = 0; i < ySorted.length; i++) {
            RPoint2D p = ySorted[i];

            // Is 'p' on the same vertical line as 'midPoint' is?
            boolean onMidLine = p.x.equals(midPoint.x);

            // Only add the point 'p' to the array in the following four cases:
            //  A - if 'p' is to the left of 'midPoint' AND 'left' == true
            //  B - if 'p' is to the right of 'midPoint' AND 'left' == false
            //  C - if 'p' is on the same line as 'midPoint' AND 'left' == true AND 'p' is below 'midPoint'
            //  D - if 'p' is on the same line as 'midPoint' AND 'left' == false AND 'p' is NOT below 'midPoint'
            if ((left && p.isLeftOf(midPoint)) ||                   // A
                    (!left && p.isRightOf(midPoint)) ||             // B
                    (onMidLine && left && p.isBelow(midPoint)) ||   // C
                    (onMidLine && !left && !p.isBelow(midPoint))) { // D
                array[j++] = p;
            }
        }
        return array;
    }

    /**
     * Returns the point-pair with the minimum distance between the two
     * points. The vararg parameter can be between 1 and 3 elements in size.
     *
     * @param pts the points to calculate the minimum between.
     * @return the point-pair with the minimum distance between the two points.
     * @throws RuntimeException if 'pts' is not between 1 and 3 (bot inclusive).
     */
    private static PPair min(RPoint2D... pts) throws RuntimeException {
        switch (pts.length) {
            case 1:
                return new PPair(pts[0], new RPoint2D(Rational.POSITIVE_INFINITY, Rational.POSITIVE_INFINITY));
            case 2:
                return new PPair(pts[0], pts[1]);
            case 3:
                PPair ab = new PPair(pts[0], pts[1]);
                PPair ac = new PPair(pts[0], pts[2]);
                PPair bc = new PPair(pts[1], pts[2]);
                return ab.min(ac).min(bc);
            default:
                throw new RuntimeException("wrong number of points: " + pts.length);
        }
    }

    /**
     * A class that extends a regular Pair by adding a Rational to it that
     * represents the squared distance between the two points. Note that the
     * squared value of the distance between two points is being calculated
     * with since that is the only way to assure exact results not suffering
     * from floating point round-off errors.
     */
    private static class PPair extends Pair<RPoint2D> {

        /**
         * The squared distance between 'n' and 'm'.
         */
        final Rational deltaSquared;

        /**
         * Creates a new point-pair.
         *
         * @param m the first point.
         * @param n the second point.
         */
        PPair(RPoint2D m, RPoint2D n) {
            super(m, n);
            deltaSquared = m.distanceSquared(n);
        }

        /**
         * Returns the pair of points with the smallest 'deltaSquared'.
         *
         * @param that the other pair to compare 'this' with.
         * @return the pair of points with the smallest 'deltaSquared'.
         */
        PPair min(PPair that) {
            return this.deltaSquared.isLessThan(that.deltaSquared) ? this : that;
        }
    }
}
