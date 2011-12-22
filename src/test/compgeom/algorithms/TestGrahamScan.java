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
import compgeom.util.CGUtil;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 13, 2010
 * </p>
 */
public class TestGrahamScan {

    @Test(expected = IllegalArgumentException.class)
    public void testGetConvexHull_IllegalArgumentException_1() {
        GrahamScan.getConvexHull(CGUtil.createRPoint2DList(
                new int[]{1, 2},  // less than 3 points
                new int[]{1, 2}
            )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetConvexHull_IllegalArgumentException_2() {
        GrahamScan.getConvexHull(CGUtil.createRPoint2DList(
                new int[]{1, 5, -3},  // all points are collinear
                new int[]{1, 5, -3}
            )
        );
    }

    @Test
    public void testGetConvexHull() {
        List<RPoint2D> hull = GrahamScan.getConvexHull(CGUtil.createRPoint2DList(
                new int[]{0, 1, 2, 3, 4, 5, 4, 4, 3, 3, 3, 2, 1},
                new int[]{0, 1, 2, 3, 2, 1, 0, -1, 2, 1, 0, -2, 0}
            )
        );
        List<RPoint2D> expectedHull = CGUtil.createRPoint2DList(
                new int[]{2, 4, 5, 3, 0, 2},
                new int[]{-2, -1, 1, 3, 0, -2}
        );

        assertTrue(hull.get(0).equals(hull.get(hull.size() - 1)));
        assertTrue(expectedHull.equals(hull));
    }
}
