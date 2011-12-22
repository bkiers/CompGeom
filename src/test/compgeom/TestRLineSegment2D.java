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

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 13, 2010
 * </p>
 */
public class TestRLineSegment2D {

    private RLineSegment2D seg(int x1, int y1, int x2, int y2) {
        return new RLineSegment2D(new RPoint2D(x1, y1), new RPoint2D(x2, y2));
    }
    // constructor

    @Test
    public void testRLineSegment2D() {
        new RLineSegment2D(new RPoint2D(1, 1), new RPoint2D(1, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLineSegment2D_IllegalArgumentException() {
        new RLineSegment2D(new RPoint2D(1, 1), new RPoint2D(1, 1));
    }

    // methods

    @Test
    public void testCenter() {
        RLineSegment2D a = seg(-2, -2, 2, 2);
        RLineSegment2D b = seg(0, 0, 4, 0);
        RLineSegment2D c = seg(0, 0, 0, 4);

        assertTrue(a.center().equals(RPoint2D.ORIGIN));
        assertTrue(b.center().equals(new RPoint2D(2, 0)));
        assertTrue(c.center().equals(new RPoint2D(0, 2)));
    }

    @Test
    public void testContains() {
        RLineSegment2D a = seg(1, 1, 1, 5);
        RLineSegment2D b = seg(1, 5, 5, 5);
        RLineSegment2D c = seg(3, 4, 5, 6);
        RLineSegment2D d = seg(-2, 1, 1, 2);

        assertTrue(a.contains(new RPoint2D(1, 1)));
        assertTrue(a.contains(new RPoint2D(1, 4)));
        assertTrue(b.contains(new RPoint2D(2, 5)));
        assertTrue(c.contains(new RPoint2D(4, 5)));
        assertTrue(d.contains(new RPoint2D(new Rational("0"), new Rational("5/3"))));
    }

    @Test
    public void testIntersectionSegment() {
        RLineSegment2D a = seg(1, 1, 1, 5);
        RLineSegment2D b = seg(1, 5, 5, 5);
        RLineSegment2D c = seg(3, 4, 5, 6);
        RLineSegment2D d = seg(-2, 1, 1, 2);

        RLineSegment2D e = seg(1, 1, 3, 3);
        RLineSegment2D f = seg(2, 2, 4, 4);
        RLineSegment2D g = seg(2, 2, 3, 2);
        RLineSegment2D h = seg(3, 3, 5, 5);
        RLineSegment2D i = seg(3, 3, 8, 8);

        assertTrue(a.intersection(b).equals(new RPoint2D(1, 5)));
        assertTrue(b.intersection(c).equals(new RPoint2D(4, 5)));
        assertTrue(a.intersection(d).equals(new RPoint2D(1, 2)));
        assertTrue(a.intersection(c) == null);

        assertTrue(e.intersection(f) == null);
        assertTrue(g.intersection(e).equals(new RPoint2D(2, 2)));
        assertTrue(g.intersection(f).equals(new RPoint2D(2, 2)));
        assertTrue(e.intersection(h).equals(new RPoint2D(3, 3)));

        assertTrue(h.intersection(i) == null);
    }

    @Test
    public void testIntersectionLine() {
        RLineSegment2D a = seg(1, 1, 5, 1);
        RLine2D l1 = new RLine2D(new RPoint2D(1, 0), new RPoint2D(2, 1));
        RLine2D l2 = new RLine2D(new RPoint2D(5, 0), new RPoint2D(5, 1));
        RLine2D l3 = new RLine2D(new RPoint2D(6, 0), new RPoint2D(6, 1));

        assertTrue(a.intersection(l1).equals(new RPoint2D(2, 1)));
        assertTrue(a.intersection(l2).equals(new RPoint2D(5, 1)));
        assertTrue(a.intersection(l3) == null);
    }

    @Test
    public void testIntersectsSegment() {
        RLineSegment2D a = seg(1, 1, 1, 5);
        RLineSegment2D b = seg(1, 5, 5, 5);
        RLineSegment2D c = seg(3, 4, 5, 6);
        RLineSegment2D d = seg(-2, 1, 1, 2);

        assertTrue(a.intersects(b));
        assertTrue(b.intersects(c));
        assertTrue(a.intersects(d));

        assertFalse(a.intersects(c));
    }

    @Test
    public void testIntersectsLine() {
        RLineSegment2D a = seg(1, 1, 5, 1);
        RLine2D l1 = new RLine2D(new RPoint2D(1, 0), new RPoint2D(2, 1));
        RLine2D l2 = new RLine2D(new RPoint2D(5, 0), new RPoint2D(5, 1));
        RLine2D l3 = new RLine2D(new RPoint2D(6, 0), new RPoint2D(6, 1));

        assertTrue(a.intersects(l1));
        assertTrue(a.intersects(l2));
        assertFalse(a.intersects(l3));
    }

    @Test
    public void testEquals() {
        RLineSegment2D a = seg(1, 1, 1, 5);
        RLineSegment2D b = seg(1, 5, 1, 1);
        RLineSegment2D c = seg(3, 4, 5, 6);
        RLineSegment2D d = seg(5, 6, 3, 4);

        assertTrue(a.equals(b));
        assertTrue(c.equals(d));

        assertFalse(a.equals(c));
    }

    @Test
    public void testHashCode() {
        RLineSegment2D a = seg(1, 1, 1, 5);
        RLineSegment2D b = seg(1, 5, 1, 1);
        RLineSegment2D c = seg(3, 4, 5, 6);
        RLineSegment2D d = seg(5, 6, 3, 4);

        assertTrue(a.hashCode() == b.hashCode());
        assertTrue(c.hashCode() == d.hashCode());

        assertFalse(a.hashCode() == c.hashCode());
    }

    @Test
    public void testLength() {
        RLineSegment2D a = seg(0, 0, 3, 4);
        RLineSegment2D b = seg(1, 1, 1, 2);
        RLineSegment2D c = seg(1, 1, 2, 1);

        assertTrue(a.length() == 5.0);
        assertTrue(b.length() == 1.0);
        assertTrue(c.length() == 1.0);
    }

    @Test
    public void testLengthXY() {
        RLineSegment2D a = seg(0, 0, 3, 4);
        RLineSegment2D b = seg(1, 1, 1, 2);
        RLineSegment2D c = seg(1, 1, 2, 1);

        assertTrue(a.lengthXY().equals(new Rational(7)));
        assertTrue(b.lengthXY().equals(new Rational(1)));
        assertTrue(c.lengthXY().equals(new Rational(1)));
    }

    @Test
    public void testLengthSquared() {
        RLineSegment2D a = seg(0, 0, 3, 4);
        RLineSegment2D b = seg(1, 1, 1, 2);
        RLineSegment2D c = seg(1, 1, 2, 1);
        RLineSegment2D d = seg(-1, -1, 4, 4);

        assertTrue(a.lengthSquared().equals(new Rational(25)));
        assertTrue(b.lengthSquared().equals(new Rational(1)));
        assertTrue(c.lengthSquared().equals(new Rational(1)));
        assertTrue(d.lengthSquared().equals(new Rational(5 * 5 + 5 * 5)));
    }

    @Test
    public void testIsEqualLength() {
        RLineSegment2D a = seg(0, 0, 3, 4);
        RLineSegment2D b = seg(1, 1, 1, 2);
        RLineSegment2D c = seg(1, 1, 2, 1);

        assertTrue(b.isEqualLength(c));
        assertTrue(c.isEqualLength(b));
        assertTrue(c.isEqualLength(c));
        assertFalse(a.isEqualLength(c));
    }

    @Test
    public void testIsLongerThan() {
        RLineSegment2D a = seg(0, 0, 3, 4);
        RLineSegment2D b = seg(1, 1, 1, 2);
        RLineSegment2D c = seg(1, 1, 2, 1);

        assertFalse(b.isLongerThan(c));
        assertFalse(c.isLongerThan(b));
        assertFalse(c.isLongerThan(c));
        assertTrue(a.isLongerThan(c));
    }

    @Test
    public void testIsShorterThan() {
        RLineSegment2D a = seg(0, 0, 3, 4);
        RLineSegment2D b = seg(1, 1, 1, 2);
        RLineSegment2D c = seg(1, 1, 2, 1);

        assertFalse(b.isShorterThan(c));
        assertFalse(c.isShorterThan(b));
        assertFalse(c.isShorterThan(c));
        assertFalse(a.isShorterThan(c));
        assertTrue(b.isShorterThan(a));
        assertTrue(c.isShorterThan(a));
    }

    @Test
    public void testHasEnding() {
        RLineSegment2D a = seg(0, 0, 3, 4);

        assertTrue(a.hasEnding(new RPoint2D(0,0)));
        assertTrue(a.hasEnding(new RPoint2D(3,4)));
        assertFalse(a.hasEnding(new RPoint2D(1,0)));
    }
}
