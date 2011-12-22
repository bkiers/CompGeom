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

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public class TestRRectangle {

    // helper method to create a rectangle

    private RRectangle rectangle(int p1x, int p1y, int p2x, int p2y, int p3x, int p3y, int p4x, int p4y) {
        return new RRectangle(new RPoint2D(p1x, p1y), new RPoint2D(p2x, p2y),
                new RPoint2D(p3x, p3y), new RPoint2D(p4x, p4y));
    }

    @Test
    public void testRRectangleRLine2DRLine2DRLine2DRLine2DValid() {
        new RRectangle(new RLine2D(new Rational("1"), new Rational("0")),
                new RLine2D(new Rational("-1"), new Rational("0")),
                new RLine2D(new Rational("1"), new Rational("4")),
                new RLine2D(new Rational("-1"), new Rational("-8")));
    }

    @Test(expected = RuntimeException.class)
    public void testRRectangleRLine2DRLine2DRLine2DRLine2DInvalid() {
        new RRectangle(new RLine2D(new Rational("1"), new Rational("0")),
                new RLine2D(new Rational("-1"), new Rational("0")),
                new RLine2D(new Rational("1"), new Rational("4")),
                new RLine2D(new Rational("-2"), new Rational("-8")));
    }

    @Test
    public void testRRectangleRPoint2DRPoint2DRPoint2DRPoint2DValid() {
        new RRectangle(new RPoint2D(2, 2), new RPoint2D(3, 4), new RPoint2D(5, 3), new RPoint2D(4, 1));
    }

    @Test(expected = RuntimeException.class)
    public void testRRectangleRPoint2DRPoint2DRPoint2DRPoint2DInvalid() {
        new RRectangle(new RPoint2D(1, 1), new RPoint2D(1, 1), new RPoint2D(1, 1), new RPoint2D(1, 1));
    }

    @Test
    public void testArea() {
        RRectangle r1 = rectangle(0, 0, 3, 4, 7, 1, 4, -3);
        RRectangle r2 = rectangle(0, 0, 0, 1, 1, 1, 1, 0);

        assertTrue(r1.area() == 25.0);
        assertTrue(r2.area() == 1.0);
    }

    @Test
    public void testAreaSquared() {
        RRectangle r1 = rectangle(0, 0, 3, 4, 7, 1, 4, -3);
        RRectangle r2 = rectangle(0, 0, 0, 1, 1, 1, 1, 0);

        assertTrue(r1.areaSquared().equals(new Rational(25 * 25)));
        assertTrue(r2.areaSquared().equals(new Rational("1")));
    }

    @Test
    public void testCenter() {
        RRectangle r1 = rectangle(0, 0, 0, 4, 4, 4, 4, 0);
        RRectangle r2 = rectangle(0, 4, 4, 4, 4, 0, 0, 0);
        RRectangle r3 = rectangle(1, 1, 1, -1, -1, -1, -1, 1);
        RRectangle r4 = rectangle(0, 2, 4, 6, 6, 4, 2, 0);

        assertTrue(r1.center().equals(new RPoint2D(2, 2)));
        assertTrue(r2.center().equals(new RPoint2D(2, 2)));
        assertTrue(r3.center().equals(RPoint2D.ORIGIN));
        assertTrue(r4.center().equals(new RPoint2D(3, 3)));
    }

    @Test
    public void testContains() {
        RRectangle r1 = rectangle(3, 1, 2, 4, 8, 6, 9, 3);
        // inside
        assertTrue(r1.contains(new RPoint2D(4, 2)));
        assertTrue(r1.contains(new RPoint2D(3, 4)));
        assertTrue(r1.contains(new RPoint2D(6, 2))); // on a segment
        assertTrue(r1.contains(new RPoint2D(8, 3)));
        assertTrue(r1.contains(new RPoint2D(8, 5)));
        assertTrue(r1.contains(new RPoint2D(8, 6))); // on a corner
        // outside
        assertFalse(r1.contains(new RPoint2D(8, 2)));
        assertFalse(r1.contains(new RPoint2D(8, 7)));
        assertFalse(r1.contains(new RPoint2D(80000, -2)));
        assertFalse(r1.contains(new RPoint2D(-1, -1)));

        RRectangle r2 = rectangle(2, 1, 2, 6, 9, 6, 9, 1); // x- and y-axis aligned
        // inside
        assertTrue(r2.contains(new RPoint2D(4, 2)));
        assertTrue(r2.contains(new RPoint2D(3, 4)));
        assertTrue(r2.contains(new RPoint2D(6, 1))); // on a segment
        assertTrue(r2.contains(new RPoint2D(8, 3)));
        assertTrue(r2.contains(new RPoint2D(8, 5)));
        assertTrue(r2.contains(new RPoint2D(2, 1))); // on a corner
        // outside
        assertFalse(r1.contains(new RPoint2D(8, 0)));
        assertFalse(r1.contains(new RPoint2D(8, 7)));
        assertFalse(r1.contains(new RPoint2D(80000, -2)));
        assertFalse(r1.contains(new RPoint2D(-1, -1)));
    }

    @Test
    public void testEquals() {
        RRectangle r1 = rectangle(3, 1, 2, 4, 8, 6, 9, 3);
        RRectangle r2 = rectangle(9, 3, 3, 1, 2, 4, 8, 6);
        RRectangle r3 = rectangle(0, 0, 0, 1, 1, 1, 1, 0);

        assertTrue(r1.equals(r2));
        assertFalse(r1.equals(r3));
    }

    @Test
    public void testGetPoints() {
        RPoint2D[] points = {new RPoint2D(2, 2), new RPoint2D(3, 4), new RPoint2D(5, 3), new RPoint2D(4, 1)};

        RRectangle r1 = rectangle(2, 2, 3, 4, 5, 3, 4, 1);
        RRectangle r2 = rectangle(9, 3, 3, 1, 2, 4, 8, 6);

        assertTrue(Arrays.equals(points, r1.getPoints().toArray(new RPoint2D[4])));
        assertFalse(Arrays.equals(points, r2.getPoints().toArray(new RPoint2D[4])));
    }

    @Test
    public void testGetSide() {
        RRectangle r1 = rectangle(3, 1, 2, 4, 8, 6, 9, 3);

        assertTrue(
                r1.getSide(true).equals(
                        new RLineSegment2D(new RPoint2D(2, 4), new RPoint2D(8, 6))
                )
                        ||
                        r1.getSide(true).equals(
                                new RLineSegment2D(new RPoint2D(3, 1), new RPoint2D(9, 3))
                        )
        );
        assertFalse(
                r1.getSide(true).equals(
                        new RLineSegment2D(new RPoint2D(2, 4), new RPoint2D(3, 1))
                )
                        ||
                        r1.getSide(true).equals(
                                new RLineSegment2D(new RPoint2D(8, 6), new RPoint2D(9, 3))
                        )
        );
    }

    @Test
    public void testHashCode() {
        RRectangle r1 = rectangle(3, 1, 2, 4, 8, 6, 9, 3);
        RRectangle r2 = rectangle(9, 3, 3, 1, 2, 4, 8, 6);
        RRectangle r3 = rectangle(0, 0, 0, 1, 1, 1, 1, 0);

        assertTrue(r1.hashCode() == r2.hashCode());
        assertFalse(r1.hashCode() == r3.hashCode());
    }

    @Test
    public void testIsSquare() {
        RRectangle r1 = rectangle(3, 1, 2, 4, 8, 6, 9, 3);
        RRectangle r2 = rectangle(9, 3, 3, 1, 2, 4, 8, 6);
        RRectangle r3 = rectangle(0, 0, 0, 1, 1, 1, 1, 0);

        assertFalse(r1.isSquare());
        assertFalse(r2.isSquare());
        assertTrue(r3.isSquare());
    }

    @Test
    public void testIsSide() {
        RRectangle r1 = rectangle(3, 1, 2, 4, 8, 6, 9, 3);
        RRectangle r2 = rectangle(0, 0, 0, 1, 1, 1, 1, 0);

        assertFalse(r1.isSide(new RLineSegment2D(RPoint2D.ORIGIN, new RPoint2D(1, 1))));
        assertTrue(r1.isSide(new RLineSegment2D(new RPoint2D(2, 4), new RPoint2D(3, 1))));
        assertTrue(r1.isSide(new RLineSegment2D(new RPoint2D(3, 1), new RPoint2D(2, 4))));

        assertFalse(r2.isSide(new RLineSegment2D(RPoint2D.ORIGIN, new RPoint2D(1, 1))));
        assertTrue(r2.isSide(new RLineSegment2D(new RPoint2D(1, 1), new RPoint2D(1, 0))));
        assertTrue(r2.isSide(new RLineSegment2D(new RPoint2D(1, 0), RPoint2D.ORIGIN)));
    }

    @Test
    public void testIsCongruentTo() {
        RRectangle r1 = rectangle(0,0 , 3,4 , 7,1 , 4,-3);
        RRectangle r2 = rectangle(0,0 , 0,5 , 5,5 , 5,0);
        RRectangle r3 = rectangle(0,0 , 0,6 , 5,6 , 5,0);

        RRectangle r4 = rectangle(1,1 , 1,3 , 4,3 , 4,1);
        RRectangle r5 = rectangle(2,-1 , 2,-4 , 4,-4 , 4,-1);

        RPolygon2D poly = new RPolygon2D(new RPoint2D(4,3), new RPoint2D(4,1), new RPoint2D(1,1), new RPoint2D(1,3));

        assertTrue(r1.isCongruentTo(r2));
        assertTrue(r2.isCongruentTo(r1));
        assertTrue(r4.isCongruentTo(r5));
        assertTrue(r5.isCongruentTo(r4));
        assertTrue(r4.isCongruentTo(poly));

        assertFalse(r1.isCongruentTo(r3));
        assertFalse(r3.isCongruentTo(r4));
    }
}
