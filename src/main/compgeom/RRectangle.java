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

import java.util.*;

/**
 * <p>
 * A class that represents a rectangle.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class RRectangle implements RBoundSurface2D {

    /**
     * the first point of this rectangle
     */
    public final RPoint2D p1;

    /**
     * the second point of this rectangle
     */
    public final RPoint2D p2;

    /**
     * the third point of this rectangle
     */
    public final RPoint2D p3;

    /**
     * the last point of this rectangle
     */
    public final RPoint2D p4;

    /**
     * the line segment formed by points: {@link RRectangle#p1} <code>-></code> {@link RRectangle#p2},
     * parallel to {@link RRectangle#s3}
     */
    public final RLineSegment2D s1;

    /**
     * the line segment formed by points: {@link RRectangle#p2} <code>-></code> {@link RRectangle#p3},
     * parallel to {@link RRectangle#s4}
     */
    public final RLineSegment2D s2;

    /**
     * the line segment formed by points: {@link RRectangle#p3} <code>-></code> {@link RRectangle#p4},
     * parallel to {@link RRectangle#s1}
     */
    public final RLineSegment2D s3;

    /**
     * the line segment formed by points: {@link RRectangle#p4} <code>-></code> {@link RRectangle#p1},
     * parallel to {@link RRectangle#s2}
     */
    public final RLineSegment2D s4;

    /**
     * maximum x value of the bounding box aligned with the x- and y-axis
     */
    private Rational maxX;

    /**
     * maximum y value of the bounding box aligned with the x- and y-axis
     */
    private Rational maxY;

    /**
     * minimum x value of the bounding box aligned with the x- and y-axis
     */
    private Rational minX;

    /**
     * minimum y value of the bounding box aligned with the x- and y-axis
     */
    private Rational minY;

    /**
     * a hash set of the points, used for equals(...) and hashCode()
     */
    private final HashSet<RPoint2D> pointSet;

    /**
     * a pre calculated hash code
     */
    private final int hash;

    /**
     * Constructs a rectangle made from four lines, where line <code>a</code>
     * is parallel to <code>c</code> and line <code>constant</code> is parallel to
     * <code>d</code>. And the lines <code>a</code> and <code>c</code> are
     * perpendicular to the lines <code>constant</code> and <code>d</code>.
     *
     * @param a the first line
     * @param b the second line
     * @param c the third line
     * @param d the last line
     * @throws RuntimeException if the lines <code>a</code>, <code>constant</code>, <code>c</code>
     *                          and <code>d</code> do not form a rectangle.
     */
    public RRectangle(RLine2D a, RLine2D b, RLine2D c, RLine2D d) throws RuntimeException {
        this(a.intersection(b), b.intersection(c), c.intersection(d), d.intersection(a));
    }

    /**
     * Constructs a rectangle made from four points that form the segments:
     * <code>p1->p2</code>, <code>p2->p3</code>, <code>p3->p4</code>
     * and <code>p4->p1</code>, where <code>p1->p2</code> and <code>p3->p4</code>
     * are parallel and <code>p2->p3</code> and <code>p4->p1</code>
     * are parallel. The segments <code>p1->p2</code> and <code>p3->p4</code>
     * are perpendicular to the segments <code>p2->p3</code> and <code>p4->p1</code>.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @param p3 the third point
     * @param p4 the last point
     * @throws RuntimeException if the segments formed by <code>p1</code>, <code>p2</code>
     *                          <code>p3</code> and <code>p4</code> do not form a rectangle.
     */
    public RRectangle(RPoint2D p1, RPoint2D p2, RPoint2D p3, RPoint2D p4) throws RuntimeException {

        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;

        this.s1 = new RLineSegment2D(p1, p2);
        this.s2 = new RLineSegment2D(p2, p3);
        this.s3 = new RLineSegment2D(p3, p4);
        this.s4 = new RLineSegment2D(p4, p1);

        // check if p1, p2, p3 and p4 form a valid rectangle
        validate();

        pointSet = new HashSet<RPoint2D>();
        pointSet.addAll(Arrays.asList(p1, p2, p3, p4));
        hash = pointSet.hashCode();

        findMinMaxValues();
    }

    /**
     * Calculates the approximate area of this rectangle.
     *
     * @return the approximate area of this rectangle.
     */
    public double area() {
        return s1.length() * s2.length();
    }

    /**
     * Calculates the exact squared area of this rectangle.
     *
     * @return the exact squared area of this rectangle.
     */
    public Rational areaSquared() {
        return s1.lengthSquared().multiply(s2.lengthSquared());
    }

    /**
     * Returns the point on which this rectangle can be balanced.
     *
     * @return the point on which this rectangle can be balanced.
     */
    public RPoint2D center() {
        Rational two = new Rational(2);
        Rational x = minX.add(maxX.subtract(minX).divide(two));
        Rational y = minY.add(maxY.subtract(minY).divide(two));
        return new RPoint2D(x, y);
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * <p>
     * The point is also considered inside if <code>p</code> lies on a line segment or if
     * <code>p</code> is one of the corner points of this rectangle.
     * </p>
     *
     * @param p the point to check if it's inside the boundaries of this rectangle.
     * @return <code>true</code> iff <code>p</code> is inside of <code>this</code>
     *         rectangle.
     */
    public boolean contains(RPoint2D p) {
        return // true if 'p' lies on one of the four segments
                (s1.contains(p) || s2.contains(p) || s3.contains(p) || s4.contains(p))
                        ||
                        // ... or if all 'paths' a->b->p, form a left turn
                        (CGUtil.formsLeftTurn(p1, p2, p) && CGUtil.formsLeftTurn(p2, p3, p)
                                && CGUtil.formsLeftTurn(p3, p4, p) && CGUtil.formsLeftTurn(p4, p1, p))
                        ||
                        // ... or if all 'paths' a->b->p, form a right turn
                        (CGUtil.formsRightTurn(p1, p2, p) && CGUtil.formsRightTurn(p2, p3, p)
                                && CGUtil.formsRightTurn(p3, p4, p) && CGUtil.formsRightTurn(p4, p1, p));
    }

    /**
     * Returns <code>true</code> iff <code>o</code> is a <code>RRectangle</code>
     * and all points in <code>this</code> are also present in <code>that</code>
     * (the order of the points does not matter).
     *
     * @param o the other rectangle.
     * @return <code>true</code> iff <code>o</code> is a <code>RRectangle</code>
     *         and all points in <code>this</code> are also present in
     *         <code>that</code> (the order of the points does not matter).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RRectangle that = (RRectangle) o;
        return this.pointSet.equals(that.pointSet);
    }

    /**
     * Find all minimum and maximum x- and y values of this rectangle.
     */
    private void findMinMaxValues() {
        maxX = maxY = Rational.NEGATIVE_INFINITY;
        minX = minY = Rational.POSITIVE_INFINITY;
        for (RPoint2D p : this.pointSet) {
            if (p.x.isMoreThan(maxX)) maxX = p.x;
            if (p.y.isMoreThan(maxY)) maxY = p.y;
            if (p.x.isLessThan(minX)) minX = p.x;
            if (p.y.isLessThan(minY)) minY = p.y;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RPoint2D> getPoints() {
        return Arrays.asList(p1, p2, p3, p4);
    }

    /**
     * Returns, depending on the parameter <code>boolean longSide</code>,
     * the longest or shortest side from this rectangle. Note that both
     * sides can be equal, in which case <code>getSide(true)</code> and
     * <code>getSide(false)</code> both return an arbitrary side of this
     * rectangle.
     *
     * @param longSide if <code>true</code>, the longest side is returned,
     *                 else the shortest.
     * @return depending on the parameter <code>boolean longSide</code>,
     *         the longest or shortest side from this rectangle. Note that both
     *         side could be equal.
     * @see compgeom.RLineSegment2D#lengthSquared()
     * @see #isSquare()
     */
    public RLineSegment2D getSide(boolean longSide) {
        RLineSegment2D longSegment = s1.lengthSquared().isMoreThan(s2.lengthSquared()) ? s1 : s2;
        RLineSegment2D shortSegment = s1.lengthSquared().isLessThan(s2.lengthSquared()) ? s1 : s2;
        return longSide ? longSegment : shortSegment;
    }

    /**
     * Returns a pre calculated hash code based on the <code>hashCode()</code>
     * of a <code>HashSet</code> containing the four points of
     * <code>this</code> rectangle.
     *
     * @return a pre calculated hash code based on the <code>hashCode()</code>
     *         of a <code>HashSet</code> containing the four points of
     *         <code>this</code> rectangle.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCongruentTo(RBoundSurface2D that) {
        List<RPoint2D> thatPoints = that.getPoints();
        if(thatPoints.size() != 4) {
            return false;
        }

        Rational thisA = s1.lengthSquared();
        Rational thisB = s2.lengthSquared();

        Rational thatA = new RLineSegment2D(thatPoints.get(0), thatPoints.get(1)).lengthSquared();
        Rational thatB = new RLineSegment2D(thatPoints.get(1), thatPoints.get(2)).lengthSquared();

        return (thisA.equals(thatA) && thisB.equals(thatB)) ||
                (thisA.equals(thatB) && thisB.equals(thatA));
    }
    
    /**
     * Returns <code>true</code> if <code>side</code> is a side of this rectangle.
     *
     * @param side the segment to check.
     * @return <code>true</code> if <code>side</code> is a side of this rectangle.
     */
    public boolean isSide(RLineSegment2D side) {
        return this.s1.equals(side) || this.s2.equals(side) || this.s3.equals(side) || this.s4.equals(side);
    }

    /**
     * Returns <code>true</code> if all sides of this rectangle are equal.
     *
     * @return <code>true</code> if all sides of this rectangle are equal.
     * @see compgeom.RLineSegment2D#lengthSquared()
     */
    public boolean isSquare() {
        return s1.lengthSquared().equals(s2.lengthSquared());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rational maxX() {
        return this.maxX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rational maxY() {
        return this.maxY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rational minX() {
        return this.minX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rational minY() {
        return this.minY;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        return String.format("p1: %s\np2: %s\n" +
                "p3: %s\np4: %s\nline p1 -> p2: %s\nline p2 " +
                "-> p3: %s\nline p3 -> p4: %s\nline p4 -> p1: %s\n" +
                "area: +/- %.2f",
                p1, p2, p3, p4, new RLine2D(p1, p2), new RLine2D(p2, p3),
                new RLine2D(p3, p4), new RLine2D(p4, p1), area());
    }

    /**
     * Checks if this is a valid rectangle.
     */
    private void validate() {
        if (!s1.line.slope.equals(s3.line.slope)) {
            throw new RuntimeException("Line segments s1 and s3 are not parallel.");
        }
        if (!s2.line.slope.equals(s4.line.slope)) {
            throw new RuntimeException("Line segments s2 and s4 are not parallel.");
        }
        if (!s1.line.isPerpendicularTo(s2.line)) {
            throw new RuntimeException("Line segments s1 and s3 are not " +
                    "perpendicular to line segments s2 and s4.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RRectangle xyAlignedRectangle() {
        RPoint2D topLeft = new RPoint2D(this.minX, this.maxY);
        RPoint2D topRight = new RPoint2D(this.maxX, this.maxY);
        RPoint2D bottomLeft = new RPoint2D(this.maxX, this.minY);
        RPoint2D bottomRight = new RPoint2D(this.minX, this.minY);
        return new RRectangle(topLeft, topRight, bottomLeft, bottomRight);
    }
}
