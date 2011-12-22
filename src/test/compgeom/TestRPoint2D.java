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
public class TestRPoint2D {

    // constructor

    @Test
    public void testRPoint2D() {
        new RPoint2D();
        new RPoint2D(1, 2);
        new RPoint2D(0, -2);
        new RPoint2D(new Rational(1), new Rational(5));
        new RPoint2D(new Rational("1"), new Rational("5"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRPoint2D_IllegalArgumentException_1() {
        new RPoint2D(Rational.ONE, Rational.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRPoint2D_IllegalArgumentException_2() {
        new RPoint2D(Rational.NaN, Rational.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRPoint2D_IllegalArgumentException_3() {
        new RPoint2D(Rational.NaN, Rational.NaN);
    }

    @Test(expected = ArithmeticException.class)
    public void testRPoint2D_ArithmeticException() {
        new RPoint2D(new Rational("1/1"), new Rational("1/0"));
    }

    @Test(expected = NumberFormatException.class)
    public void testRPoint2D_NumberFormatException() {
        new RPoint2D(new Rational("1e10"), new Rational("1"));
    }

    // methods

    @Test
    public void testDistance() {
        RPoint2D a = new RPoint2D(0, 0);
        RPoint2D b = new RPoint2D(3, 0);
        RPoint2D c = new RPoint2D(3, 4);

        assertTrue(a.distance(a) == 0.0);
        assertTrue(a.distance(b) == b.distance(a));
        assertTrue(a.distance(b) == 3.0);
        assertTrue(b.distance(c) == 4.0);
        assertTrue(c.distance(a) == 5.0);
    }

    @Test
    public void testDistanceSquared() {
        RPoint2D a = new RPoint2D(0, 0);
        RPoint2D b = new RPoint2D(3, 0);
        RPoint2D c = new RPoint2D(3, 4);

        assertTrue(a.distanceSquared(a).equals(Rational.ZERO));
        assertTrue(a.distanceSquared(b).equals(b.distanceSquared(a)));
        assertTrue(a.distanceSquared(b).equals(new Rational(9)));
        assertTrue(b.distanceSquared(c).equals(new Rational(16)));
        assertTrue(c.distanceSquared(a).equals(new Rational(25)));
    }

    @Test
    public void testDistanceXY() {
        RPoint2D a = new RPoint2D(0, 0);
        RPoint2D b = new RPoint2D(3, 0);
        RPoint2D c = new RPoint2D(3, 4);

        assertTrue(a.distanceXY(a).equals(Rational.ZERO));
        assertTrue(a.distanceXY(b).equals(b.distanceXY(a)));
        assertTrue(a.distanceXY(b).equals(new Rational("3.0")));
        assertTrue(b.distanceXY(c).equals(new Rational("4.0")));
        assertTrue(c.distanceXY(a).equals(new Rational("7.0")));
    }

    @Test
    public void testEquals() {
        assertTrue(new RPoint2D().equals(new RPoint2D(0, 0)));
        assertTrue(new RPoint2D(0, 0).equals(new RPoint2D(0, 0)));
        assertFalse(new RPoint2D(0, 3).equals(new RPoint2D(0, 0)));
    }

    @Test
    public void testHashCode() {
        assertTrue(new RPoint2D().hashCode() == new RPoint2D(0, 0).hashCode());
        assertTrue(new RPoint2D(0, 0).hashCode() == new RPoint2D(0, 0).hashCode());
        assertFalse(new RPoint2D(0, 3).hashCode() == new RPoint2D(0, 0).hashCode());
    }

    @Test
    public void testIsAbove() {
        /*
                d   c

                a   b
        */
        List<RPoint2D> points = CGUtil.createRPoint2DList("0 0   1 0   1 1   0 1");
        RPoint2D a = points.get(0);
        RPoint2D b = points.get(1);
        RPoint2D c = points.get(2);
        RPoint2D d = points.get(3);

        assertFalse(a.isAbove(b));
        assertFalse(d.isAbove(c));
        assertFalse(a.isAbove(c));

        assertTrue(d.isAbove(a));
        assertTrue(d.isAbove(b));
        assertTrue(c.isAbove(a));
        assertTrue(c.isAbove(b));
    }

    @Test
    public void testIsBelow() {
        /*
                d   c

                a   b
        */
        List<RPoint2D> points = CGUtil.createRPoint2DList("0 0   1 0   1 1   0 1");
        RPoint2D a = points.get(0);
        RPoint2D b = points.get(1);
        RPoint2D c = points.get(2);
        RPoint2D d = points.get(3);

        assertFalse(a.isBelow(b));
        assertFalse(d.isBelow(c));
        assertFalse(c.isBelow(a));

        assertTrue(a.isBelow(d));
        assertTrue(a.isBelow(c));
        assertTrue(b.isBelow(c));
        assertTrue(b.isBelow(d));
    }

    @Test
    public void testIsLeftOf_Point() {
        /*
                d   c

                a   b
        */
        List<RPoint2D> points = CGUtil.createRPoint2DList("0 0   1 0   1 1   0 1");
        RPoint2D a = points.get(0);
        RPoint2D b = points.get(1);
        RPoint2D c = points.get(2);
        RPoint2D d = points.get(3);

        assertFalse(b.isLeftOf(a));
        assertFalse(c.isLeftOf(d));
        assertFalse(a.isLeftOf(d));

        assertTrue(a.isLeftOf(c));
        assertTrue(a.isLeftOf(b));
        assertTrue(d.isLeftOf(c));
        assertTrue(d.isLeftOf(b));
    }
    
    @Test
    public void testIsLeftOf_Line() {
        RLine2D line = new RLine2D("f(x) -> 2*x - 3");
        assertTrue(new RPoint2D(2,2).isLeftOf(line));
        assertTrue(new RPoint2D(-1,-4).isLeftOf(line));
        assertTrue(new RPoint2D(0,0).isLeftOf(line));
        assertTrue(new RPoint2D(-5,5).isLeftOf(line));
        assertFalse(new RPoint2D(2,0).isLeftOf(line));
        assertFalse(new RPoint2D(-1,-5).isLeftOf(line)); // point on the line
        assertFalse(new RPoint2D(0,-4).isLeftOf(line));
        assertFalse(new RPoint2D(3,2).isLeftOf(line));

        RLine2D lineH = new RLine2D("y = -5");
        assertTrue(new RPoint2D(2,0).isLeftOf(lineH));
        assertTrue(new RPoint2D(2,-4).isLeftOf(lineH));
        assertFalse(new RPoint2D(2,-5).isLeftOf(lineH)); // point on the line
        assertFalse(new RPoint2D(2,-7).isLeftOf(lineH));

        RLine2D lineV = new RLine2D("x = -5");
        assertTrue(new RPoint2D(-6,0).isLeftOf(lineV));
        assertTrue(new RPoint2D(-9,-4).isLeftOf(lineV));
        assertFalse(new RPoint2D(-5,8).isLeftOf(lineV)); // point on the line
        assertFalse(new RPoint2D(7,8).isLeftOf(lineV));
    }

    @Test
    public void testIsRightOf_Point() {
        /*
                d   c

                a   b
        */
        List<RPoint2D> points = CGUtil.createRPoint2DList("0 0   1 0   1 1   0 1");
        RPoint2D a = points.get(0);
        RPoint2D b = points.get(1);
        RPoint2D c = points.get(2);
        RPoint2D d = points.get(3);

        assertFalse(a.isRightOf(b));
        assertFalse(c.isRightOf(b));
        assertFalse(a.isRightOf(d));

        assertTrue(b.isRightOf(a));
        assertTrue(b.isRightOf(d));
        assertTrue(c.isRightOf(a));
        assertTrue(c.isRightOf(d));
    }

    @Test
    public void testIsRightOf_Line() {
        RLine2D line = new RLine2D("f(x) -> 2*x - 3");
        assertTrue(new RPoint2D(2,0).isRightOf(line));
        assertTrue(new RPoint2D(0,-4).isRightOf(line));
        assertTrue(new RPoint2D(3,2).isRightOf(line));
        assertFalse(new RPoint2D(2,2).isRightOf(line));
        assertFalse(new RPoint2D(-1,-4).isRightOf(line));
        assertFalse(new RPoint2D(0,0).isRightOf(line));
        assertFalse(new RPoint2D(-5,5).isRightOf(line));
        assertFalse(new RPoint2D(-1,-5).isRightOf(line)); // point on the line

        RLine2D lineH = new RLine2D("y = -5");
        assertFalse(new RPoint2D(2,0).isRightOf(lineH));
        assertFalse(new RPoint2D(2,-4).isRightOf(lineH));
        assertFalse(new RPoint2D(2,-5).isRightOf(lineH)); // point on the line
        assertTrue(new RPoint2D(2,-7).isRightOf(lineH));
        assertTrue(new RPoint2D(2000,-77).isRightOf(lineH));

        RLine2D lineV = new RLine2D("x = -5");
        assertFalse(new RPoint2D(-6,0).isRightOf(lineV));
        assertFalse(new RPoint2D(-9,-4).isRightOf(lineV));
        assertFalse(new RPoint2D(-5,8).isRightOf(lineV)); // point on the line
        assertTrue(new RPoint2D(7,8).isRightOf(lineV));
        assertTrue(new RPoint2D(-4,80000).isRightOf(lineV));
    }

    @Test
    public void testTranslate() {
        RPoint2D a = new RPoint2D(1, 1);

        assertTrue(a.translate(1, 2).equals(new RPoint2D(2, 3)));
        assertTrue(a.translate(new Rational("1"), new Rational("2")).equals(new RPoint2D(2, 3)));
        assertTrue(a.translate(new Rational("1"), new Rational("2")).equals(new RPoint2D(2, 3)));

        assertFalse(a.translate(1, 2).equals(new RPoint2D(3, 3)));
        assertFalse(a.translate(new Rational("1"), new Rational("2")).equals(new RPoint2D(3, 3)));
        assertFalse(a.translate(new Rational("1"), new Rational("2")).equals(new RPoint2D(3, 3)));

        assertTrue(a.translate(1, -2).equals(new RPoint2D(2, -1)));
        assertTrue(a.translate(new Rational("1"), new Rational("-2")).equals(new RPoint2D(2, -1)));
        assertTrue(a.translate(new Rational("-1/-1"), new Rational("-2/1")).equals(new RPoint2D(2, -1)));
    }
}
