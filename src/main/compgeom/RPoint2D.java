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

/**
 * <p>
 * A class that represents a point in 2D space.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class RPoint2D implements RCGObject {

    /**
     * a point denoting the origin: (0,0)
     */
    public static final RPoint2D ORIGIN = new RPoint2D(0, 0);

    /**
     * the x coordinate
     */
    public final Rational x;

    /**
     * the y coordinate
     */
    public final Rational y;

    /**
     * a pre-calculated hash
     */
    private final int hash;

    /**
     * Creates a new point at the origin: (0,0).
     */
    public RPoint2D() {
        this(0, 0);
    }

    /**
     * Creates a new point given an x- and y coordinate as a primitive <code>long</code>.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public RPoint2D(long x, long y) {
        this(new Rational(x, 1), new Rational(y, 1));
    }

    /**
     * Creates a new point given an x- and y coordinate as a <code>Rational</code>.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @throws IllegalArgumentException if <code>x</code> or <code>y</code>
     *                                  is not a proper rational number.
     * @see Rational#isRational()
     */
    public RPoint2D(Rational x, Rational y) throws IllegalArgumentException {
        if(x.isNaN() || y.isNaN()) {
            throw new IllegalArgumentException("x or y cannot be NaN or infinite");
        }
        this.x = x;
        this.y = y;
        hash = (x.hashCode() * 23) ^ (y.hashCode() * 17);
    }

    /**
     * Calculates the approximate distance between <code>this</code> and
     * <code>that</code> point.
     *
     * @param that the other point.
     * @return the approximate distance between <code>this</code> and
     *         <code>that</code> point.
     */
    public double distance(RPoint2D that) {
        double dX = this.x.subtract(that.x).doubleValue();
        double dY = this.y.subtract(that.y).doubleValue();
        return Math.sqrt((dX * dX) + (dY * dY));
    }

    /**
     * Calculates the exact value of <code>(this.x - that.x)<sup>2</sup> +
     * (this.y - that.y)<sup>2</sup></code>.
     *
     * @param that the other point.
     * @return the exact value of <code>(this.x - that.x)<sup>2</sup> +
     *         (this.y - that.y)<sup>2</sup></code>.
     */
    public Rational distanceSquared(RPoint2D that) {
        Rational dX = this.x.subtract(that.x);
        Rational dY = this.y.subtract(that.y);
        return dX.multiply(dX).add(dY.multiply(dY));
    }

    /**
     * Calculate the exact distance between  <code>this</code> and
     * <code>that</code> point denoted by:
     * <code>|this.x-that.x| + |this.y-that.y| + 1</code>.
     *
     * @param that the other point.
     * @return the exact distance between  <code>this</code> and
     *         <code>that</code> point denoted by:
     *         <code>|this.x-that.x| + |this.y-that.y|</code>.
     */
    public Rational distanceXY(RPoint2D that) {
        Rational dX = this.x.subtract(that.x).abs();
        Rational dY = this.y.subtract(that.y).abs();
        return dX.add(dY);
    }

    /**
     * Returns <code>true</code> iff <code>o</code> is a <code>RPoint2D</code>
     * and if <code>this.x</code> equals <code>((RPoint2D)o).x</code> and
     * <code>this.y</code> equals <code>((RPoint2D)o).y</code>.
     *
     * @param o the other Object.
     * @return <code>true</code> iff <code>o</code> is a <code>RPoint2D</code>
     *         and if <code>this.x</code> equals <code>((RPoint2D)o).x</code> and
     *         <code>this.y</code> equals <code>((RPoint2D)o).y</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPoint2D that = (RPoint2D) o;
        return this.x.equals(that.x) && this.y.equals(that.y);
    }

    /**
     * Returns a pre-calculated hash code, calculated as follows:
     * <code>(x.hashCode()*23) ^ (y.hashCode()*17)</code>.
     *
     * @return a pre-calculated hash code, calculated as follows:
     *         <code>(x.hashCode()*23) ^ (y.hashCode()*17)</code>.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Returns <code>true</code> iff <code>this</code> point is above
     * <code>that</code> point. <code>this</code> is considered to be
     * above <code>that</code> when: <code>this.y &gt; that.y</code>.
     *
     * @param that the other point.
     * @return <code>true</code> iff <code>this</code> point is above
     *         <code>that</code> point.
     */
    public boolean isAbove(RPoint2D that) {
        return this.y.isMoreThan(that.y);
    }

    /**
     * Returns <code>true</code> iff <code>this</code> point is below
     * <code>that</code> point. <code>this</code> is considered to be
     * below <code>that</code> when: <code>this.y &lt; that.y</code>.
     *
     * @param that the other point.
     * @return <code>true</code> iff <code>this</code> point is below
     *         <code>that</code> point.
     */
    public boolean isBelow(RPoint2D that) {
        return this.y.isLessThan(that.y);
    }

    /**
     * Returns <code>true</code> if <code>this</code> point lies on the left of
     * a given <code>line</code>. If the <code>line</code> is horizontal, this
     * method returns <code>true</code> if <code>this</code> point lies above the
     * point where <code>line</code> intersects the y axis. Note that this method
     * returns <code>false</code> if <code>this</code> point lies on the
     * <code>line</code>.
     *
     * @param line the line.
     * @return <code>true</code> if <code>this</code> point lies on the left of
     *         a given <code>line</code>.
     */
    public boolean isLeftOf(RLine2D line) {
        return !line.contains(this) && !this.isRightOf(line);
    }

    /**
     * Returns <code>true</code> iff <code>this</code> point is to the left
     * of <code>that</code> point. <code>this</code> is considered to be to
     * the left of <code>that</code> when: <code>this.x &lt; that.x</code>.
     *
     * @param that the other point.
     * @return <code>true</code> iff <code>this</code> point is to the left of
     *         <code>that</code> point.
     */
    public boolean isLeftOf(RPoint2D that) {
        return this.x.isLessThan(that.x);
    }

    /**
     * Returns <code>true</code> if <code>this</code> point lies on the right of
     * a given <code>line</code>. If the <code>line</code> is horizontal, this
     * method returns <code>true</code> if <code>this</code> point lies below the
     * point where <code>line</code> intersects the y axis. Note that this method
     * returns <code>false</code> if <code>this</code> point lies on the
     * <code>line</code>.
     *
     * @param line the line.
     * @return <code>true</code> if <code>this</code> point lies on the right of
     *         a given <code>line</code>.
     */
    public boolean isRightOf(RLine2D line) {
        if(line.contains(this)) {
            return false;
        }
        if(line.isHorizontal()) {
            return this.y.isLessThan(line.yIntercept());
        }
        if(line.isVertical()) {
            return this.x.isMoreThan(line.xIntercept());
        }

        RPoint2D linePoint = line.intersection(RLine2D.vertical(Rational.ZERO));
        RLine2D temp = new RLine2D(this, linePoint);

        if(this.y.isLessThan(linePoint.y)) {
            // a
            if(line.slope.isNegative()) {
                return temp.slope.isNegative() && temp.slope.isMoreThan(line.slope);
            }
            // b
            else {
                return temp.slope.isNegative() || temp.slope.isMoreThan(line.slope);
            }
        } else if(this.y.isMoreThan(linePoint.y)) {
            // c
            if(line.slope.isNegative()) {
                return temp.slope.isPositive() || temp.slope.isLessThan(line.slope);
            }
            // d
            else {
                return temp.slope.isPositive() && temp.slope.isLessThan(line.slope);
            }
        } else {
            // 'this' has the same y coordinate as the 'linePoint'
            return this.isRightOf(linePoint);
        }
    }

    /**
     * Returns <code>true</code> iff <code>this</code> point is to the right
     * of <code>that</code> point. <code>this</code> is considered to be to
     * the right of <code>that</code> when: <code>this.x &gt; that.x</code>.
     *
     * @param that the other point.
     * @return <code>true</code> iff <code>this</code> point is to the left of
     *         <code>that</code> point.
     */
    public boolean isRightOf(RPoint2D that) {
        return this.x.isMoreThan(that.x);
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        return String.format("(%s,%s)", x, y);
    }

    /**
     * Returns a new point with coordinate: <code>(this.x + dx, this.y + dy)</code>.
     *
     * @param dx delta x.
     * @param dy delta y.
     * @return a new point with coordinate: <code>(this.x + dx, this.y + dy)</code>.
     */
    public RPoint2D translate(long dx, long dy) {
        return translate(new Rational(dx), new Rational(dy));
    }

    /**
     * Returns a new point with coordinate: <code>(this.x + dx, this.y + dy)</code>.
     *
     * @param dx delta x.
     * @param dy delta y.
     * @return a new point with coordinate: <code>(this.x + dx, this.y + dy)</code>.
     */
    public RPoint2D translate(Rational dx, Rational dy) {
        return new RPoint2D(this.x.add(dx), this.y.add(dy));
    }
}
