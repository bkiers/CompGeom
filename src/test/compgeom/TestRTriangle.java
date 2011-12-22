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

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: May 14, 2010
 * </p>
 */
public class TestRTriangle {

    private RTriangle triangle(long x1, long y1, long x2, long y2, long x3, long y3) {
        return new RTriangle(new RPoint2D(x1,y1), new RPoint2D(x2,y2), new RPoint2D(x3,y3));
    }

    @Test
    public void testRTriangle() {
        triangle(0,0 , 10,1 , -100,-100);      
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRTriangle_IllegalArgumentException_1() {
        triangle(0,0 , 2,0 , 1,0);      
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRTriangle_IllegalArgumentException_2() {
        triangle(0,0 , 1,1 , -100,-100);      
    }

    @Test
    public void testContains() {
        RTriangle x = triangle(0,0 , 3,0 , 0,4);

        assertTrue(x.contains(new RPoint2D(1,1)));
        assertTrue(x.contains(new RPoint2D(0,4)));
        assertTrue(x.contains(new RPoint2D(2,0)));
        assertTrue(x.contains(new RPoint2D(1,2)));
        assertTrue(x.contains(new RPoint2D(2,1)));

        assertFalse(x.contains(new RPoint2D(1,3)));
        assertFalse(x.contains(new RPoint2D(2,2)));
        assertFalse(x.contains(new RPoint2D(0,5)));
        assertFalse(x.contains(new RPoint2D(1,-3)));
    }

    @Test
    public void testEquals() {
        RTriangle a = triangle(0,0 , 3,0 , 0,4);
        RTriangle b = triangle(3,0 , 0,4 , 0,0);
        RTriangle c = triangle(0,0 , 3,1 , 0,4);

        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));

        assertFalse(a.equals(c));
        assertFalse(c.equals(a));
    }

    @Test
    public void testGetLongestSide() {
        RTriangle a = triangle(0,0 , 3,0 , 0,4);

        assertTrue(a.getLongestSide().lengthSquared().equals(new Rational(25)));
    }

    @Test
    public void testGetPoints() {
        RTriangle a = triangle(0,0 , 3,0 , 0,4);

        RPoint2D[] pts = a.getPoints().toArray(new RPoint2D[3]);

        assertTrue(pts[0].equals(new RPoint2D(0,0)));
        assertTrue(pts[1].equals(new RPoint2D(3,0)));
        assertTrue(pts[2].equals(new RPoint2D(0,4)));
    }

    @Test
    public void testHashCode() {
        RTriangle a = triangle(0,0 , 3,0 , 0,4);
        RTriangle b = triangle(3,0 , 0,4 , 0,0);

        assertTrue(a.hashCode() == b.hashCode());
    }

    @Test
    public void testIsRightAngled() {
        RTriangle a = triangle(0,0,3,1,1,2);
        RTriangle b = triangle(0,0,3,0,0,4); // horizontal and vertical lines
        RTriangle c = triangle(0,0,2,0,1,3);

        assertTrue(a.isRightAngled());
        assertTrue(b.isRightAngled());
        
        assertFalse(c.isRightAngled());
    }

    @Test
    public void testIsAcuteAngled() {
        RTriangle a = triangle(0,0,3,1,1,2);
        RTriangle b = triangle(0,0,3,0,0,4); // horizontal and vertical lines

        assertFalse(a.isAcuteAngled());
        assertFalse(b.isAcuteAngled());
        assertFalse(triangle(0,0, 9999999999999999L,1, -1,9999999999999999L).isAcuteAngled());

        assertTrue(triangle(0,0, 3,0, 2,3).isAcuteAngled());
        assertTrue(triangle(0,0, 9999999999999999L,1, 0,9999999999999999L).isAcuteAngled());
        assertTrue(triangle(0,0, 9999999999999999L,2, -1,9999999999999999L).isAcuteAngled());
    }

    @Test
    public void testIsEquilateral() {
        assertFalse(triangle(0,0 , 21,0 , 0,20).isEquilateral());
        assertFalse(triangle(0,0 , 201,0 , 1,20).isEquilateral());
        assertFalse(triangle(0,0 , 21,0 , 2,20).isEquilateral());
        assertFalse(triangle(0,0 , 2000000000001L,0 , -10,20).isEquilateral());
    }

    @Test
    public void testIsIsosceles() {
        assertTrue(triangle(0,0 , 3,0 , 0,3).isIsosceles());
        assertTrue(triangle(0,0 , 2,0 , 1,300000000000000000L).isIsosceles());

        assertFalse(triangle(0,0 , 21,0 , 0,20).isIsosceles());
        assertFalse(triangle(0,0 , 201,0 , 1,20).isIsosceles());
        assertFalse(triangle(0,0 , 21,0 , 2,20).isIsosceles());
        assertFalse(triangle(0,0 , 2000000000001L,0 , -10,20).isIsosceles());
    }

    @Test
    public void testIsObtuseAngled() {
        RTriangle a = triangle(0,0,3,1,1,2);
        RTriangle b = triangle(0,0,3,0,0,4); // horizontal and vertical lines

        assertTrue(a.isObtuseAngled());
        assertTrue(b.isObtuseAngled());
        assertTrue(triangle(0,0, 9999999999999999L,1, -1,9999999999999999L).isObtuseAngled());

        assertFalse(triangle(0,0, 3,0, 2,3).isObtuseAngled());
        assertFalse(triangle(0,0, 9999999999999999L,1, 0,9999999999999999L).isObtuseAngled());
        assertFalse(triangle(0,0, 9999999999999999L,2, -1,9999999999999999L).isObtuseAngled());
    }

    @Test
    public void testIsScalene() {
        assertFalse(triangle(0,0 , 2,0 , 1,-100).isScalene());
        assertFalse(triangle(0,0 , 20,0 , 0,20).isScalene());

        assertTrue(triangle(0,0 , 21,0 , 0,20).isScalene());
        assertTrue(triangle(0,0 , 201,0 , 1,20).isScalene());
        assertTrue(triangle(0,0 , 21,0 , 2,20).isScalene());
        assertTrue(triangle(0,0 , 2000000000001L,0 , -10,20).isScalene());
    }

    @Test
    public void testIsOblique() {
        RTriangle a = triangle(0,0,3,1,1,2);
        RTriangle b = triangle(0,0,3,0,0,4); // horizontal and vertical lines
        RTriangle c = triangle(0,0,2,0,1,3);

        assertFalse(a.isOblique());
        assertFalse(b.isOblique());

        assertTrue(c.isOblique());
    }

    @Test
    public void testMaxX() {
        RTriangle a = triangle(-3,-2 , 3,1 , -1,4);
        assertTrue(a.maxX().equals(new Rational(3)));
    }

    @Test
    public void testMaxY() {
        RTriangle a = triangle(-3,-2 , 3,1 , -1,4);
        assertTrue(a.maxY().equals(new Rational(4)));
    }

    @Test
    public void testMinX() {
        RTriangle a = triangle(-3,-2 , 3,1 , -1,4);
        assertTrue(a.minX().equals(new Rational(-3)));
    }

    @Test
    public void testMinY() {
        RTriangle a = triangle(-3,-2 , 3,1 , -1,4);
        assertTrue(a.minY().equals(new Rational(-2)));
    }

    @Test
    public void testXYAlignedRectangle() {
        RTriangle a = triangle(-3,-2 , 3,1 , -1,4);
        assertTrue(a.xyAlignedRectangle().equals(new RRectangle(
                new RPoint2D(-3,-2),
                new RPoint2D(-3,4),
                new RPoint2D(3,4),
                new RPoint2D(3,-2)
        )));
    }

    @Test
    public void testIsCongruentTo() {
        RTriangle a = triangle(0,0 , 3,0 , 0,4);
        RTriangle b = triangle(0,0 , -3,4 , 0,4);
        RTriangle x = triangle(0,0 , -4,4 , 0,4);

        RPolygon2D polyA = new RPolygon2D(new RPoint2D(0,0), new RPoint2D(0,4), new RPoint2D(3,0));
        RPolygon2D polyB = new RPolygon2D(new RPoint2D(3,0), new RPoint2D(6,0), new RPoint2D(6,4));
        RPolygon2D polyX = new RPolygon2D(new RPoint2D(1,1), new RPoint2D(3,5), new RPoint2D(8,9));

        assertTrue(a.isCongruentTo(b));
        assertTrue(b.isCongruentTo(a));

        assertTrue(a.isCongruentTo(polyA));
        assertTrue(b.isCongruentTo(polyB));

        assertFalse(a.isCongruentTo(x));
        assertFalse(a.isCongruentTo(polyX));
    }
}
