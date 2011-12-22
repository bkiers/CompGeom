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

import compgeom.util.parser.LineParser;

/**
 * <p>
 * A class that represents a line in 2D space.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class RLine2D implements RCGObject {

    /**
     * the slope of this line.
     */
    public final Rational slope;

    /**
     * the constant of this line.
     */
    public final Rational constant;

    /**
     * a pre-calculated hash code.
     */
    private final int hash;

    /**
     * Creates a new line based on a given <code>slope</code> and
     * <code>constant</code>.
     *
     * @param slope    the slope of the line.
     * @param constant the intersection with the y axis, except
     *                 when <code>slope</code> is {@link Rational#isInfinite()}:
     *                 then it will represent the intersection
     *                 with the x axis.
     * @throws IllegalArgumentException thrown when <code>slope</slope> is
     *                                  {@link Rational#isNaN()}
     */
    public RLine2D(Rational slope, Rational constant) throws IllegalArgumentException {
        if (slope.isNaN() || constant.isNaN()) {
            throw new IllegalArgumentException("The slope and constant cannot be NaN");
        }
        this.slope = slope;
        this.constant = constant;
        this.hash = (this.slope.hashCode() * 23) ^ (this.constant.hashCode() * 37);
    }

    /**
     * Creates a new line based on a given <code>slope</code> and a point
     * <code>p</code> in the plane through which this line runs.
     *
     * @param slope the slope of the line.
     * @param p     a point in the plane through which this line runs.
     * @throws IllegalArgumentException thrown when <code>slope</slope> is
     *                                  {@link Rational#isNaN()}
     */
    public RLine2D(Rational slope, RPoint2D p) throws IllegalArgumentException {
        if (slope.isNaN()) {
            throw new IllegalArgumentException("The slope cannot be NaN");
        }
        this.slope = slope.isInfinite() ? Rational.POSITIVE_INFINITY : slope;
        this.constant = calculateConstant(p);
        this.hash = (this.slope.hashCode() * 23) ^ (constant.hashCode() * 37);
    }

    /**
     * Creates a new line based on a two points through which the line runs.
     *
     * @param p1 the first point through which this line runs.
     * @param p2 the second point through which this line runs.
     * @throws IllegalArgumentException when <code>p1</code> equals <code>p2</code>.
     */
    public RLine2D(RPoint2D p1, RPoint2D p2) {
        if (p1.equals(p2)) {
            throw new IllegalArgumentException("cannot create a line: p1 equals p2");
        }
        this.slope = calculateSlope(p1, p2);
        this.constant = calculateConstant(p1);
        this.hash = calculateHash();
    }

    /**
     * <p>
     * Creates a line based on a linear function. Some examples of valid functions are:
     * </p>
     * <ol>
     * <li><code>"f(x) -> 3/1x + 1/2"</code></li>
     * <li><code>"y = 3.0*x + -1/-2"</code> (the same as #1)</li>
     * <li><code>"f(x) -> 3*x"</code></li>
     * <li><code>"f(x) -> 0*x + 0"</code></li>
     * <li><code>"f(x) -> 6/2"</code></li>
     * <li><code>"f(x) -> 0x + 1/2"</code></li>
     * <li><code>"y = 1/2"</code> (the same as #6)</li>
     * <li><code>"x = -0.01"</code></li>
     * </ol>
     * <p>
     * The formal grammar of valid linear functions follows in
     * <a href="http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form">EBNF</a> notation:
     * </p>
     * <pre>
     * function
     *   =  "f" "(" "x" ")" "->" rhs
     *   |  "y" "=" rhs
     *   |  "x" "=" number
     *   ;
     *
     * rhs
     *   =  slope constant
     *   |  slope
     *   |  number
     *   ;
     *
     * slope
     *   =  [number ["*"]] "x"
     *   ;
     *
     * constant
     *   =  "+" number
     *   |  "-" number
     *   ;
     *
     * number
     *   =  decimal
     *   |  rational
     *   |  integer
     *   ;
     *
     * rational
     *   =  integer "/" integer
     *   ;
     *
     * decimal
     *   =  integer "." digits
     *   ;
     *
     * integer
     *   =  ["-"] digits
     *   ;
     *
     * digits
     *   =  digit {digit}
     *   ;
     *
     * digit
     *   =  "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
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
     * White spaces between the tokens inside the <code>function</code> literal are
     * ignored and the functions are case insensitively parsed. In other words,
     * the following <code>function</code>'s are the same:
     * <code>"f(x)->x"</code> and <code>"F( X ) -> X"</code>, but this function is
     * invalid: <code>"f(x) - > x"</code> (<code>-></code> is a token and can
     * therefor not contain a white space in the middle).
     * </p>
     * <p>
     * <a href="http://www.antlr.org">ANTLR</a> was used to generate a lexer
     * and parser to parse the <code>function</code>.
     * </p>
     *
     * @param function the linear function.
     * @throws IllegalArgumentException if <code>function</code> is invalid.
     */
    public RLine2D(String function) throws IllegalArgumentException {
        if (function.isEmpty()) {
            throw new IllegalArgumentException("invalid function: '" + function + "'");
        }
        RLine2D line = LineParser.parse(function);
        slope = line.slope;
        constant = line.constant;
        hash = calculateHash();
    }

    /**
     * Returns the degrees between this line and the x axis. Note that
     * the value being returned is always between the 0.0 and
     * 180.0 (both inclusive). If 180.0 is returned, the line might not
     * actually be horizontal, but just very close to it. This is due to
     * the inaccuracy of the floating point primitive <code>double</code>.
     *
     * @return the degrees between this line and the x axis.
     * @see #angle(boolean)
     */
    public double angle() {
        return angle(true);
    }

    /**
     * Returns the angle between this line and the x axis. The angle
     * is in degrees when <code>asDegrees</code> is <code>true</code>
     * else in radians. Note that the value being returned is always
     * between the 0.0 and 180.0 (both inclusive) when
     * <code>asDegrees</code> is <code>true</code>, else the value
     * is between 0.0 and <code>{@link java.lang.Math#PI}</code>
     * (both inclusive). If 180.0 (or <code>{@link java.lang.Math#PI}</code>)
     * is returned, the line might not actually be horizontal, but just very
     * close to it. This is due to the inaccuracy of the floating point
     * primitive <code>double</code>.
     *
     * @param asDegrees if <code>true</code>, the angle is returned in
     *                  degrees else in radians.
     * @return the angle between this line and the x axis either
     *         in degrees, or in radians, depending on
     *         <code>asDegrees</code>.
     */
    public double angle(boolean asDegrees) {
        double radians = Math.atan(this.slope.doubleValue());
        if (asDegrees) {
            double degrees = Math.toDegrees(radians);
            return degrees < 0.0 ? 180.0 + degrees : degrees;
        }
        return radians < 0.0 ? Math.PI + radians : radians;
    }

    /**
     * Calculates the constant of this line: <code>constant = (-slope * p.x) + p.y</code>.
     *
     * @param p a point through which this line runs.
     * @return the constant of this line: <code>constant = (-slope * x) + y</code>.
     */
    private Rational calculateConstant(RPoint2D p) {
        if (this.isVertical()) {
            return p.x;
        } else {
            return Rational.MINUS_ONE.multiply(slope).multiply(p.x).add(p.y);
        }
    }

    /**
     * Calculates a hash code based on the slope and constant of this line.
     *
     * @return a hash code based on the slope and constant of this line.
     */
    private int calculateHash() {
        return (slope.hashCode() * 23) ^ (constant.hashCode() * 37);
    }

    /**
     * Calculates the slope of this line: <code>this.slope = (p2.y - p1.y) / (p2.x - p1.x)</code>.
     *
     * @param p1 the first point through which this line runs.
     * @param p2 the second point through which this line runs.
     * @return the slope of this line: <code>this.slope = (p2.y - p1.y) / (p2.x - p1.x)</code>.
     */
    private Rational calculateSlope(RPoint2D p1, RPoint2D p2) {
        if (p1.x.equals(p2.x)) {
            return Rational.POSITIVE_INFINITY;
        } else {
            return p2.y.subtract(p1.y).divide(p2.x.subtract(p1.x));
        }
    }

    /**
     * Returns <code>true</code> iff <code>p</code> lies on this line.
     *
     * @param p the point to check if it intersects with <code>this</code> line.
     * @return <code>true</code> iff <code>p</code> lies on this line.
     */
    public boolean contains(RPoint2D p) {
        if (this.isVertical()) {
            return this.constant.equals(p.x);
        } else {
            return this.slope.multiply(p.x).add(this.constant).equals(p.y);
        }
    }

    /**
     * Returns <code>true</code> iff <code>o</code> is a <code>RLine2D</code>
     * and if <code>this.slope</code> equals <code>that.slope</code> and
     * <code>this.constant</code> equals <code>that.constant</code>.
     *
     * @param o the other line.
     * @return <code>true</code> iff <code>o</code> is a <code>RLine2D</code>
     *         and if <code>this.slope</code> equals <code>that.slope</code> and
     *         <code>this.constant</code> equals <code>that.constant</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RLine2D that = (RLine2D) o;
        return this.slope.equals(that.slope) && this.constant.equals(that.constant);
    }

    /**
     * Returns a pre-calculated hash code:
     * <code>(slope.hashCode()*23) ^ (constant.hashCode()*37)</code>.
     *
     * @return a pre-calculated hash code:
     *         <code>(slope.hashCode()*23) ^ (constant.hashCode()*37)</code>.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Returns a horizontal line that passes the y-axis through <code>y</code>.
     *
     * @param y the value to which to go through the y-axis.
     * @return a horizontal line that passes the y-axis through <code>y</code>.
     */
    public static RLine2D horizontal(Rational y) {
        RPoint2D p = new RPoint2D(Rational.ZERO, y);
        return new RLine2D(p, p.translate(1, 0));
    }

    /**
     * Finds the intersection point of two lines <code>this</code>
     * and <code>that</code>, if it exists.
     *
     * @param that the other line.
     * @return the intersection point of two lines <code>this</code>
     *         and <code>that</code>. If no intersection exists
     *         <code>null</code> is returned.
     */
    public RPoint2D intersection(RLine2D that) {
        if (this.slope.equals(that.slope)) {
            return null;
        }
        Rational xInt = xIntercept(that);
        if (this.isVertical()) {
            return new RPoint2D(xInt, that.yIntercept(xInt));
        }
        return new RPoint2D(xInt, yIntercept(xInt));
    }

    /**
     * Finds the intersection point of this line and a given line segment,
     * if it exists.
     *
     * @param segment the line segment.
     * @return the intersection point of this line and a given line segment,
     *         if it exists.
     */
    public RPoint2D intersection(RLineSegment2D segment) {
        return segment.intersection(this);
    }

    /**
     * Returns <code>true</code> iff <code>this</code> and <code>that</code>
     * line intersect. They intersect is their slopes are not equal.
     *
     * @param that the other line.
     * @return <code>true</code> iff <code>this</code> and <code>that</code>
     *         line intersect.
     */
    public boolean intersects(RLine2D that) {
        return !this.slope.equals(that.slope);
    }

    /**
     * Returns <code>true</code> iff <code>this</code> and <code>that</code> line
     * segment intersect.
     *
     * @param that the line segment.
     * @return <code>true</code> iff <code>this</code> and <code>that</code> line
     *         segment intersect.
     */
    public boolean intersects(RLineSegment2D that) {
        return that.intersects(this);
    }

    /**
     * Returns <code>true</code> if this line is vertical or horizontal.
     *
     * @return <code>true</code> if this line is vertical or horizontal.
     */
    public boolean isAxisAligned() {
        return isHorizontal() || isVertical();
    }

    /**
     * Returns <code>true</code> if the line is horizontal.
     *
     * @return <code>true</code> if the line is horizontal.
     */
    public boolean isHorizontal() {
        return slope.equals(Rational.ZERO);
    }

    /**
     * Returns <code>true</code> iff <code>this</code> line and
     * <code>that</code> line are parallel to each other.
     *
     * @param that the other line.
     * @return <code>true</code> iff <code>this</code> line and
     *         <code>that</code> line are parallel to each other.
     */
    public boolean isParallelTo(RLine2D that) {
        return this.slope.equals(that.slope);
    }

    /**
     * Returns <code>true</code> iff <code>this</code> line and
     * <code>that</code> line are perpendicular to each other.
     *
     * @param that the other line.
     * @return <code>true</code> iff <code>this</code> line and
     *         <code>that</code> line are perpendicular to each
     *         other.
     */
    public boolean isPerpendicularTo(RLine2D that) {
        if (this.isVertical()) {
            return that.isHorizontal();
        } else if (that.isVertical()) {
            return this.isHorizontal();
        } else {
            return this.slope.multiply(that.slope).equals(Rational.MINUS_ONE);
        }
    }

    /**
     * Returns <code>true</code> if the line is vertical.
     *
     * @return <code>true</code> if the line is vertical.
     */
    public boolean isVertical() {
        return slope.isInfinite();
    }

    /**
     * Returns a new line perpendicular to <code>this</code> going
     * through a given point <code>p</code>.
     *
     * @param p the point through which the perpendicular line must go.
     * @return a new line perpendicular to <code>this</code> going
     *         through a given point <code>p</code>.
     */
    public RLine2D perpendicularLine(RPoint2D p) {
        if (this.isHorizontal()) {
            return RLine2D.vertical(p.x);
        } else if (this.isVertical()) {
            return RLine2D.horizontal(p.y);
        } else {
            Rational newSlope = Rational.MINUS_ONE.divide(this.slope);
            return new RLine2D(newSlope, p);
        }
    }

    /**
     * Calculates the tangent of the degrees, <code>theta</code>, between two lines
     * with gradients <code>m1</code> (<code>this.slope</code>) and <code>m2</code>
     * (<code>that.slope</code>): <code>tan(theta) = (m1 - m2)/(1 + (m1*m2))</code>.
     *
     * @param that the other line.
     * @return the tangent of the degrees, <code>theta</code>, between two lines
     *         with gradients <code>m1</code> (<code>this.slope</code>) and <code>m2</code>
     *         (<code>that.slope</code>): <code>tan(theta) = (m1 - m2)/(1 + (m1*m2))</code>.
     */
    public Rational tangent(RLine2D that) {
        if (this.isParallelTo(that)) {
            return Rational.ZERO;
        } else if (this.isVertical()) {
            if (that.isHorizontal()) {
                return Rational.MINUS_ONE;
            } else {
                return Rational.ONE.divide(that.slope);
            }
        } else if (that.isVertical()) {
            if (that.isHorizontal()) {
                return Rational.MINUS_ONE;
            } else {
                return this.slope;
            }
        } else {
            if (this.isPerpendicularTo(that)) {
                return Rational.MINUS_ONE;
            } else {
                return this.slope.subtract(that.slope).divide(
                        Rational.ONE.add(this.slope.multiply(that.slope)));
            }
        }
    }

    /**
     * Returns a vertical line that passes the x-axis through <code>x</code>.
     *
     * @param x the value to which to go through the x-axis.
     * @return a vertical line that passes the x-axis through <code>x</code>.
     */
    public static RLine2D vertical(Rational x) {
        RPoint2D p = new RPoint2D(x, Rational.ZERO);
        return new RLine2D(p, p.translate(0, 1));
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        if (this.isVertical()) {
            return "x = " + constant;
        }
        StringBuilder builder = new StringBuilder("f(x) -> ");
        // slope
        if (slope.equals(Rational.MINUS_ONE)) {
            builder.append("-x");
        } else if (slope.equals(Rational.ONE)) {
            builder.append("x");
        } else if (!slope.equals(Rational.ZERO)) {
            builder.append(slope).append("*x");
        }
        // constant
        if (!constant.equals(Rational.ZERO)) {
            if (this.isVertical() || this.isHorizontal()) {
                builder.append(constant);
            } else if (constant.isPositive()) {
                builder.append(" + ").append(constant);
            } else {
                builder.append(" - ").append(constant.abs());
            }
        } else if (this.isHorizontal()) {
            builder.append("0");
        }

        return builder.toString();
    }
    
    /**
     * Calculates the x-intercept point of this line: <code>-constant / slope</code>.
     *
     * @return the x-intercept point of this line: <code>-constant / slope</code>, or
     *         <code>null</code> if the slope of this line equals 0
     *         (a horizontal line).
     */
    public Rational xIntercept() {
        if (this.isHorizontal()) {
            return null;
        } else if (this.isVertical()) {
            return constant;
        } else {
            return constant.negate().divide(slope);
        }
    }

    /**
     * Calculates the x-intercept between two lines, y = slope*x + constant:
     * <code>((b2 - b1) / (m1 - m2))</code>.
     *
     * @param that the other line.
     * @return the x-intercept between two lines: <code>((b2 - b1) / (m1 - m2))</code>.
     */
    private Rational xIntercept(RLine2D that) {
        if (this.isVertical()) {
            return this.constant;
        } else if (that.isVertical()) {
            return that.constant;
        } else {
            return that.constant.subtract(this.constant).divide(this.slope.subtract(that.slope));
        }
    }

    /**
     * Calculates the y-intercept of this line.
     *
     * @return the y-intercept of this line, or <code>null</code>
     *         if the slope of this line is infinite (a vertical line).
     */
    public Rational yIntercept() {
        if (this.isVertical()) {
            return null;
        } else {
            return constant;
        }
    }

    /**
     * Calculates the y-intercept of this line, given the x-intercept,
     * <code>xInt</code>: <code>((slope*xInt) + constant)</code>.
     *
     * @param xInt the x-intercept.
     * @return the y-intercept of this line, given the x-intercept,
     *         <code>xInt</code>: <code>((slope*xInt) + constant)</code>.
     */
    private Rational yIntercept(Rational xInt) {
        return this.slope.multiply(xInt).add(this.constant);
    }
}
