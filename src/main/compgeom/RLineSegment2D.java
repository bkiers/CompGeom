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

import compgeom.util.Extremal;

/**
 * <p>
 * A class that represents a line segment in 2D space.
 * </p>
 * <p>
 * The point with the lowest x coordinate is <code>{@link #p1}</code>,
 * the other <code>{@link #p2}</code>:
 * </p>
 * <p>
 * <pre>
 *         p2    p1
 *       /         \
 *     /            \
 *   /               p2
 * p1
 * </pre>
 * </p>
 * <p>
 * In case both points have equal x coordinates, the point with
 * the lowest y coordinate is <code>{@link #p1}</code>,
 * the other <code>{@link #p2}</code>:
 * </p>
 * <p>
 * <pre>
 *         p2
 *         |
 *         |
 *         |
 *         p1
 * </pre>
 * </p>
 *
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class RLineSegment2D implements RCGObject {

    /**
     * the line that can be formed by going through {@link #p1} and {@link #p2}
     */
    public final RLine2D line;

    /**
     * the point with the smallest x, or in case of a tie, with the smallest y
     */
    public final RPoint2D p1;

    /**
     * the point with the highest x, or in case of a tie, with the highest y
     */
    public final RPoint2D p2;

    /**
     * maximum x value of the bounding box aligned with the x- and y-axis
     */
    public final Rational maxX;

    /**
     * maximum y value of the bounding box aligned with the x- and y-axis
     */
    public final Rational maxY;

    /**
     * minimum x value of the bounding box aligned with the x- and y-axis
     */
    public final Rational minX;

    /**
     * minimum y value of the bounding box aligned with the x- and y-axis
     */
    public final Rational minY;

    /**
     * a pre-calculated hash
     */
    private final int hash;

    /**
     * Creates a new line segment from the two points <code>pA</code> and <code>pB</code>.
     * <code>pA</code> will be the left-most point ({@link #p1}), and <code>pB</code>
     * will be the right-most point ({@link #p2}).
     *
     * @param pA the first point.
     * @param pB the second point.
     * @throws IllegalArgumentException thrown when <code>pA</code> and
     *                                  <code>pB</code> are equal.
     */
    public RLineSegment2D(RPoint2D pA, RPoint2D pB) throws IllegalArgumentException {
        if (pA.equals(pB)) {
            throw new IllegalArgumentException("cannot create line segment: p1 equals p2, both are "+pA);
        }
        p1 = Extremal.LEFT_LOWER.moreExtremeThan(pA, pB) ? pA : pB;
        p2 = p1 == pA ? pB : pA;
        maxX = p1.x.isMoreThan(p2.x) ? p1.x : p2.x;
        maxY = p1.y.isMoreThan(p2.y) ? p1.y : p2.y;
        minX = p1.x.isLessThan(p2.x) ? p1.x : p2.x;
        minY = p1.y.isLessThan(p2.y) ? p1.y : p2.y;
        hash = (p1.hashCode() * 13) ^ (p2.hashCode() * 37);
        line = new RLine2D(p1, p2);
    }

    /**
     * Returns the point that divides this line segment in two equally sized pieces.
     *
     * @return the point that divides this line segment in two equally sized pieces.
     */
    public RPoint2D center() {
        Rational dX = this.p1.x.subtract(this.p2.x).abs();
        Rational dY = this.p1.y.subtract(this.p2.y).abs();
        Rational two = new Rational(2);
        return new RPoint2D(p1.x.add(dX.divide(two)), p1.y.add(dY.divide(two)));
    }

    /**
     * Returns <code>true</code> iff <code>p</code> lies on this segment. Note
     * that if <code>p</code> equals <code>{@link #p1}</code> or <code>{@link #p2}</code>,
     * it is also considered to lie on this segment.
     *
     * @param p the point to check if it intersects with <code>this</code>.
     * @return <code>true</code> iff <code>p</code> lies on this segment. Note
     *         that if <code>p</code> equals <code>this.p1</code> or <code>this.p2</code>,
     *         it is also considered to lie on this segment.
     */
    public boolean contains(RPoint2D p) {
        return (p1.equals(p) || p2.equals(p))
                ||
                (line.contains(p) &&
                        (p.x.isMoreThanEq(minX) && p.x.isLessThanEq(maxX) &&
                                p.y.isMoreThanEq(minY) && p.y.isLessThanEq(maxY)));
    }

    /**
     * Returns <code>true</code> iff <code>o</code> is a <code>RLineSegment2D</code>
     * and if <code>{@link #p1}</code> equals <code>((RLineSegment2D)o).p1</code> and
     * <code>{@link #p2}</code> equals <code>((RLineSegment2D)o).p2</code>.
     *
     * @param o the other Object.
     * @return <code>true</code> iff <code>o</code> is a <code>RLineSegment2D</code>
     *         and if <code>this.p1</code> equals <code>((RLineSegment2D)o).p1</code> and
     *         <code>this.p2</code> equals <code>((RLineSegment2D)o).p2</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RLineSegment2D that = (RLineSegment2D) o;
        return this.p1.equals(that.p1) && this.p2.equals(that.p2);
    }

    /**
     * Returns <code>true</code> if <code>p</code> equals either
     * <code>{@link RLineSegment2D#p1}</code> or
     * <code>{@link RLineSegment2D#p2}</code>.
     *
     * @param p the point.
     * @return <code>true</code> if <code>p</code> equals either
     *         <code>{@link RLineSegment2D#p1}</code> or
     *         <code>{@link RLineSegment2D#p2}</code>.
     */
    public boolean hasEnding(RPoint2D p) {
        return p1.equals(p) || p2.equals(p);
    }

    /**
     * Returns a pre-calculated hash code, calculated as follows:
     * <code>(p1.hashCode()*13) ^ (p2.hashCode()*37)</code>.
     *
     * @return a pre-calculated hash code, calculated as follows:
     *         <code>(p1.hashCode()*13) ^ (p2.hashCode()*37)</code>.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * <p>
     * Finds the proper intersection point of two line segments <code>this</code>
     * and <code>line</code>. If no intersection exists <code>null</code>
     * is returned. A proper intersection exists if the two segments share
     * exactly one point, and this point lies in the interior of both line
     * segments. For example, take the segments:
     * </p>
     * <ol>
     * <li><code>(1,1) -- (3,3)</code></li>
     * <li><code>(2,2) -- (4,4)</code></li>
     * <li><code>(2,2) -- (3,2)</code></li>
     * <li><code>(3,3) -- (4,4)</code></li>
     * </ol>
     * <p>
     * The segments <b>1</b> and <b>2</b> do not intersect (they have an
     * infinite amount of intersection points between <code>(2,2) -- (3,3)
     * </code>), but the segments <b>1</b> and <b>3</b> intersect at point
     * <code>(2,2)</code>, just like the segments <b>2</b> and <b>3</b>. And
     * the segments <b>1</b> and <b>4</b> intersect at <code>(3,3)</code>.
     * </p>
     *
     * @param that the <code>RLineSegment2D</code>.
     * @return the intersection point of two <code>RLineSegment2D</code>'s:
     *         <code>this</code> and <code>that</code>.
     */
    public RPoint2D intersection(RLineSegment2D that) {
        if (this.p1.equals(that.p2)) {
            return this.p1;
        } else if (this.p2.equals(that.p1)) {
            return this.p2;
        } else if(this.p1.equals(that.p1)) {
            return this.line.slope.equals(that.line.slope) ? null : this.p1;
        } else if(this.p2.equals(that.p2)) {
            return this.line.slope.equals(that.line.slope) ? null : this.p2;
        } else {
            RPoint2D p = this.line.intersection(that.line);
            if (p == null || !this.contains(p) || !that.contains(p)) {
                return null;
            } else {
                return p;
            }
        }
    }

    /**
     * <p>
     * Finds the intersection point of <code>this</code> line segment
     * and a <code>line</code>. If no intersection exists, <code>null</code>
     * is returned.
     * </p>
     *
     * @param line the <code>{@link RLine2D}</code>.
     * @return the intersection point of <code>this</code> segment and a
     *         <code>line</code>.
     */
    public RPoint2D intersection(RLine2D line) {
        RPoint2D p = this.line.intersection(line);
        if (p == null || !this.contains(p)) {
            return null;
        } else {
            return p;
        }
    }

    /**
     * <p>
     * Returns <code>true</code> if <code>this</code> and
     * <code>that</code> intersect properly.
     * </p>
     * <p>
     * A proper intersection exists if the two segments share
     * exactly one point, and this point lies in the interior of both line
     * segments. For example, take the segments:
     * </p>
     * <ol>
     * <li><code>(1,1) -- (3,3)</code></li>
     * <li><code>(2,2) -- (4,4)</code></li>
     * <li><code>(2,2) -- (3,2)</code></li>
     * <li><code>(3,3) -- (4,4)</code></li>
     * </ol>
     * <p>
     * The segments <b>1</b> and <b>2</b> do not intersect (they have an
     * infinite amount of intersection points between <code>(2,2) -- (3,3)
     * </code>), but the segments <b>1</b> and <b>3</b> intersect at point
     * <code>(2,2)</code>, just like the segments <b>2</b> and <b>3</b>. And
     * the segments <b>1</b> and <b>4</b> intersect at <code>(3,3)</code>.
     * </p>
     *
     * @param that the other <code>RLineSegment2D</code>.
     * @return <code>true</code> if <code>this</code> and
     *         <code>that</code> intersect.
     */
    public boolean intersects(RLineSegment2D that) {
        return intersection(that) != null;
    }

    /**
     * <p>
     * Returns <code>true</code> if <code>this</code> and
     * <code>line</code> intersect.
     * </p>
     *
     * @param line the <code>RLine2D</code> to check.
     * @return <code>true</code> if <code>this</code> and
     *         <code>line</code> intersect.
     */
    public boolean intersects(RLine2D line) {
        return intersection(line) != null;
    }

    /**
     * Returns <code>true</code> iff <code>this</code> and <code>that</code>
     * are equal in length. Note that this method does not suffer from imprecise
     * floating point arithmetic since {@link #lengthSquared()} is used to do
     * the comparison, which is backed up by the {@link Rational} class.
     *
     * @param that the other segment.
     * @return <code>true</code> iff <code>this</code> and <code>that</code>
     *         are equal in length.
     * @see #lengthSquared()
     */
    public boolean isEqualLength(RLineSegment2D that) {
        return this.lengthSquared().equals(that.lengthSquared());
    }

    /**
     * Returns <code>true</code> iff <code>this</code>' length is more than
     * <code>that</code>'s length. Note that this method does not suffer from imprecise
     * floating point arithmetic since {@link #lengthSquared()} is used to do
     * the comparison, which is backed up by the {@link Rational} class.
     *
     * @param that the other segment.
     * @return <code>true</code> iff <code>this</code>' length is more than
     *         <code>that</code>'s length.
     * @see #lengthSquared()
     */
    public boolean isLongerThan(RLineSegment2D that) {
        return this.lengthSquared().isMoreThan(that.lengthSquared());
    }

    /**
     * Returns <code>true</code> iff <code>this</code>' length is less than
     * <code>that</code>'s length. Note that this method does not suffer from imprecise
     * floating point arithmetic since {@link #lengthSquared()} is used to do
     * the comparison, which is backed up by the {@link Rational} class.
     *
     * @param that the other segment.
     * @return <code>true</code> iff <code>this</code>' length is less than
     *         <code>that</code>'s length.
     * @see #lengthSquared()
     */
    public boolean isShorterThan(RLineSegment2D that) {
        return this.lengthSquared().isLessThan(that.lengthSquared());
    }

    /**
     * Calculates the approximate length of this line segment.
     *
     * @return the approximate length of this line segment as
     *         a <code>double</code>.
     */
    public double length() {
        return this.p1.distance(this.p2);
    }

    /**
     * Calculates the exact value of <code>(this.p1.x - this.p2.x)<sup>2</sup> +
     * (this.p1.y - this.p2.y)<sup>2</sup></code>.
     *
     * @return the exact value of <code>(this.p1.x - this.p2.x)<sup>2</sup> +
     *         (this.p1.y - this.p2.y)<sup>2</sup></code>.
     */
    public Rational lengthSquared() {
        return this.p1.distanceSquared(this.p2);
    }

    /**
     * The exact delta x + delta y between <code>{@link #p1}</code> and
     * <code>{@link #p2}</code>.
     *
     * @return the exact delta x + delta y between
     *         <code>this.p1</code> and <code>this.p2</code>.
     */
    public Rational lengthXY() {
        return this.p1.distanceXY(this.p2);
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        return String.format("[%s~%s]", p1, p2);
    }
}