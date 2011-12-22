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

import compgeom.util.parser.RationalParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * <p>
 * A class that represents a rational value of arbitrary size. It is backed up
 * by two BigInteger's as numerator and denominator.
 * </p>
 * <p>
 * Note that a value like 2/4 is normalized to 1/2 and the information that 2
 * was it's numerator and 4 it's denominator is lost.
 * </p>
 * <p>
 * All arithmetical operations concerning infinity follow the same rules as the
 * java.lang.Double class except there's no notion of minus zero in the Rational
 * class. So a normal rational number divided by {@link #NEGATIVE_INFINITY}
 * equals a normal rational number divided by {@link #POSITIVE_INFINITY} (both
 * equal zero) while the Double class returns -0.0 and 0.0 respectively (which
 * are not considered equal in Java):
 * </p>
 * <pre>
 * Double m = 1.0/Double.NEGATIVE_INFINITY;
 * Double n = 1.0/Double.POSITIVE_INFINITY;
 * System.out.printf("%f == %f ? %s", m, n, m.equals(n));
 * // Output:
 * //           -0.000000 == 0.000000 ? false
 * </pre>
 * <p>
 * but the Rational class will print <code>true</code> in such cases:
 * </p>
 * <p>
 * <pre>
 * Rational m = Rational.ONE.divide(Rational.NEGATIVE_INFINITY);
 * Rational n = Rational.ONE.divide(Rational.POSITIVE_INFINITY);
 * System.out.printf("%s == %s ? %s", m, n, m.equals(n));
 * // Output:
 * //           0 == 0 ? true
 * </pre>
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class Rational extends Number implements Comparable<Rational> {

    /**
     * serial version UID since Number is Serializable.
     */
    private static final long serialVersionUID = 8712356785548951879L;

    /**
     * the default precision used by {@link #toBigDecimal()}.
     */
    public static final int DEFAULT_PRECISION = 100;

    /**
     * the public static value -1.
     */
    public static final Rational MINUS_ONE = new Rational(-1);

    /**
     * the public static value 0.
     */
    public static final Rational ZERO = new Rational(0);

    /**
     * the public static value 1.
     */
    public static final Rational ONE = new Rational(1);

    /**
     * the public static value 10.
     */
    public static final Rational TEN = new Rational(10);

    /**
     * the public static value 100.
     */
    public static final Rational HUNDRED = new Rational(100);

    /**
     * the public static value 1000.
     */
    public static final Rational THOUSAND = new Rational(1000);

    /**
     * the public static value positive infinity.
     */
    public static final Rational POSITIVE_INFINITY = new Rational(Double.POSITIVE_INFINITY);

    /**
     * the public static value negative infinity.
     */
    public static final Rational NEGATIVE_INFINITY = new Rational(Double.NEGATIVE_INFINITY);

    /**
     * the public static value NaN (not a number).
     */
    public static final Rational NaN = new Rational(Double.NaN);

    /**
     * the numerator of this rational.
     */
    private BigInteger numerator;

    /**
     * the denominator of this rational.
     */
    private BigInteger denominator;

    /**
     * a pre-calculated hash code
     */
    private int hash;

    /**
     * <p>
     * Creates a new Rational from a given <code>number</code>. The number
     * can be one of four formats:
     * </p>
     * <hr />
     * <h4>1. Repeating Decimal</h4>
     * <p>
     * A decimal value with three trailing dots (ellipsis). The ellipsis
     * denote the largest repeating number sequence from the decimal part.
     * Some examples:
     * </p>
     * <table border="1">
     * <tr><th><code>String number</code></th><th>&nbsp;repeating digit(s)&nbsp;</th><th>&nbsp;equals fraction&nbsp;</th></tr>
     * <tr><td><code>"0.333..."</code></td><td>3</td><td>1/3</td></tr>
     * <tr><td><code>"2.6..."</code></td><td>6</td><td>8/3</td></tr>
     * <tr><td><code>"0.142857142857..."</code></td><td>142857</td><td>1/7</td></tr>
     * <tr><td><code>"0.012345679..."</code></td><td>012345679</td><td>1/81</td></tr>
     * <tr><td><code>"0.58333..."</code></td><td>3</td><td>7/12</td></tr>
     * <tr><td><code>"0.99..."</code></td><td>9</td><td><a href="http://mathforum.org/dr.math/faq/faq.0.9999.html">1/1</a></td></tr>
     * </table>
     * <p>
     * Note that the repetition must be repeated at least once. For example,
     * if you want to represent the rational 34/303, the repeated decimal
     * 0.1122... is incorrect (this equals 0.11222222..., which is 101/900).
     * You'll need to provide 0.11221122... instead to create a Rational
     * representing 34/303.
     * </p>
     * <h4>2. Decimal</h4>
     * <p>
     * A decimal number consists of two sequences of digits separated by
     * a decimal point with an optional minus sign to indicate if the number
     * is negative. Some examples:
     * <ul>
     * <li>0.01</li>
     * <li>545665878972121654549875461232124654.00000000000000000000000000001</li>
     * <li>-3.987</li>
     * </ul>
     * </p>
     * <h4>3. Fraction</h4>
     * <p>
     * A fraction consists of two sequences of digits, both of which can have
     * an optional minus sign in front of it, separated by a forward slash.
     * Some examples:
     * <ul>
     * <li>1/3</li>
     * <li>-99999999999999999999999999999999999999999999999999/2</li>
     * <li>5/-2 (equals -5/2)</li>
     * <li>-5/-1 (equals 5/1)</li>
     * </ul>
     * </p>
     * <h4>4. Integer</h4>
     * <p>
     * An integer is one or more digits with an optional minus sign in front of it.
     * </p>
     * <hr />
     * <p>
     * The formal grammar of valid rational numbers follows in
     * <a href="http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form">EBNF</a> notation:
     * </p>
     * <pre>
     * rational
     *   = repeating_decimal
     *   | decimal
     *   | fraction
     *   | integer
     *   ;
     *
     * repeating_decimal
     *   = decimal "..."
     *   ;
     *
     * decimal
     *   = integer "." digits
     *   ;
     *
     * fraction
     *   = integer "/" integer
     *   ;
     *
     * integer
     *   = ["-"] digits
     *   ;
     *
     * digits
     *   = digit {digit}
     *   ;
     *
     * digit
     *   = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
     *   ;
     * </pre>
     * <p>
     * A brief explanation of the notation above:
     * </p>
     * <ul>
     * <li><code>[X]</code> means an optional <code>X</code></li>
     * <li><code>{X}</code> means <code>X, zero or more times</code></li>
     * <li><code>X | Y</code> means <code>either X or Y</code></li>
     * <li><code>"X"</code> means the literal <code>X</code>
     * </ul>
     * <p>
     * <p>
     * <a href="http://www.antlr.org">ANTLR</a> was used to generate a lexer
     * and parser to parse the <code>number</code>.
     * </p>
     * 
     * @param number the number.
     * @throws NumberFormatException if a number is not conform the grammar above.
     */
    public Rational(String number) throws NumberFormatException {
        BigInteger[] temp = RationalParser.parse(number);
        this.init(temp[0], temp[1]);
    }

    /**
     * Creates a new Rational from a given number <code>n</code>.
     *
     * @param n the number.
     */
    public Rational(long n) {
        this(n, 1);
    }

    /**
     * Creates a new Rational from a given <code>numerator</code> and
     * <code>denominator</code>.
     *
     * @param num the numerator.
     * @param den the denominator.
     * @throws ArithmeticException when <code>den</code> equals zero.
     */
    public Rational(long num, long den) throws ArithmeticException {
        this(new BigInteger(String.valueOf(num)), new BigInteger(String.valueOf(den)));
    }

    /**
     * Creates a new Rational from a given <code>numerator</code> and
     * <code>denominator</code>.
     *
     * @param num the numerator.
     * @param den the denominator.
     * @throws ArithmeticException when <code>den</code> equals zero.
     */
    public Rational(BigInteger num, BigInteger den) throws ArithmeticException {
        init(num, den);
    }

    /**
     * Used only to create NaN, POSITIVE_INFINITY and NEGATIVE_INFINITY.
     *
     * @param wrapper the Double wrapper from which to get the hash code.
     */
    private Rational(Double wrapper) {
        hash = wrapper.hashCode();
    }

    /**
     * Returns the absolute value of <code>this</code> as a new Rational.
     *
     * @return the absolute value of <code>this</code> as a new Rational.
     */
    public Rational abs() {
        if (this.isNaN()) {
            return NaN;
        } else if (this.isInfinite()) {
            return POSITIVE_INFINITY;
        } else {
            return new Rational(numerator.abs(), denominator.abs());
        }
    }

    /**
     * Returns <code>this</code> added to <code>that</code>. The following
     * corner cases can occur:<br />
     * <br />
     * <code>
     * Infinity + N = Infinity <br />
     * N + Infinity = Infinity <br />
     * Infinity + Infinity = Infinity <br />
     * <br />
     * -Infinity + N = -Infinity <br />
     * N + -Infinity = -Infinity <br />
     * -Infinity + -Infinity = -Infinity <br />
     * <br />
     * Infinity + -Infinity = NaN <br />
     * -Infinity + Infinity = NaN <br />
     * </code><br />
     * <br />
     * where N is a regular rational number.
     *
     * @param that the Rational to add to <code>this</code>.
     * @return <code>this</code> + <code>that</code> as a new Rational.
     */
    public Rational add(Rational that) {
        if (this.isNaN() || that.isNaN()) {
            return NaN;
        } else if (this.isInfinite() || that.isInfinite()) {
            if (this.isInfinite() && that.isInfinite() &&
                    (this.isPositive() != that.isPositive())) {
                return NaN;
            } else if ((this.isInfinite() && this.isPositive()) ||
                    (that.isInfinite() && that.isPositive())) {
                return POSITIVE_INFINITY;
            } else {
                return NEGATIVE_INFINITY;
            }
        } else {
            BigInteger num = this.numerator.multiply(that.denominator).add(
                    this.denominator.multiply(that.numerator));
            BigInteger den = this.denominator.multiply(that.denominator);
            return new Rational(num, den);
        }
    }

    /**
     * Compares <code>this</code> and <code>that</code> according their natural
     * order. The following rules apply: <br />
     * <br />
     * <code>
     * -INF &lt; N &lt; +INF &lt; NaN <br />
     * </code>
     * <br />
     * where N is a normal rational number, +INF is positive infinity and
     * -INF is negative infinity.
     *
     * @param that the other number
     * @return -1 if <code>this</code> is less than <code>that</code>,
     *         1 if <code>this</code> is more than <code>that</code> or
     *         0 if <code>this</code> equals <code>that</code>.
     */
    @Override
    public int compareTo(Rational that) {
        if (this == that) return 0;
        if (this.isNaN() || that.isNaN()) {
            return this.isNaN() ? 1 : -1;
        } else if (this.isInfinite() || that.isInfinite()) {
            return (this == POSITIVE_INFINITY) || (that == NEGATIVE_INFINITY) ? 1 : -1;
        } else {
            BigInteger thisN = this.numerator.multiply(that.denominator);
            BigInteger thatN = that.numerator.multiply(this.denominator);
            int temp = thisN.compareTo(thatN);
            return temp < 0 ? -1 : temp > 0 ? 1 : 0;
        }
    }

    /**
     * Returns the denominator of <code>this</code> as a rational. If
     * <code>this</code> is NaN, or infinity, <code>this</code> is returned.
     * Note that when <code>this</code> is negative, the denominator is
     * returned as a positive value.
     *
     * @return the denominator of <code>this</code> as a rational. If
     *         <code>this</code> is NaN, or infinity, <code>this</code>
     *         is returned.
     */
    public Rational denominator() {
        if (!this.isRational()) {
            return this;
        } else {
            Rational den = new Rational(denominator, BigInteger.ONE);
            return this.isNegative() ? den.negate() : den;
        }
    }

    /**
     * Returns <code>this</code> divided to <code>that</code> as a new Rational.
     * The following corner cases can occur:<br />
     * <br />
     * <code>
     * Infinity / N = Infinity <br />
     * <br />
     * N / Infinity = 0.0 <br />
     * N / -Infinity = 0.0 (and <strong>not</strong> -0.0)<br />
     * <br />
     * -Infinity / N = -Infinity <br />
     * <br />
     * Infinity / -Infinity = NaN <br />
     * -Infinity / Infinity = NaN <br />
     * Infinity / Infinity = NaN <br />
     * -Infinity / -Infinity = NaN <br />
     * </code><br />
     * <br />
     * where N is a regular rational number.
     *
     * @param that the number by which <code>this</code> is to be divided.
     * @return <code>this</code> divided to <code>that</code> as a new Rational.
     * @throws ArithmeticException thrown when <code>that</code> is zero.
     */
    public Rational divide(Rational that) throws ArithmeticException {
        if (that == Rational.ZERO) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        if (this.isNaN() || that.isNaN()) {
            return NaN;
        } else if (this.isInfinite() || that.isInfinite()) {
            if (this.isRational() && that.isInfinite()) {
                return Rational.ZERO;
            } else if (that.isRational()) {
                return this;
            } else {
                return NaN;
            }
        } else {
            return this.multiply(that.inverse());
        }
    }

    /**
     * Converts <code>this</code> into a primitive double value.
     *
     * @return <code>this</code> as a primitive double value.
     */
    @Override
    public double doubleValue() {
        if (this.isNaN()) {
            return Double.NaN;
        } else if (this.isInfinite()) {
            return this.isPositive() ? Double.POSITIVE_INFINITY :
                    Double.NEGATIVE_INFINITY;
        } else {
            return this.toBigDecimal().doubleValue();
        }
    }

    /**
     * Returns <code>true</code> iff <code>o</code> is a <code>Rational</code>
     * and if <code>this.numerator</code> equals <code>that.numerator</code>
     * and <code>this.denominator</code> equals <code>that.denominator</code>.
     *
     * @param o the other object
     * @return <code>true</code> iff <code>o</code> is a <code>Rational</code>
     *         and if <code>this.numerator</code> equals <code>that.numerator</code>
     *         and <code>this.denominator</code> equals <code>that.denominator</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rational that = (Rational) o;
        if (!this.isRational() || !that.isRational()) {
            return this == that;
        } else {
            return numerator.equals(that.numerator) &&
                    denominator.equals(that.denominator);
        }
    }

    /**
     * Converts <code>this</code> into a primitive float value.
     *
     * @return <code>this</code> as a primitive float value.
     */
    @Override
    public float floatValue() {
        if (this.isNaN()) {
            return Float.NaN;
        } else if (this.isInfinite()) {
            return this.isPositive() ? Float.POSITIVE_INFINITY :
                    Float.NEGATIVE_INFINITY;
        } else {
            return this.toBigDecimal().floatValue();
        }
    }

    /**
     * Returns a pre-calculated hash code:
     * 0 if <code>NaN()</code>, 1 if <code>POSITIVE_INFINITY</code>,
     * -1 if <code>NEGATIVE_INFINITY</code> and else
     * <code>(numerator.hashCode()*13) ^ (denominator.hashCode()*37)</code>.
     *
     * @return a pre-calculated hash code:
     *         0 if <code>NaN()</code>, 1 if <code>POSITIVE_INFINITY</code>,
     *         -1 if <code>NEGATIVE_INFINITY</code> and else
     *         <code>(numerator.hashCode()*13) ^ (denominator.hashCode()*37)</code>.
     */
    @Override
    public int hashCode() {
        return this.hash;
    }

    /**
     * Initializes this Rational and normalizes the numerator and denominator.
     *
     * @param num the numerator.
     * @param den the denominator.
     */
    private void init(BigInteger num, BigInteger den) {
        if (den.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("denominator can't be zero");
        }
        this.denominator = den;
        this.numerator = num;
        this.normalize();
        this.hash = (numerator.hashCode() * 13) ^ (denominator.hashCode() * 37);
    }

    /**
     * Converts <code>this</code> into a primitive int value. The same
     * rounding as in BigDecimal's intValue() method is applied here.
     *
     * @return <code>this</code> as a primitive int value.
     * @see BigDecimal#intValue()
     */
    @Override
    public int intValue() throws IllegalStateException {
        return this.toBigDecimal().intValue();
    }

    /**
     * Returns the inverse, <code>1/(m/n)</code>, which equals
     * <code>n/m</code>, of a rational <code>m/n</code>.
     * When <code>this</code> is infinite, 0 is returned and when
     * <code>this</code> is <code>NaN</code>, <code>NaN</code> is returned.
     *
     * @return the inverse, <code>1/(m/n)</code>, which equals
     *         <code>n/m</code>, of a rational <code>m/n</code>.
     *         When <code>this</code> is infinite, 0 is returned and when
     *         <code>this</code> is <code>NaN</code>, <code>NaN</code> is returned.
     * @throws ArithmeticException when <code>this</code> equals zero.
     */
    public Rational inverse() throws ArithmeticException {
        if (this == Rational.ZERO) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        if (this.isNaN()) {
            return NaN;
        } else if (this.isInfinite() || numerator.equals(BigInteger.ZERO)) {
            return ZERO;
        } else {
            return new Rational(denominator, numerator);
        }
    }

    /**
     * Returns <code>true</code> iff <code>this</code> equals
     * {@link #NEGATIVE_INFINITY} or {@link #POSITIVE_INFINITY}, else
     * <code>false</code> is returned.
     *
     * @return <code>true</code> iff <code>this</code> equals
     *         {@link #NEGATIVE_INFINITY} or {@link #POSITIVE_INFINITY}, else
     *         <code>false</code> is returned.
     */
    public boolean isInfinite() {
        return (this == POSITIVE_INFINITY) || (this == NEGATIVE_INFINITY);
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is less than
     * <code>that</code>.
     *
     * @param that the rational number to compare to <code>this</code>.
     * @return <code>true</code> iff <code>this</code> is less than
     *         <code>that</code>.
     * @see #compareTo(Rational)
     */
    public boolean isLessThan(Rational that) {
        return this.compareTo(that) < 0;
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is less than, or equals
     * <code>that</code>.
     *
     * @param that the rational number to compare to <code>this</code>.
     * @return <code>true</code> iff <code>this</code> is less than,
     *         or equals <code>that</code>.
     * @see #compareTo(Rational)
     */
    public boolean isLessThanEq(Rational that) {
        return this.compareTo(that) <= 0;
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is more than
     * <code>that</code>.
     *
     * @param that the rational number to compare to <code>this</code>.
     * @return <code>true</code> iff <code>this</code> is more than
     *         <code>that</code>.
     * @see #compareTo(Rational)
     */
    public boolean isMoreThan(Rational that) {
        return this.compareTo(that) > 0;
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is more than, or equals
     * <code>that</code>.
     *
     * @param that the rational number to compare to <code>this</code>.
     * @return <code>true</code> iff <code>this</code> is more than, or equals
     *         <code>that</code>.
     * @see #compareTo(Rational)
     */
    public boolean isMoreThanEq(Rational that) {
        return this.compareTo(that) >= 0;
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is NaN.
     *
     * @return <code>true</code> iff <code>this</code> is NaN.
     */
    public boolean isNaN() {
        return this == NaN;
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is negative (less than
     * zero).
     *
     * @return <code>true</code> iff <code>this</code> is negative (less than
     *         zero).
     */
    public boolean isNegative() {
        return !this.isNaN() && !isPositive();
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is positive (more than,
     * or equal to zero).
     *
     * @return <code>true</code> iff <code>this</code> is positive (more than,
     *         or equal to zero).
     */
    public boolean isPositive() {
        if (this.isNaN()) {
            return false;
        } else if (this.isInfinite()) {
            return this == POSITIVE_INFINITY;
        } else {
            return numerator.compareTo(BigInteger.ZERO) >= 0;
        }
    }

    /**
     * Returns <code>true</code> iff <code>this</code> is not NaN or infinite.
     *
     * @return <code>true</code> iff <code>this</code> is not NaN or infinite.
     */
    public boolean isRational() {
        return !this.isInfinite() && !this.isNaN();
    }

    /**
     * Converts <code>this</code> into a primitive long value. The same
     * rounding as in BigDecimal's longValue() method is applied here.
     *
     * @return <code>this</code> as a primitive long value.
     */
    @Override
    public long longValue() {
        return this.toBigDecimal().longValue();
    }

    /**
     * Returns the rational with the maximum value between <code>a</code>
     * and <code>b</code>/. The following rules apply: <br />
     * <br />
     * <code>-INF &lt; N &lt; +INF &lt; NaN</code>
     *
     * @param a the first rational value.
     * @param b the second rational value.
     * @return  the rational with the maximum value between <code>a</code> 
     *          and <code>b</code>/.
     */
    public static Rational max(Rational a, Rational b) {
        return a.isMoreThan(b) ? a : b;
    }

    /**
     * Returns the rational with the minimum value between <code>a</code>
     * and <code>b</code>/. The following rules apply: <br />
     * <br />
     * <code>-INF &lt; N &lt; +INF &lt; NaN</code>
     *
     * @param a the first rational value.
     * @param b the second rational value.
     * @return  the rational with the minimum value between <code>a</code>
     *          and <code>b</code>/.
     */
    public static Rational min(Rational a, Rational b) {
        return a.isLessThan(b) ? a : b;
    }

    /**
     * Returns <code>this</code> - 1 as a new Rational.
     *
     * @return <code>this</code> - 1 as a new Rational.
     */
    public Rational minusOne() {
        return this.subtract(Rational.ONE);
    }

    /**
     * Returns <code>this</code> multiplied by <code>that</code> as a new Rational.
     * The following corner cases can occur:<br />
     * <code><br />
     * -Infinity * N = -Infinity <br />
     * N * -Infinity = -Infinity <br />
     * Infinity * -Infinity = -Infinity <br />
     * -Infinity * Infinity = -Infinity <br />
     * Infinity * -N = -Infinity <br />
     * <br />
     * -Infinity * -N = Infinity <br />
     * Infinity * N = Infinity <br />
     * N * Infinity = Infinity <br />
     * Infinity * Infinity = Infinity <br />
     * -Infinity * -Infinity = Infinity <br />
     * </code><br />
     * <br />
     * where N is a regular rational number.
     *
     * @param that the number to be multiplied by <code>this</code>.
     * @return <code>this</code> multiplied by <code>that</code> as a new Rational.
     */
    public Rational multiply(Rational that) {
        if (this.isNaN() || that.isNaN()) {
            return NaN;
        } else if (this.isInfinite() || that.isInfinite()) {
            if ((this.isNegative() && that.isNegative()) ||
                    (this.isPositive() && that.isPositive())) {
                return POSITIVE_INFINITY;
            } else {
                return NEGATIVE_INFINITY;
            }
        } else {
            BigInteger num = this.numerator.multiply(that.numerator);
            BigInteger den = this.denominator.multiply(that.denominator);
            return new Rational(num, den);
        }
    }

    /**
     * Returns <code>this</code> multiplied by -1 as a new Rational.
     *
     * @return <code>this</code> multiplied by -1 as a new Rational.
     */
    public Rational negate() {
        return this.multiply(MINUS_ONE);
    }

    /**
     * Normalizes this rational number:
     * -m/-n becomes m/n
     * m/-n  becomes -m/n
     * 4/8   becomes 1/2
     * 16/20 becomes 4/5
     *
     * @throws ArithmeticException thrown when denominator equals zero
     */
    private void normalize() throws ArithmeticException {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            denominator = denominator.abs();
            if (numerator.compareTo(BigInteger.ZERO) < 0) {
                numerator = numerator.abs();
            } else {
                numerator = numerator.negate();
            }
        }
        BigInteger gcd = numerator.gcd(denominator);
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
    }

    /**
     * Returns the numerator of <code>this</code> as a rational. If
     * <code>this</code> is NaN, or infinity, <code>this</code> is returned.
     * Note that if <code>this</code> is negative, the numerator will
     * also be negative. 
     *
     * @return the numerator of <code>this</code> as a rational. If
     *         <code>this</code> is NaN, or infinity, <code>this</code>
     *         is returned.
     */
    public Rational numerator() {
        if (!this.isRational()) {
            return this;
        } else {
            return new Rational(numerator, BigInteger.ONE);
        }
    }

    /**
     * Returns <code>this</code> + 1 as a new Rational.
     *
     * @return <code>this</code> + 1 as a new Rational.
     */
    public Rational plusOne() {
        return this.add(Rational.ONE);
    }

    /**
     * <p>Returns <code>this</code><sup><code>n</code></sup> as a new Rational.</p>
     * <p>The following corner cases can occur:<br />
     * <code><br />
     * NaN<sup>n</sup> = NaN <br />
     * Infinity<sup>n</sup> = Infinity <br />
     * -Infinity<sup>((N*2)+1)</sup> = -Infinity <br />
     * -Infinity<sup>(N*2)</sup> = Infinity <br />
     * </code><br />
     * </p>
     *
     * @param n the number to raise <code>this</code> to the power.
     * @return <code>this</code><sup><code>n</code></sup> as a new Rational.
     * @throws IllegalArgumentException when <code>n</code> is less than zero.
     */
    public Rational pow(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot raise '" + this +
                    "' to a negative power.");
        }

        if (this.isNaN()) {
            return NaN;
        } else if (n == 0) {
            return ONE;
        } else if (n == 1) {
            return this;
        } else if (this.isInfinite()) {
            return this.isNegative() && n % 2 == 1 ? NEGATIVE_INFINITY : POSITIVE_INFINITY;
        } else {
            return new Rational(numerator.pow(n), denominator.pow(n));
        }
    }

    /**
     * Returns <code>that</code> subtracted from <code>this</code>. The
     * following corner cases can occur:<br />
     * <code><br />
     * N - -Infinity = Infinity <br />
     * Infinity - -Infinity = Infinity <br />
     * Infinity - N = Infinity <br />
     * <br />
     * N - Infinity = -Infinity <br />
     * -Infinity - N = -Infinity <br />
     * -Infinity - Infinity = -Infinity <br />
     * <br />
     * Infinity - Infinity = NaN <br />
     * -Infinity - -Infinity = NaN <br />
     * </code><br />
     * <br />
     * where N is a regular rational number.
     *
     * @param that the number to be subtracted from <code>this</code>.
     * @return <code>that</code> subtracted from <code>this</code>.
     */
    public Rational subtract(Rational that) {
        if (this.isNaN() || that.isNaN()) {
            return NaN;
        } else if (this == that && this.isInfinite()) {
            return NaN;
        } else if (this.isInfinite() || that.isInfinite()) {
            if (that.isNegative() || (this.isPositive() && that.isRational())) {
                return POSITIVE_INFINITY;
            } else {
                return NEGATIVE_INFINITY;
            }
        } else {
            BigInteger n = this.numerator.multiply(that.denominator).subtract(
                    this.denominator.multiply(that.numerator));
            BigInteger d = this.denominator.multiply(that.denominator);
            return new Rational(n, d);
        }
    }

    /**
     * Returns <code>this</code> as a <code>BigDecimal</code> with a
     * {@link #DEFAULT_PRECISION}.
     *
     * @return <code>this</code> as a <code>BigDecimal</code> with a
     *         {@link #DEFAULT_PRECISION}.
     * @see #toBigDecimal(int)
     */
    public BigDecimal toBigDecimal() {
        return toBigDecimal(DEFAULT_PRECISION);
    }

    /**
     * Returns <code>this</code> as a <code>BigDecimal</code> with a certain
     * precision.
     *
     * @param precision the numbers of accurate digits after the decimal point.
     * @return <code>this</code> as a <code>BigDecimal</code> with a certain
     *         precision.
     * @see #toBigDecimal()
     */
    public BigDecimal toBigDecimal(int precision) {
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        return num.divide(den, new MathContext(precision));
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        if (this == POSITIVE_INFINITY) return "Infinity";
        else if (this == NEGATIVE_INFINITY) return "-Infinity";
        else if (this.isNaN()) return "NaN";
        else if (denominator.equals(BigInteger.ONE)) return numerator.toString();
        else return numerator.toString() + "/" + denominator.toString();
    }
}
