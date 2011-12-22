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
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public class TestRLine2D {

    // a helper method to create RLine2D objects

    private RLine2D line(long x1, long y1, long x2, long y2) {
        return new RLine2D(new RPoint2D(x1, y1), new RPoint2D(x2, y2));
    }

    private boolean approximateEquals(double a, double b) {
        double diff = Math.abs(a - b);
        return diff < 0.0000000000001;
    }

    // valid constructors

    @Test
    public void testRLine2DString() {
        RLine2D a = new RLine2D("f(x) -> 3/1x + 1/2");
        RLine2D b = new RLine2D("y = 3.0*x + -1/-2");
        RLine2D c = new RLine2D("f(x) -> 3*x");
        RLine2D d = new RLine2D("F(X) -> 0*x + 0");
        RLine2D e = new RLine2D("f(x) -> 6/2");
        RLine2D f = new RLine2D("f(X) -> 0x + 1/2");
        RLine2D g = new RLine2D("Y = 1/2");
        RLine2D h = new RLine2D("x = -0.01");
        new RLine2D("y = x");

        assertTrue(a.equals(b));
        assertTrue(a.slope.equals(new Rational(3)));
        assertTrue(a.slope.equals(c.slope));
        assertTrue(f.equals(g));
        assertTrue(h.equals(RLine2D.vertical(new Rational("-0.01"))));
        assertTrue(e.constant.equals(new Rational("3")));
        assertTrue(d.slope.equals(Rational.ZERO));
        assertTrue(d.constant.equals(Rational.ZERO));
    }

    @Test
    public void testRLine2DRationalRationalValid() {
        new RLine2D(new Rational("2"), new Rational("-11111"));
        new RLine2D(new Rational("2.0"), new Rational("-11111"));
        new RLine2D(new Rational("123"), new Rational("0"));
        new RLine2D(new Rational("0"), new Rational("0.0"));
    }

    @Test
    public void testRLine2DRationalRPoint2DValid() {
        new RLine2D(new Rational("2"), new RPoint2D(0, 0));
        new RLine2D(new Rational("2.0"), new RPoint2D(0, 0));
        new RLine2D(new Rational("123"), new RPoint2D(0, 0));
        new RLine2D(new Rational("0"), new RPoint2D(0, 0));
    }

    @Test
    public void testRLine2DRPoint2DRPoint2DValid() {
        new RLine2D(new RPoint2D(4, 1), new RPoint2D(0, 0));
        new RLine2D(new RPoint2D(324234, 0), new RPoint2D(0, 0));
        new RLine2D(new RPoint2D(new Rational("123"), new Rational("9999999999999999999999999999999999")), new RPoint2D(0, 0));
    }

    // invalid constructors

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DStringInvalid1() {
        new RLine2D("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DStringInvalid2() {
        new RLine2D("f(z) -> x");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DStringInvalid3() {
        new RLine2D("f(x) -> 0.1/2 * x"); // invalid rational: 0.1/2
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DStringInvalid4() {
        new RLine2D("f(x) = x"); // '=' should be '->'
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DStringInvalid5() {
        new RLine2D("y -> x"); // '->' should be '='
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DStringInvalid6() {
        new RLine2D("x = x"); // second 'x' should be a number
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DRationalRationalInvalid1() {
        new RLine2D(Rational.NaN, new Rational("-11111"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DRationalRationalInvalid2() {
        new RLine2D(new Rational("2"), Rational.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DRationalRPoint2DInvalid() {
        new RLine2D(Rational.NaN, new RPoint2D(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRLine2DRPoint2DRPoint2DInvalid() {
        new RLine2D(new RPoint2D(0, 0), new RPoint2D(0, 0));
    }

    // methods

    @Test
    public void testDegrees() {
        RLine2D a = line(1, 1, 5, 5);
        RLine2D b = line(0, 1, 4, 5);
        RLine2D c = line(0, 1, 0, 2); // vertical
        RLine2D d = line(1, 0, 2, 0); // horizontal
        RLine2D e = line(0, 0, -1, 1); // negative slope
        RLine2D f = line(0, 0, -999999999999999999L, 1); // negative slope, close to 180 degrees

        assertTrue(a.angle() == 45.0);
        assertTrue(b.angle() == 45.0);
        assertTrue(c.angle() == 90.0);
        assertTrue(d.angle() == 0.0);
        assertTrue(e.angle() == 135.0);
        assertTrue(approximateEquals(180.0, f.angle() + 0.00000000000000000001));

        assertTrue(approximateEquals(a.angle(false), Math.PI / 4.0));
        assertTrue(approximateEquals(b.angle(false), Math.PI / 4.0));
        assertTrue(approximateEquals(c.angle(false), Math.PI / 2.0));
        assertTrue(d.angle(false) == 0.0);
        assertTrue(approximateEquals(e.angle(false), (Math.PI * 3.0) / 4.0));
        assertTrue(approximateEquals(f.angle(false) + 0.00000000000000000001, Math.PI));
    }

    @Test
    public void testEquals() {
        RLine2D a = line(1, 1, 5, 5);
        RLine2D b = line(-1, -1, -100, -100);
        RLine2D c = line(0, 0, 99999999, 99999998);
        RLine2D d = line(0, 0, 0, 10); // vertical line through x=0
        RLine2D e = line(0, 5, 0, 15); // vertical line through x=0
        RLine2D f = line(0, 0, 10, 0); // horizontal line through y=0
        RLine2D g = line(-5, 0, 15, 0); // horizontal line through y=0
        RLine2D h = line(1, 5, 1, 15); // vertical line through x=1
        RLine2D i = line(0, 1, 10, 1); // horizontal line through y=1

        // true
        assertTrue(a.equals(b));
        assertTrue(d.equals(e));
        assertTrue(f.equals(g));

        // false
        assertFalse(a.equals(c));
        assertFalse(d.equals(f));
        assertFalse(a.equals(g));
        assertFalse(d.equals(h));
        assertFalse(f.equals(i));
    }

    @Test
    public void testHashCode() {
        RLine2D a = line(1, 1, 5, 5);
        RLine2D b = line(-1, -1, -100, -100);
        RLine2D c = line(0, 0, 99999999, 99999998);
        RLine2D d = line(0, 0, 0, 10); // vertical line through x=0
        RLine2D e = line(0, 5, 0, 15); // vertical line through x=0
        RLine2D f = line(0, 0, 10, 0); // horizontal line through y=0
        RLine2D g = line(-5, 0, 15, 0); // horizontal line through y=0
        RLine2D h = line(1, 5, 1, 15); // vertical line through x=1
        RLine2D i = line(0, 1, 10, 1); // horizontal line through y=1

        // true
        assertTrue(a.hashCode() == b.hashCode());
        assertTrue(d.hashCode() == e.hashCode());
        assertTrue(f.hashCode() == g.hashCode());

        // false
        assertFalse(a.hashCode() == c.hashCode());
        assertFalse(d.hashCode() == f.hashCode());
        assertFalse(a.hashCode() == g.hashCode());
        assertFalse(d.hashCode() == h.hashCode());
        assertFalse(f.hashCode() == i.hashCode());
    }

    @Test
    public void testIntersectionLine() {
        RLine2D a = line(1, 1, 5, 5);
        RLine2D b = line(-1, -1, -100, -100);
        RLine2D c = line(1, 0, 11, 11);
        RLine2D d = line(0, 0, 0, 10); // vertical line through x=0
        RLine2D h = line(1, 5, 1, 15); // vertical line through x=1
        RLine2D i = line(0, 1, 10, 1); // horizontal line through y=1

        assertTrue(a.intersection(b) == null);
        assertTrue(d.intersection(a).equals(new RPoint2D(0, 0)));
        assertTrue(h.intersection(i).equals(new RPoint2D(1, 1)));
        assertTrue(a.intersection(c).equals(new RPoint2D(11, 11)));
    }

    @Test
    public void testIntersectsLine() {
        RLine2D a = line(1, 1, 5, 5);
        RLine2D b = line(-1, -1, -100, -100);
        RLine2D c = line(1, 0, 11, 11);
        RLine2D d = line(0, 0, 0, 10); // vertical line through x=0
        RLine2D h = line(1, 5, 1, 15); // vertical line through x=1
        RLine2D i = line(0, 1, 10, 1); // horizontal line through y=1

        assertFalse(a.intersects(b));
        assertTrue(d.intersects(a));
        assertTrue(h.intersects(i));
        assertTrue(a.intersects(c));
    }

    @Test
    public void testIntersectionSegment() {
        RLine2D line = line(0, 0, 5, 5);
        RLineSegment2D s1 = new RLineSegment2D(new RPoint2D(2, 0), new RPoint2D(0, 2));
        RLineSegment2D s2 = new RLineSegment2D(new RPoint2D(0, 0), new RPoint2D(-1, 0));
        RLineSegment2D s3 = new RLineSegment2D(new RPoint2D(1, 0), new RPoint2D(2, 0));

        assertTrue(line.intersection(s1).equals(new RPoint2D(1, 1)));
        assertTrue(line.intersection(s2).equals(new RPoint2D(0, 0)));
        assertTrue(line.intersection(s3) == null);
    }

    @Test
    public void testIntersectsSegment() {
        RLine2D line = line(0, 0, 5, 5);
        RLineSegment2D s1 = new RLineSegment2D(new RPoint2D(2, 0), new RPoint2D(0, 2));
        RLineSegment2D s2 = new RLineSegment2D(new RPoint2D(0, 0), new RPoint2D(-1, 0));
        RLineSegment2D s3 = new RLineSegment2D(new RPoint2D(1, 0), new RPoint2D(2, 0));

        assertTrue(line.intersects(s1));
        assertTrue(line.intersects(s2));
        assertFalse(line.intersects(s3));
    }

    @Test
    public void testIsHorizontal() {
        RLine2D a = line(2, 1, 3, 1);
        RLine2D b = line(1, 1, 2, 2);
        RLine2D c = line(2, 1, 2, 2);

        assertTrue(a.isHorizontal());
        assertFalse(b.isHorizontal());
        assertFalse(c.isHorizontal());
    }

    @Test
    public void testIsParallelTo() {
        RLine2D a = line(1, 1, 5, 5);
        RLine2D b = line(1, 0, 100, 99);
        RLine2D c = line(0, 0, 99999999, 99999998);
        RLine2D d = line(0, 0, 0, 10); // vertical line through x=0
        RLine2D g = line(-5, 0, 15, 0); // horizontal line through y=0
        RLine2D h = line(1, 5, 1, 15); // vertical line through x=1
        RLine2D i = line(0, 1, 10, 1); // horizontal line through y=1

        assertTrue(a.isParallelTo(b));
        assertTrue(d.isParallelTo(h));
        assertTrue(g.isParallelTo(i));

        assertFalse(a.isParallelTo(c));
    }

    @Test
    public void testIsPerpendicularTo() {
        RLine2D a = line(0, 0, 5, 5);
        RLine2D b = line(4, 1, 5, 0);
        RLine2D c = line(4, 2, 5, 0);
        RLine2D d = line(0, 0, 1, 0); // horizontal
        RLine2D e = line(0, 0, 0, 1); // vertical

        assertTrue(a.isPerpendicularTo(b));
        assertTrue(b.isPerpendicularTo(a));
        assertFalse(a.isPerpendicularTo(c));
        assertTrue(d.isPerpendicularTo(e));
        assertTrue(e.isPerpendicularTo(d));
    }

    @Test
    public void testIsVertical() {
        RLine2D a = line(2, 1, 3, 1);
        RLine2D b = line(1, 1, 2, 2);
        RLine2D c = line(2, 1, 2, 2);

        assertFalse(a.isVertical());
        assertFalse(b.isVertical());
        assertTrue(c.isVertical());
    }

    @Test
    public void testLiesOnLine() {
        RLine2D a = line(0, 0, 5, 5);
        RLine2D b = line(4, 1, 5, 0);
        RLine2D c = line(-5, 0, 15, 0); // horizontal line through y=0

        assertTrue(a.contains(new RPoint2D(-1, -1)));
        assertTrue(a.contains(new RPoint2D(1000, 1000)));
        assertTrue(b.contains(new RPoint2D(new Rational("2.5"), new Rational("2.5"))));
        assertTrue(c.contains(new RPoint2D(new Rational("2.5123123132"), new Rational("0"))));

        assertFalse(a.contains(new RPoint2D(1000, 999)));
    }

    @Test
    public void testPerpendicularLine() {
        RLine2D a = line(0, 0, 5, 5); // f(x) -> x
        RLine2D b = line(0, 0, 5, 0); // horizontal
        RLine2D c = line(0, 0, 0, 5); // vertical
        RLine2D d = line(0, 0, 1, 3);

        assertTrue(a.perpendicularLine(new RPoint2D(1, 3)).equals(line(0, 4, 4, 0)));
        assertTrue(b.perpendicularLine(new RPoint2D(10, 3)).equals(line(10, 30, 10, 31)));
        assertTrue(c.perpendicularLine(new RPoint2D(6, 6)).equals(line(10, 6, 11, 6)));
        assertTrue(d.perpendicularLine(new RPoint2D(4, 2)).equals(line(1,3,-2,4)));
    }

    @Test
    public void testTangent() {
        RLine2D a = line(1, 1, 5, 5);
        RLine2D b = line(-1, -1, -100, -100);
        RLine2D c = line(0, 0, 99999999, 99999998);
        RLine2D d = line(0, 0, 0, 10); // vertical line through x=0
        RLine2D f = line(0, 0, 10, 0); // horizontal line through y=0
        RLine2D h = line(1, 5, 1, 15); // vertical line through x=1
        RLine2D i = line(0, 1, 10, 1); // horizontal line through y=1

        assertTrue(a.tangent(b).equals(new Rational("0")));
        assertTrue(a.tangent(c).equals(new Rational("1/199999997")));
        assertTrue(a.tangent(a).equals(new Rational("0")));
        assertTrue(d.tangent(f).equals(new Rational("-1")));
        assertTrue(d.tangent(h).equals(new Rational("0")));
        assertTrue(f.tangent(i).equals(new Rational("0")));
    }

    @Test
    public void testXIntercept() {
        RLine2D a = line(0, 0, 5, 5);
        RLine2D b = line(4, 1, 5, 0);
        RLine2D c = line(-5, 0, 15, 0); // horizontal line through y=0
        RLine2D d = line(1, 5, 1, 15); // vertical line through x=1

        assertTrue(a.xIntercept().equals(new Rational(0)));
        assertTrue(b.xIntercept().equals(new Rational(5)));
        assertTrue(c.xIntercept() == null);
        assertTrue(d.xIntercept().equals(new Rational(1)));
    }

    @Test
    public void testYIntercept() {
        RLine2D a = line(0, 0, 5, 5);
        RLine2D b = line(4, 1, 5, 0);
        RLine2D c = line(-5, 0, 15, 0); // horizontal line through y=0
        RLine2D d = line(1, 5, 1, 15); // vertical line through x=1

        assertTrue(a.yIntercept().equals(new Rational(0)));
        assertTrue(b.yIntercept().equals(new Rational(5)));
        assertTrue(c.yIntercept().equals(new Rational(0)));
        assertTrue(d.yIntercept() == null);
    }
    
    @Test
    public void testIsAxisAligned() {
        RLine2D a = new RLine2D("y = 800000000000000000000000000000");
        RLine2D b = new RLine2D("x = -11111111111111111111111111111/9");
        RLine2D c = new RLine2D("f(x) -> 0.0000000000000000000000000000000000000000000001*x");

        assertTrue(a.isAxisAligned());
        assertTrue(b.isAxisAligned());
        assertFalse(c.isAxisAligned());
    }
}
