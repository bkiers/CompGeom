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
package compgeom.demos;

import compgeom.*;
import compgeom.algorithms.GrahamScan;
import compgeom.algorithms.RotatingCalipers;
import compgeom.util.CGUtil;

import java.util.List;

/**
 * <p>
 * A class containing a couple of examples of how to use this library.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public class Examples {

    private Examples() {}

    private static void convexHullDemo() {
        // create a list of points
        List<RPoint2D> points = CGUtil.createRPoint2DList(
                "(1,1) (3,5) (-1,-3) (-3,5) (8,2) (9,2) (4,5) (2,4)"
        );
        // get the convex hull of the list of points
        List<RPoint2D> convexHull = GrahamScan.getConvexHull(points);
        System.out.printf("\n%s\npoints = %s\nconvex hull = %s\n",
                "=== convexHullDemo() ===", points, convexHull);
    }

    private static void lineDemo() {
        // create equal line using different c-tors
        RLine2D a = new RLine2D(new RPoint2D(-1,-1), new RPoint2D(-5,-5)); // point, point
        RLine2D b = new RLine2D(new Rational(1), new RPoint2D(2,2)); // slope, point
        RLine2D c = new RLine2D(new Rational(1), new RPoint2D(2,2)); // slope, constant
        RLine2D d = new RLine2D("f(x) -> x"); // function
        RLine2D e = new RLine2D("y = 1/1*x + 0"); // function
        System.out.printf("\n%s\na = %s\nb = %s\nc = %s\nd = %s\ne = %s\na == b == c == d == e ? %s\n",
                "=== lineDemo() ===", a, b, c, d, e,
                a.equals(b) && a.equals(c) && a.equals(d) && a.equals(e));
    }

    private static void rotatingCalipersDemo() {
        // create a list of points
        List<RPoint2D> points = CGUtil.createRPoint2DList(
                new int[]{0, 50, 30, 20, 80, 40, 130, 20, 70, 50, 100},
                new int[]{0, 10, 20, 160, 20, 30, 70, 50, 110, 10, 40}
        );
        // find the minimum bounding rectangle of the list of points
        RRectangle rectangle = RotatingCalipers.getMinimumBoundingRectangle(points);
        System.out.printf("\n%s\npoints = %s\nminimum bounding rectangle =\n%s\n",
                "=== rotatingCalipersDemo() ===", points, rectangle);
    }

    /**
     * Entry point of this class: it runs the demos.
     *
     * @param args command line parameters, are ignored.
     */
    public static void main(String[] args) {
        convexHullDemo();
        lineDemo();
        rotatingCalipersDemo();
        System.out.println("Done.");
    }
}