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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public class TestRational {

    /*
     * Helper method to create instance of Rational numbers
     */
    private static Rational[] create(String... numbers) {
        List<Rational> list = new ArrayList<Rational>();
        for (String n : numbers) {
            list.add(new Rational(n));
        }
        return list.toArray(new Rational[list.size()]);
    }

    @Test
    public void testRationalString() {
        // repeating decimal
        assertTrue(new Rational("0.333...").equals(new Rational("1/3")));
        assertTrue(new Rational("-2.6...").equals(new Rational("-8/3")));
        assertTrue(new Rational("0.142857142857...").equals(new Rational("1/7")));
        assertTrue(new Rational("0.012345679...").equals(new Rational("1/81")));
        assertTrue(new Rational("-0.58333...").equals(new Rational("-7/12")));
        assertTrue(new Rational("0.99...").equals(new Rational("1/1")));
        assertTrue(new Rational("1.23444...").equals(new Rational("1111/900")));
        assertTrue(new Rational("-0.3789789...").equals(new Rational("-631/1665")));
        assertTrue(new Rational("-0.0...").equals(new Rational("0")));
        assertTrue(new Rational("-0.0...").equals(new Rational("0.00000...")));
        assertTrue(new Rational("0.010309278350515463917525773195876288659793814432989690721649484536082474226804123711340206185567...").equals(new Rational("1/97")));
        assertTrue(new Rational("0.010309278350515463917525773195876288659793814432989690721649484536082474226804123711340206185567"+
                "010309278350515463917525773195876288659793814432989690721649484536082474226804123711340206185567...").equals(new Rational("1/97")));

        // decimal
        assertTrue(new Rational("0.3").equals(new Rational("3/10")));
        assertTrue(new Rational("1.3").equals(new Rational("13/10")));
        assertTrue(new Rational("0.1").equals(new Rational("1/10")));
        assertTrue(new Rational("0.01").equals(new Rational("1/100")));
        assertTrue(new Rational("0.001").equals(new Rational("1/1000")));
        assertTrue(new Rational("0.0001").equals(new Rational("1/10000")));

        // fraction
        assertTrue(new Rational("-1/-4").equals(new Rational("0.25")));
        assertTrue(new Rational("1/-4").equals(new Rational("-0.25")));
        assertTrue(new Rational("-1/4").equals(new Rational("-0.25")));

        // integer
        assertTrue(new Rational("1").equals(new Rational("1.0")));
        assertTrue(new Rational("1").equals(new Rational("1/1")));
        assertTrue(new Rational("0").equals(new Rational("0/1")));
    }

    @Test(expected = ArithmeticException.class)
    public void testRationalStringArithmeticException() {
        new Rational("234/0");
    }

    @Test(expected = NumberFormatException.class)
    public void testRationalStringNumberFormatException1() {
        new Rational("abc");
    }

    @Test(expected = NumberFormatException.class)
    public void testRationalStringNumberFormatException2() {
        new Rational("0....");
    }

    @Test
    public void testRationalLong() {
        new Rational(17654343242342432L);
        new Rational(-176543);
        new Rational(0);
    }

    @Test
    public void testRationalLongLong() {
        new Rational(17654343242342432L, 32122);
        new Rational(176543, -32122);
        new Rational(-176543, 32122);
        new Rational(-176543, -32122);
    }

    @Test(expected = ArithmeticException.class)
    public void testRationalIntIntArithmeticException() {
        new Rational(234, 0);
    }

    @Test
    public void testRationalBigIntegerBigInteger() {
        new Rational(new BigInteger("234"), new BigInteger("23"));
        new Rational(new BigInteger("-234"), new BigInteger("23"));
        new Rational(new BigInteger("234"), new BigInteger("-23"));
        new Rational(new BigInteger("-234"), new BigInteger("-23"));
    }

    @Test(expected = ArithmeticException.class)
    public void testRationalBigIntegerBigIntegerArithmeticException() {
        new Rational(new BigInteger("234"), new BigInteger("0"));
    }
    
    @Test
    public void testHashCode() {
        Rational[] ra = create("8/4", "2", "1/3", "15/45", "1/1000000000000000000", "1/1000000000000000001");
        assertTrue(ra[0].hashCode() == ra[1].hashCode());
        assertTrue(ra[2].hashCode() == ra[3].hashCode());
    }

    @Test
    public void testIntValue() {
        String large = "9999999999999999999.999";
        Rational[] ra = create("8/4", "2", "2/3", "-5/2", large);
        int[] expected = {2, 2, 0, -2, new BigDecimal(large).intValue()};
        int i = 0;
        for (Rational r : ra) {
            assertTrue(r.intValue() == expected[i++]);
        }
    }

    @Test
    public void testLongValue() {
        String large = "9999999999999999999.999";
        Rational[] ra = create("8/4", "2", "2/3", "-5/2", large);
        long[] expected = {2, 2, 0, -2, new BigDecimal(large).longValue()};
        int i = 0;
        for (Rational r : ra) {
            assertTrue(r.longValue() == expected[i++]);
        }
    }

    @Test
    public void testFloatValue() {
        String large = "9999999999999999999.999";
        Rational[] ra = create("8/4", "2", "2/3", "-5/2", large);
        float[] expected = {8.0f / 4.0f, 2.0f, 2.0f / 3.0f, -5.0f / 2.0f,
                new BigDecimal(large).floatValue()};
        int i = 0;
        for (Rational r : ra) {
            assertTrue(r.floatValue() == expected[i++]);
        }
    }

    @Test
    public void testDoubleValue() {
        String large = "9999999999999999999.999";
        Rational[] ra = create("8/4", "2", "2/3", "-5/2", large);
        double[] expected = {8.0d / 4.0d, 2.0d, 2.0d / 3.0d, -5.0d / 2.0d,
                new BigDecimal(large).doubleValue()};
        int i = 0;
        for (Rational r : ra) {
            assertTrue(r.doubleValue() == expected[i++]);
        }
    }

    @Test
    public void testAbs() {
        Rational[] values = create("-1/2", "1/-99", "-9/2", "-1/-2");
        Rational[] expected = create("1/2", "1/99", "9/2", "1/2");
        int index = 0;
        for (Rational v : values) {
            assertTrue(v.abs().equals(expected[index]));
            index++;
        }
    }

    @Test
    public void testAdd() {
        Rational[] valuesA = create("1/2", "-1/2", "1/5", "1000");
        Rational[] valuesB = create("1/2", "1/2", "1/6", "-2000");
        Rational[] expected = create("1", "0", "11/30", "-1000");
        for (int i = 0; i < valuesA.length; i++) {
            assertTrue(valuesA[i].add(valuesB[i]).equals(expected[i]));
        }
    }

    @Test
    public void testCompareTo() {
        Rational a = Rational.NEGATIVE_INFINITY;
        Rational b = new Rational("-111");
        Rational c = new Rational("11");
        Rational d = new Rational("22/2");
        Rational e = Rational.POSITIVE_INFINITY;
        Rational f = Rational.NaN;
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertTrue(b.compareTo(c) < 0);
        assertTrue(c.compareTo(b) > 0);
        assertTrue(c.compareTo(d) == 0);
        assertTrue(d.compareTo(e) < 0);
        assertTrue(e.compareTo(d) > 0);
        assertTrue(e.compareTo(f) < 0);
        assertTrue(f.compareTo(e) > 0);
    }

    @Test
    public void testDenominator() {
        Rational[] values = create("1/2", "-1/-2", "1/-5", "1000");
        assertTrue(values[0].denominator().equals(new Rational("2")));
        assertTrue(values[1].denominator().equals(new Rational("2")));
        assertTrue(values[2].denominator().equals(new Rational("-5")));
        assertTrue(values[3].denominator().equals(new Rational("1")));
        Rational posInf = Rational.POSITIVE_INFINITY;
        Rational negInf = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(posInf.denominator().equals(posInf));
        assertTrue(negInf.denominator().equals(negInf));
        assertTrue(nan.denominator().equals(nan));
    }

    @Test
    public void testDivide() {
        Rational[] valuesA = create("1/2", "-1/2", "-1/5", "1000");
        Rational[] valuesB = create("1/2", "1/2", "1/-6", "-2000");
        Rational[] expected = create("1", "-1", "6/5", "-1/2");
        for (int i = 0; i < valuesA.length; i++) {
            assertTrue(valuesA[i].divide(valuesB[i]).equals(expected[i]));
        }

        // The corner cases:
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        Rational n = Rational.ONE;
        Rational zero = Rational.ZERO;
        assertTrue(pi.divide(n).equals(pi));   // Infinity / N = Infinity
        assertTrue(n.divide(pi).equals(zero)); // N / Infinity = 0.0
        assertTrue(n.divide(ni).equals(zero)); // N / -Infinity = 0.0 
        assertTrue(ni.divide(n).equals(ni));   // -Infinity / N = -Infinity 
        assertTrue(pi.divide(ni).equals(nan)); // Infinity / -Infinity = NaN 
        assertTrue(ni.divide(pi).equals(nan)); // -Infinity / Infinity = NaN
        assertTrue(pi.divide(pi).equals(nan)); // Infinity / Infinity = NaN 
        assertTrue(ni.divide(ni).equals(nan)); // -Infinity / -Infinity = NaN
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideArithmeticException() {
        Rational.ONE.divide(Rational.ZERO);
    }

    @Test
    public void testEqualsObject() {
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        Rational one = Rational.ONE;
        assertTrue(new Rational("1/2").equals(new Rational("2/4")));
        assertTrue(one.equals(new Rational("1")));
        assertTrue(nan.equals(Rational.NaN));
        assertTrue(pi.equals(Rational.POSITIVE_INFINITY));
        assertTrue(ni.equals(Rational.NEGATIVE_INFINITY));
        assertFalse(nan.equals(pi));
        assertFalse(nan.equals(ni));
        assertFalse(ni.equals(pi));
    }

    @Test
    public void testInverse() {
        Rational a = new Rational("1/2");
        Rational b = new Rational("-2/4");
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(a.inverse().equals(new Rational("2")));
        assertTrue(b.inverse().equals(new Rational("-2")));
        assertTrue(pi.inverse().equals(Rational.ZERO));
        assertTrue(ni.inverse().equals(Rational.ZERO));
        assertTrue(nan.inverse().equals(nan));
    }

    @Test(expected = ArithmeticException.class)
    public void testInverseArithmeticException() {
        Rational.ZERO.inverse();
    }

    @Test
    public void testIsInfinite() {
        assertTrue(Rational.POSITIVE_INFINITY.isInfinite());
        assertTrue(Rational.NEGATIVE_INFINITY.isInfinite());
        assertFalse(Rational.NaN.isInfinite());
        assertFalse(new Rational("444/21").isInfinite());
    }

    @Test
    public void testIsLessThan() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        Rational d = new Rational("1");
        assertTrue(a.isLessThan(b));
        assertTrue(b.isLessThan(c));
        assertFalse(c.isLessThan(d));
        assertFalse(b.isLessThan(a));
        assertFalse(c.isLessThan(b));
        assertFalse(d.isLessThan(c));
    }

    @Test
    public void testIsLessThanEq() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        Rational d = new Rational("1");
        assertTrue(a.isLessThanEq(b));
        assertTrue(b.isLessThanEq(c));
        assertTrue(c.isLessThanEq(d));
        assertFalse(b.isLessThanEq(a));
        assertFalse(c.isLessThanEq(b));
        assertTrue(d.isLessThanEq(c));
    }

    @Test
    public void testIsMoreThan() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        Rational d = new Rational("1");
        assertFalse(a.isMoreThan(b));
        assertFalse(b.isMoreThan(c));
        assertFalse(c.isMoreThan(d));
        assertTrue(b.isMoreThan(a));
        assertTrue(c.isMoreThan(b));
        assertFalse(d.isMoreThan(c));
    }

    @Test
    public void testIsMoreThanEq() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        Rational d = new Rational("1");
        assertFalse(a.isMoreThanEq(b));
        assertFalse(b.isMoreThanEq(c));
        assertTrue(c.isMoreThanEq(d));
        assertTrue(b.isMoreThanEq(a));
        assertTrue(c.isMoreThanEq(b));
        assertTrue(d.isMoreThanEq(c));
    }

    @Test
    public void testIsNaN() {
        assertTrue(Rational.NaN.isNaN());
        assertFalse(Rational.POSITIVE_INFINITY.isNaN());
        assertFalse(Rational.NEGATIVE_INFINITY.isNaN());
        assertFalse(Rational.ONE.isNaN());
    }

    @Test
    public void testIsNegative() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(a.isNegative());
        assertFalse(b.isNegative());
        assertFalse(c.isNegative());
        assertFalse(pi.isNegative());
        assertTrue(ni.isNegative());
        assertFalse(nan.isNegative());
    }

    @Test
    public void testIsPositive() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertFalse(a.isPositive());
        assertTrue(b.isPositive());
        assertTrue(c.isPositive());
        assertTrue(pi.isPositive());
        assertFalse(ni.isPositive());
        assertFalse(nan.isPositive());
    }

    @Test
    public void testIsRational() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(a.isRational());
        assertTrue(b.isRational());
        assertTrue(c.isRational());
        assertFalse(pi.isRational());
        assertFalse(ni.isRational());
        assertFalse(nan.isRational());
    }

    @Test
    public void testMinusOne() {
        Rational a = new Rational("-1");
        Rational b = new Rational("0");
        Rational c = new Rational("1");
        assertTrue(b.minusOne().equals(a));
        assertTrue(c.minusOne().equals(b));
    }

    @Test
    public void testMultiply() {
        Rational[] valuesA = create("1/2", "-1/2", "-1/5", "1000");
        Rational[] valuesB = create("1/2", "1/2", "1/-6", "-2000");
        Rational[] expected = create("1/4", "-1/4", "1/30", "-2000000");
        for (int i = 0; i < valuesA.length; i++) {
            assertTrue(valuesA[i].multiply(valuesB[i]).equals(expected[i]));
        }
        // corner cases:
        Rational nn = new Rational("-1");
        Rational pn = new Rational("1");
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(ni.multiply(pn).equals(ni)); // -Infinity * N = -Infinity
        assertTrue(pn.multiply(ni).equals(ni)); // N * -Infinity = -Infinity
        assertTrue(pi.multiply(ni).equals(ni)); // Infinity * -Infinity = -Infinity
        assertTrue(ni.multiply(pi).equals(ni)); // -Infinity * Infinity = -Infinity
        assertTrue(pi.multiply(nn).equals(ni)); // Infinity * -N = -Infinity
        assertTrue(ni.multiply(nn).equals(pi)); // -Infinity * -N = Infinity
        assertTrue(pi.multiply(pn).equals(pi)); // Infinity * N = Infinity
        assertTrue(pn.multiply(pi).equals(pi)); // N * Infinity = Infinity     
        assertTrue(pi.multiply(pn).equals(pi)); // Infinity * Infinity = Infinity
        assertTrue(ni.multiply(ni).equals(pi)); // -Infinity * -Infinity = Infinity
        // NaN tests
        assertTrue(nan.multiply(pn).equals(nan));
        assertTrue(pn.multiply(nan).equals(nan));
        assertTrue(nan.multiply(nn).equals(nan));
        assertTrue(nn.multiply(nan).equals(nan));
        assertTrue(nan.multiply(pi).equals(nan));
        assertTrue(pi.multiply(nan).equals(nan));
        assertTrue(nan.multiply(ni).equals(nan));
        assertTrue(ni.multiply(nan).equals(nan));
        assertTrue(nan.multiply(nan).equals(nan));
    }

    @Test
    public void testNegate() {
        Rational[] values = create("1/2", "-1/2", "1/-6", "-2000.0");
        Rational[] expected = create("-1/2", "1/2", "1/6", "2000");
        for (int i = 0; i < values.length; i++) {
            assertTrue(values[i].negate().equals(expected[i]));
        }
        // No need to test corner cases: these are handled by testMultiply() 
    }

    @Test
    public void testNumerator() {
        Rational[] values = create("1/2", "-1/2", "3/-5", "1000");
        assertTrue(values[0].numerator().equals(new Rational("1")));
        assertTrue(values[1].numerator().equals(new Rational("-1")));
        assertTrue(values[2].numerator().equals(new Rational("-3")));
        assertTrue(values[3].numerator().equals(new Rational("1000")));
        Rational posInf = Rational.POSITIVE_INFINITY;
        Rational negInf = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(posInf.numerator().equals(posInf));
        assertTrue(negInf.numerator().equals(negInf));
        assertTrue(nan.numerator().equals(nan));
    }

    @Test
    public void testPlusOne() {
        Rational[] values = create("1/2", "-1/2", "1/-6", "-2000");
        Rational[] expected = create("3/2", "1/2", "5/6", "-1999");
        for (int i = 0; i < values.length; i++) {
            assertTrue(values[i].plusOne().equals(expected[i]));
        }
        // No need to test corner cases: these are handled by testAdd() 
    }

    @Test
    public void testPow() {
        Rational[] values = create("1/2", "-1/2", "1/-6", "-2000.0");
        int[] n = {0, 2, 3, 4};
        Rational[] expected = create("1", "1/4", "-1/216", "16000000000000");
        for (int i = 0; i < values.length; i++) {
            assertTrue(values[i].pow(n[i]).equals(expected[i]));
        }
        // corner cases:
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(pi.pow(555).equals(pi)); // Infinity^N = Infinity 
        assertTrue(ni.pow(555).equals(ni)); // -Infinity^((N*2)+1) = -Infinity 
        assertTrue(ni.pow(554).equals(pi)); // -Infinity^(N*2) = Infinity 
        assertTrue(nan.pow(555).equals(nan));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPowIllegalArgumentException() {
        Rational.ONE.pow(-8);
    }

    @Test
    public void testSubtract() {
        Rational[] valuesA = create("1/2", "-1/2", "-1/5", "1000");
        Rational[] valuesB = create("4/8", "-1/2", "1/6", "-2000");
        Rational[] expected = create("0", "0", "-11/30", "3000");
        for (int i = 0; i < valuesA.length; i++) {
            assertTrue(valuesA[i].subtract(valuesB[i]).equals(expected[i]));
        }
        // corner cases:
        Rational n = new Rational("1000000");
        Rational pi = Rational.POSITIVE_INFINITY;
        Rational ni = Rational.NEGATIVE_INFINITY;
        Rational nan = Rational.NaN;
        assertTrue(n.subtract(ni).equals(pi)); // N - -Infinity = Infinity
        assertTrue(pi.subtract(ni).equals(pi)); // Infinity - -Infinity = Infinity
        assertTrue(pi.subtract(n).equals(pi)); // Infinity - N = Infinity
        assertTrue(n.subtract(pi).equals(ni)); // N - Infinity = -Infinity
        assertTrue(ni.subtract(n).equals(ni)); // -Infinity - N = -Infinity
        assertTrue(ni.subtract(pi).equals(ni)); // -Infinity - Infinity = -Infinity
        assertTrue(pi.subtract(pi).equals(nan)); // Infinity - Infinity = NaN
        assertTrue(ni.subtract(ni).equals(nan)); // -Infinity - -Infinity = NaN 
        // NaN tests
        assertTrue(nan.subtract(n).equals(nan));
        assertTrue(n.subtract(nan).equals(nan));
        assertTrue(nan.subtract(pi).equals(nan));
        assertTrue(pi.subtract(nan).equals(nan));
        assertTrue(nan.subtract(ni).equals(nan));
        assertTrue(ni.subtract(nan).equals(nan));
        assertTrue(nan.subtract(nan).equals(nan));
    }

    @Test
    public void testToBigDecimal() {
        String[] values = {"1231231232", "0.0000000000000000000001", "22.1312",
                "999999999999999999999999999999999999999999999999999999999999"};
        for (String v : values) {
            BigDecimal bd = new BigDecimal(v);
            Rational r = new Rational(v);
            assertTrue(bd.equals(r.toBigDecimal()));
        }
    }

    @Test
    public void testToBigDecimalInt() {
        int p = 1000;
        String s = new Rational("1/3").toBigDecimal(p).toPlainString();
        assertTrue(s.matches("0\\.3{" + p + "}"));
        p = 123;
        s = new Rational("2/3").toBigDecimal(p).toPlainString();
        assertTrue(s.matches("0\\.6{" + (p - 1) + "}7"));
    }

    @Test
    public void testMax() {
        Rational a = new Rational("-10");
        Rational b = new Rational("-11");
        Rational c = new Rational("10");
        Rational d = new Rational("-10");
        Rational e = new Rational("100000000000000");
        Rational f = Rational.NaN;
        Rational g = Rational.NEGATIVE_INFINITY;
        Rational h = Rational.POSITIVE_INFINITY;

        assertTrue(Rational.max(a,b) == a);
        assertTrue(Rational.max(a,c) == c);
        assertTrue(Rational.max(a,d).equals(a));
        assertTrue(Rational.max(a,d).equals(d));
        assertTrue(Rational.max(f,g) == f);
        assertTrue(Rational.max(e,c) == e);
        assertTrue(Rational.max(e,h) == h);
        assertTrue(Rational.max(h,f) == f);
        assertTrue(Rational.max(h,g) == h);
    }

    @Test
    public void testMin() {
        Rational a = new Rational("-10");
        Rational b = new Rational("-11");
        Rational c = new Rational("10");
        Rational d = new Rational("-10");
        Rational e = new Rational("100000000000000");
        Rational f = Rational.NaN;
        Rational g = Rational.NEGATIVE_INFINITY;
        Rational h = Rational.POSITIVE_INFINITY;

        assertFalse(Rational.min(a,b) == a);
        assertFalse(Rational.min(a,c) == c);
        assertTrue(Rational.min(a,d).equals(a));
        assertTrue(Rational.min(a,d).equals(d));
        assertFalse(Rational.min(f,g) == f);
        assertFalse(Rational.min(e,c) == e);
        assertFalse(Rational.min(e,h) == h);
        assertFalse(Rational.min(h,f) == f);
        assertFalse(Rational.min(h,g) == h);
    }
}
