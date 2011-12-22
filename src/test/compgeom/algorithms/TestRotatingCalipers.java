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
import compgeom.RPolygon2D;
import compgeom.RRectangle;
import compgeom.util.CGUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 13, 2010
 * </p>
 */
public class TestRotatingCalipers {

    @Test
    public void testGetBoundingRectangles() {
        List<RPoint2D> points = CGUtil.createRPoint2DList(
                new int[]{0, 1, 1, 2, 2, 2, 4, 4, 5},
                new int[]{1, 0, 1, 1, 2, 3, 4, 5, 4}
        );
        List<RRectangle> rectangles = RotatingCalipers.getBoundingRectangles(points);
        assertTrue(rectangles.size() == 2);
    }

    @Test
    public void testGetMinimumBoundingRectangles() {
        List<RPoint2D> points = CGUtil.createRPoint2DList(
                new int[]{0, 1, 1, 2, 2, 2, 4, 4, 5},
                new int[]{1, 0, 1, 1, 2, 3, 4, 5, 4}
        );
        RRectangle minimum = RotatingCalipers.getMinimumBoundingRectangle(points);
        RRectangle expected = new RRectangle(new RPoint2D(1, 0), new RPoint2D(5, 4), new RPoint2D(4, 5), new RPoint2D(0, 1));

        assertTrue(minimum.equals(expected));

        // corner case 
        RotatingCalipers.getMinimumBoundingRectangle(CGUtil.createRPoint2DList(
                new int[]{0, 0, 2, 3, 3, 2},
                new int[]{0, 1, 3, 3, 2, 1}
            )
        );
    }

    @Test
    public void testGetBoundingRectangles_Random() {

        Random rand = new Random();
        final int tests = 1000;
        final int maxSizePolygon = 6;
        final int maxDim = 10;

        for (int t = 0; t < tests; t++) {
            List<RPoint2D> points = null;
            RPolygon2D poly = null;
            try {
                int prevX = 0;
                int prevY = 0;
                int size = maxSizePolygon; //rand.nextInt(maxSizePolygon - 3) + 3; // Between 3 and maxSizePolygon
                points = new ArrayList<RPoint2D>(size);
                while (size-- > 0) {
                    int x = prevX + rand.nextInt(maxDim) + 1;
                    int y = prevY + rand.nextInt(maxDim) + 1;
                    points.add(new RPoint2D(x, y));
                    prevX = x;
                    prevY = y;
                }

                try {
                    poly = new RPolygon2D(points);
                } catch(IllegalArgumentException e) {
                    poly = null;
                    t--;
                }

                if(poly != null) {
                    List<RRectangle> rectangles = RotatingCalipers.getBoundingRectangles(poly);

                    // Test if all points are really inside all rectangles
                    for (RPoint2D p : points) {
                        for (RRectangle r : rectangles) {
                            if (!r.contains(p)) {
                                System.out.println("points = "+points+"\nhull = " + GrahamScan.getConvexHull(points) + "\n\n" + r);
                                System.out.println("\np=" + p);
                            }
                            assertTrue(r.contains(p));
                        }
                    }
                }
            } catch(Exception e) {
                System.out.println("points = "+points+"\npoly = "+poly);
                e.printStackTrace();
                assertTrue(false);
            }
        }
    }
}
