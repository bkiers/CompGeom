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

import compgeom.Rational;
import compgeom.RLine2D;
import compgeom.RPoint2D;
import compgeom.RPolygon2D;
import compgeom.util.CGUtil;
import compgeom.util.Extremal;

import java.util.*;

/**
 * <p>
 * A class that finds the <a href="http://mathworld.wolfram.com/ConvexHull.html">convex hull</a>
 * of a set of 2 dimensional points using the
 * <a href="http://en.wikipedia.org/wiki/Graham_scan">Graham scan</a>
 * algorithm which finds the convex hull in <code>O(n * log(n))</code>
 * time where <code>n</code> is the size of the set of points.
 * </p>
 * <p>
 * For a detailed explanation, see: O'Rourke, J. (1998), "Section 3.5. Graham's Algorithm",
 * Computational Geometry in C (2nd ed.), Cambridge University Press, pages 72-86.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class GrahamScan {

    /**
     * No need to instantiate this class
     */
    private GrahamScan() {
    }

    /**
     * Returns the convex hull of the list of points in
     * <code>O(N*log(N))</code> time. Note that the returned
     * list is a counter-clockwise 'closed' walk: the first and
     * last points are the same.
     *
     * @param points a list of points.
     * @return the convex hull of the list of points.
     * @throws IllegalArgumentException if <code>points</code> contains less
     *                                  than 3 points or if all points are
     *                                  collinear.
     */
    public static List<RPoint2D> getConvexHull(List<RPoint2D> points)
            throws IllegalArgumentException {
        return getConvexHull(new RPolygon2D(points));
    }

    /**
     * Returns the convex hull of the list of points in
     * <code>O(N*log(N))</code> time or in <code>O(N)</code> time
     * if it's already know the <code>polygon</code> is convex. Note
     * that the returned list is a counter-clockwise 'closed' walk:
     * the first and last points are the same.
     *
     * @param polygon the polygon.
     * @return the convex hull of the set of points as a
     *         counter-clockwise, 'closed' walk.
     */
    public static List<RPoint2D> getConvexHull(RPolygon2D polygon) {

        // If the polygon is already convex, there's no need to
        // perform the Graham scan.
        if(polygon.isConvex()) {

            List<RPoint2D> hull = CGUtil.removeCollinear(polygon.getPoints());

            // We want a counter-clockwise walk.
            if(CGUtil.allClockWise(hull)) {
                RPoint2D first = hull.remove(0);
                Collections.reverse(hull);
                hull.add(0, first);
            }

            // close the 'walk'
            hull.add(hull.get(0));

            // remove all collinear points
            return CGUtil.removeCollinear(hull);
        }
        else {
            //  The Graham scan.

            Set<RPoint2D> points = new HashSet<RPoint2D>(polygon.getPoints());
            List<RPoint2D> sorted = getSortedFromLowestY(points);
            removeCollinearPoints(sorted);

            Stack<RPoint2D> stack = new Stack<RPoint2D>();
            stack.push(sorted.get(0));
            stack.push(sorted.get(1));
            stack.push(sorted.get(2));

            for (int i = 3; i < sorted.size(); i++) {

                RPoint2D head = sorted.get(i);
                RPoint2D middle = stack.pop();
                RPoint2D tail = stack.pop();

                if (CGUtil.formsLeftTurn(tail, middle, head)) {
                    stack.push(tail);
                    stack.push(middle);
                    stack.push(head);
                } else {
                    stack.push(tail);
                    i--;
                }
            }

            // close the 'walk'
            stack.push(sorted.get(0));

            return new ArrayList<RPoint2D>(stack);
        }
    }

    /**
     * Returns the set of points sorted in increasing order of the
     * degrees around the point with the lowest y coordinate.
     *
     * @param points the set of points.
     * @return Returns the set of points sorted in increasing order of the
     *         degrees around the point with the lowest y coordinate.
     */
    private static List<RPoint2D> getSortedFromLowestY(Set<RPoint2D> points) {
        List<RPoint2D> sorted = new ArrayList<RPoint2D>(points);
        final RPoint2D lowestY = CGUtil.getExtremalPoint(sorted, Extremal.LOWER_LEFT);
        Collections.sort(sorted, new Comparator<RPoint2D>() {
            @Override
            public int compare(RPoint2D pA, RPoint2D pB) {
                if (pA.equals(lowestY)) return -1;
                if (pB.equals(lowestY)) return 1;

                Rational slopeA = new RLine2D(lowestY, pA).slope;
                Rational slopeB = new RLine2D(lowestY, pB).slope;

                if (slopeA.equals(slopeB)) {
                    Rational distanceAToLowestY = pA.distanceXY(lowestY);
                    Rational distanceBToLowestY = pB.distanceXY(lowestY);
                    return distanceAToLowestY.compareTo(distanceBToLowestY);
                }

                if (slopeA.isPositive() && slopeB.isNegative()) {
                    return -1;
                } else if (slopeA.isNegative() && slopeB.isPositive()) {
                    return 1;
                } else {
                    return slopeA.compareTo(slopeB);
                }
            }
        });
        return sorted;
    }

    /**
     * Removes all collinear points in the list of sorted points.
     * If a straight line can be drawn through the points A, B, C, D
     * then B and C are removed from the list.
     *
     * @param sorted the list of sorted points.
     */
    public static void removeCollinearPoints(List<RPoint2D> sorted) {
        RPoint2D lowestY = sorted.get(0);
        for (int i = sorted.size() - 1; i > 1; i--) {
            Rational slopeB = new RLine2D(lowestY, sorted.get(i)).slope;
            Rational slopeA = new RLine2D(lowestY, sorted.get(i - 1)).slope;
            if (slopeB.equals(slopeA)) {
                sorted.remove(i - 1);
            }
        }
    }
}