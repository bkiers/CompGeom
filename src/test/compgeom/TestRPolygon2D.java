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
package compgeom;

import compgeom.algorithms.GrahamScan;
import compgeom.util.CGUtil;
import org.junit.Test;

import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 13, 2010
 * </p>
 */
public class TestRPolygon2D {

    // constructors

    @Test
    public void testRPolygon2DValid() {
        new RPolygon2D(new RPoint2D(1, 1), new RPoint2D(2, 2), new RPoint2D(3, 1), new RPoint2D(2, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRPolygon2DInvalid1() {
        // less than 3 points
        new RPolygon2D(new RPoint2D(1, 1), new RPoint2D(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRPolygon2DInvalid2() {
        new RPolygon2D(new RPoint2D(1, 1), new RPoint2D(2, 2), new RPoint2D(3, 3), new RPoint2D(4, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRPolygon2DInvalid3() {
        new RPolygon2D(new int[]{1, 2, 3, 2}, new int[]{1, 2, 1});
    }

    // methods

    @Test
    public void testEquals() {
        RPolygon2D A = new RPolygon2D(new int[]{3, 2, 1, 2}, new int[]{1, 0, 1, 2});
        RPolygon2D a = new RPolygon2D(new int[]{1, 2, 3, 2}, new int[]{1, 2, 1, 0});
        RPolygon2D b = new RPolygon2D(new int[]{1, 2, 3, 2}, new int[]{1, 2, 1, 0});
        RPolygon2D c = new RPolygon2D(new int[]{0, 2, 3, 2}, new int[]{1, 2, 1, 0});
        RPolygon2D d = new RPolygon2D(new int[]{3, 2, 1, 2}, new int[]{1, 2, 1, 0});

        assertTrue(A.equals(a));
        assertTrue(a.equals(b));
        assertTrue(a.equals(d));
        assertFalse(a.equals(c));
    }

    @Test
    public void testHashCode() {
        RPolygon2D a = new RPolygon2D(new int[]{1, 2, 3, 2}, new int[]{1, 2, 1, 0});
        RPolygon2D b = new RPolygon2D(new int[]{1, 2, 3, 2}, new int[]{1, 2, 1, 0});
        RPolygon2D c = new RPolygon2D(new int[]{0, 2, 3, 2}, new int[]{1, 2, 1, 0});
        RPolygon2D d = new RPolygon2D(new int[]{3, 2, 1, 2}, new int[]{1, 2, 1, 0});

        assertTrue(a.hashCode() == b.hashCode());
        assertTrue(a.hashCode() == d.hashCode());
        assertFalse(a.hashCode() == c.hashCode());
    }

    @Test
    public void testGetPoints() {
        List<RPoint2D> ptsA = CGUtil.createRPoint2DList(new int[]{1, 2, 3, 2}, new int[]{1, 2, 1, 0});
        List<RPoint2D> ptsB = CGUtil.createRPoint2DList(new int[]{3, 2, 1, 2}, new int[]{1, 0, 1, 2});

        List<RPoint2D> expected = CGUtil.createRPoint2DList(
                new int[]{2, 1, 2, 3},
                new int[]{0, 1, 2, 1}
        );

        RPolygon2D polyA = new RPolygon2D(ptsA);
        RPolygon2D polyB = new RPolygon2D(ptsB);

        assertTrue(polyA.getPoints().equals(expected));
        assertTrue(polyB.getPoints().equals(expected));
    }

    @Test
    public void testSize() {
        RPolygon2D a = new RPolygon2D(CGUtil.createRPoint2DList(new int[]{1, 2, 3, 2}, new int[]{1, 2, 1, 0}));
        assertTrue(a.size() == 4);
    }
    
    @Test
    public void testGetSegments() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("2 1 2 3 2 5 4 6 6 4 5 1");
        RPolygon2D poly = new RPolygon2D(points);
        assertTrue(poly.getSegments().equals(new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(
                "2 1 2 3   2 3 2 5   2 5 4 6   4 6 6 4   6 4 5 1   5 1 2 1"))));
        assertTrue(poly.getSegments().equals(new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(
                "4 6 6 4   6 4 5 1   5 1 2 1   2 1 2 3   2 3 2 5   2 5 4 6"))));
    }

    @Test
    public void testIsSimple() {
        assertTrue(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 5 0 1 2 4")).isSimple());
        assertTrue(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 2 5 5 5 2 3 2 3 -1 2 1 2 -1")).isSimple());

        assertFalse(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 5 0 1 3 5")).isSimple());
        assertFalse(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 2 5 5 5 2 3 2 3 -1 2 1 3 3")).isSimple());
    }

    @Test
    public void testIsComplex() {
        assertFalse(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 5 0 1 2 4")).isComplex());
        assertFalse(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 2 5 5 5 2 3 2 3 -1 2 1 2 -1")).isComplex());

        assertTrue(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 5 0 1 3 5")).isComplex());
        assertTrue(new RPolygon2D(CGUtil.createRPoint2DList("1 1 3 4 2 2 5 5 5 2 3 2 3 -1 2 1 3 3")).isComplex());
    }

    @Test
    public void testIsConvex() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("2 1 2 3 2 5 4 6 6 4 5 1");
        RPolygon2D poly = new RPolygon2D(points);
        assertTrue(poly.isConvex());

        points = CGUtil.createRPoint2DList("3 0 1 2 0 3 3 4 3 3 6 3 5 2");
        poly = new RPolygon2D(points);
        assertFalse(poly.isConvex());

        points = CGUtil.createRPoint2DList("3 0 1 2 0 3 3 4 6 3 5 2");
        poly = new RPolygon2D(points);
        assertTrue(poly.isConvex());

        points = CGUtil.createRPoint2DList("3 0 5 2 6 3 3 4 0 3 1 2");
        poly = new RPolygon2D(points);
        assertTrue(poly.isConvex());
    }

    @Test
    public void testIsConvexRandom() {
        final Random r = new Random();
        final int box = 10;
        final int tests = 1000;

        int previousX = 0;
        int previousY = 0;

        for(int i = 1; i <= tests; i++) {
            List<RPoint2D> points = new ArrayList<RPoint2D>();

            for(int j = 0; j < 4; j++) {
                int x = previousX + 1 + r.nextInt(box);
                int y = previousY + 1 + r.nextInt(box);
                points.add(new RPoint2D(x, y));
                previousX = x;
                previousY = y;
            }

            if(!CGUtil.allCollinear(points)) {

                RPolygon2D poly = new RPolygon2D(points);

                if(poly.isConvex()) {

                    List<RPoint2D> temp = CGUtil.removeCollinear(points);
                    poly = new RPolygon2D(temp);

                    List<RPoint2D> convexHull = GrahamScan.getConvexHull(points);
                    convexHull.remove(convexHull.size()-1);
                    RPolygon2D convex = new RPolygon2D(convexHull);

                    assertTrue("ERROR ::\npoints="+points+"\npoly="+poly+"\n!=\nconvex="+convex, convex.equals(poly));
                }
            }
        }
    }

    @Test
    public void testContainsSimple() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("1 1 4 7 5 4 7 5 8 7 9 3 6 3");
        RPolygon2D poly = new RPolygon2D(points);
        assertTrue(poly.contains(new RPoint2D(4,3)));
        assertTrue(poly.contains(new RPoint2D(4,4)));
        assertTrue(poly.contains(new RPoint2D(7,4)));
        assertTrue(poly.contains(new RPoint2D(7,3)));
        assertTrue(poly.contains(new RPoint2D(4,7)));
        assertFalse(poly.contains(new RPoint2D(6,6)));
        assertFalse(poly.contains(new RPoint2D(10,5)));
        assertFalse(poly.contains(new RPoint2D(-1000000,5)));
        assertFalse(poly.contains(new RPoint2D(1000000,5)));
        assertFalse(poly.contains(new RPoint2D(4,-1000000)));
        assertFalse(poly.contains(new RPoint2D(4,1000000)));

        points = CGUtil.createRPoint2DList("-9 -9 -6 -3 -5 -6 -3 -5 -2 -3 -1 -7 -4 -7");
        poly = new RPolygon2D(points);
        assertTrue(poly.contains(new RPoint2D(-6,-3)));
        assertTrue(poly.contains(new RPoint2D(-6,-6)));
        assertTrue(poly.contains(new RPoint2D(-6,-7)));
        assertTrue(poly.contains(new RPoint2D(-3,-7)));
        assertTrue(poly.contains(new RPoint2D(-3,-6)));
        assertFalse(poly.contains(new RPoint2D(-1,-5)));
        assertFalse(poly.contains(new RPoint2D(-4,-4)));
        assertFalse(poly.contains(new RPoint2D(-1000000,5)));
        assertFalse(poly.contains(new RPoint2D(1000000,5)));
        assertFalse(poly.contains(new RPoint2D(4,-1000000)));
        assertFalse(poly.contains(new RPoint2D(4,1000000)));
    }

    @Test
    public void testContainsComplex() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("1 1 1 8 8 1 2 1 10 4 10 10 5 9");
        RPolygon2D poly = new RPolygon2D(points);

        assertTrue(poly.contains(new RPoint2D(2,6)));
        assertTrue(poly.contains(new RPoint2D(1,8)));
        assertTrue(poly.contains(new RPoint2D(4,5)));
        assertTrue(poly.contains(new RPoint2D(6,2)));
        assertTrue(poly.contains(new RPoint2D(9,4)));
        assertTrue(poly.contains(new RPoint2D(10,8)));

        assertFalse(poly.contains(new RPoint2D(2,2)));
        assertFalse(poly.contains(new RPoint2D(0,0)));
        assertFalse(poly.contains(new RPoint2D(3,4)));
        assertFalse(poly.contains(new RPoint2D(5,3)));
        assertFalse(poly.contains(new RPoint2D(3,7)));
        assertFalse(poly.contains(new RPoint2D(4,9)));
        assertFalse(poly.contains(new RPoint2D(8,10)));
        assertFalse(poly.contains(new RPoint2D(8,2)));
        assertFalse(poly.contains(new RPoint2D(11,6)));

        assertFalse(poly.contains(new RPoint2D(-1000000,5)));
        assertFalse(poly.contains(new RPoint2D(1000000,5)));
        assertFalse(poly.contains(new RPoint2D(4,-1000000)));
        assertFalse(poly.contains(new RPoint2D(4,1000000)));

    }
}
