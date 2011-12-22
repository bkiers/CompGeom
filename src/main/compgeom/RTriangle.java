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
 * A class that represents a triangle.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: May 14, 2010
 * </p>
 */
public class RTriangle implements RBoundSurface2D {

    /**
     * the first point of this triangle.
     */
    public final RPoint2D p1;

    /**
     * the second point of this triangle.
     */
    public final RPoint2D p2;

    /**
     * the third point of this triangle.
     */
    public final RPoint2D p3;

    /**
     * the line segment formed by points: {@link RTriangle#p1} <code>-></code> {@link RTriangle#p2}.
     */
    public final RLineSegment2D s1;

    /**
     * the line segment formed by points: {@link RTriangle#p2} <code>-></code> {@link RTriangle#p3}.
     */
    public final RLineSegment2D s2;

    /**
     * the line segment formed by points: {@link RTriangle#p3} <code>-></code> {@link RTriangle#p1}.
     */
    public final RLineSegment2D s3;

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
    private HashSet<RPoint2D> pointSet;

    /**
     * a pre calculated hash code
     */
    private final int hash;

    /**
     * Creates a new triangle given three points.
     * 
     * @param a the first point of this triangle.
     * @param b the second point of this triangle.
     * @param c the third point of this triangle.
     * @throws IllegalArgumentException when <code>a</code>, <code>b</code>
     *                                  and <code>c</code> are collinear.
     */
    public RTriangle(RPoint2D a, RPoint2D b, RPoint2D c) throws IllegalArgumentException {
        if(CGUtil.collinear(a, b, c)) {
            throw new IllegalArgumentException(a+", "+b+" and "+c+" are collinear.");
        }

        this.p1 = a;
        this.p2 = b;
        this.p3 = c;

        this.s1 = new RLineSegment2D(p1, p2);
        this.s2 = new RLineSegment2D(p2, p3);
        this.s3 = new RLineSegment2D(p3, p1);

        pointSet = new HashSet<RPoint2D>();
        pointSet.addAll(Arrays.asList(p1, p2, p3));
        hash = pointSet.hashCode();

        findMinMaxValues();
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * <p>
     * The point is also considered inside if <code>p</code> lies on a line segment or if
     * <code>p</code> is one of the corner points of this triangle.
     * </p>
     *
     * @param p the point to check if it's inside the boundaries of this triangle.
     * @return <code>true</code> iff <code>p</code> is inside of <code>this</code>
     *         triangle.
     */
    public boolean contains(RPoint2D p) {
        return // true if 'p' lies on one of the four segments
                (s1.contains(p) || s2.contains(p) || s3.contains(p))
                        ||
                        // ... or if all 'paths' a->b->p, form a left turn
                        (CGUtil.formsLeftTurn(p1, p2, p) && CGUtil.formsLeftTurn(p2, p3, p)
                                && CGUtil.formsLeftTurn(p3, p1, p))
                        ||
                        // ... or if all 'paths' a->b->p, form a right turn
                        (CGUtil.formsRightTurn(p1, p2, p) && CGUtil.formsRightTurn(p2, p3, p)
                                && CGUtil.formsRightTurn(p3, p1, p));
    }

    /**
     * Returns <code>true</code> iff <code>o</code> is a <code>RTriangle</code>
     * and all points in <code>this</code> are also present in <code>that</code>
     * (the order of the points does not matter!).
     *
     * @param o the other (possible) triangle.
     * @return <code>true</code> iff <code>o</code> is a <code>RTriangle</code>
     *         and all points in <code>this</code> are also present in
     *         <code>that</code> (the order of the points does not matter!).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RTriangle that = (RTriangle) o;
        return this.pointSet.equals(that.pointSet);
    }

    /**
     * Find all minimum and maximum x- and y values of this triangle.
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
     * Returns the longest side of this triangle.
     *
     * @return the longest side of this triangle.
     */
    public RLineSegment2D getLongestSide() {
        Rational l1 = s1.lengthSquared();
        Rational l2 = s2.lengthSquared();
        Rational l3 = s3.lengthSquared();
        return l1.isMoreThan(l2) ?               // compare s1 and s2
                (l1.isMoreThan(l3) ? s1 : s3) :  // compare s1 and s3
                (l2.isMoreThan(l3) ? s2 : s3);   // compare s2 and s3
    }

    /**
     * Returns the four points this triangle is made of as an array.
     *
     * @return the four points this triangle is made of as an array.
     */
    public List<RPoint2D> getPoints() {
        return Arrays.asList(p1, p2, p3);
    }

    /**
     * Returns a pre calculated hash code based on the <code>hashCode()</code>
     * of a <code>HashSet</code> containing the three points of
     * <code>this</code> triangle.
     *
     * @return a pre calculated hash code based on the <code>hashCode()</code>
     *         of a <code>HashSet</code> containing the three points of
     *         <code>this</code> triangle.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Returns <code>true</code> iff all interior angles measure less than 90 degrees.
     *
     * @return <code>true</code> iff all interior angles measure less than 90 degrees.
     */
    public boolean isAcuteAngled() {
        if(this.isRightAngled()) {
            return false;
        }

        RLine2D p = s2.line.perpendicularLine(p1);
        RLine2D q = s3.line.perpendicularLine(p2);

        RPoint2D ip = p.intersection(q);

        return this.contains(ip);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCongruentTo(RBoundSurface2D that) {
        List<RPoint2D> thatPoints = that.getPoints();
        if(thatPoints.size() != 3) {
            return false;
        }
        List<RPoint2D> thisPoints = this.getPoints();

        Map<Rational, Integer> thisDistances = CGUtil.getFrequencyMap(
                new RLineSegment2D(thisPoints.get(0), thisPoints.get(1)).lengthSquared(),
                new RLineSegment2D(thisPoints.get(1), thisPoints.get(2)).lengthSquared(),
                new RLineSegment2D(thisPoints.get(2), thisPoints.get(0)).lengthSquared()
        );

        Map<Rational, Integer> thatDistances = CGUtil.getFrequencyMap(
                new RLineSegment2D(thatPoints.get(0), thatPoints.get(1)).lengthSquared(),
                new RLineSegment2D(thatPoints.get(1), thatPoints.get(2)).lengthSquared(),
                new RLineSegment2D(thatPoints.get(2), thatPoints.get(0)).lengthSquared()
        );

        return thisDistances.equals(thatDistances);
    }
    
    /**
     * Returns <code>true</code> iff all sides have the same length.
     * 
     * @return <code>true</code> iff all sides have the same length.
     */
    public boolean isEquilateral() {
        Rational l1 = s1.lengthSquared();
        Rational l2 = s2.lengthSquared();
        Rational l3 = s3.lengthSquared();
        return l1.equals(l2) && l1.equals(l3);
    }

    /**
     * Returns <code>true</code> if at least two sides are equal in length.
     * This means that all equilateral triangles are also isosceles.
     *
     * @return <code>true</code> if at least two sides are equal in length.
     */
    public boolean isIsosceles() {
        Rational l1 = s1.lengthSquared();
        Rational l2 = s2.lengthSquared();
        Rational l3 = s3.lengthSquared();
        return l1.equals(l2) || l1.equals(l3) || l2.equals(l3);
    }

    /**
     * Returns <code>true</code> iff none of its interior angles measures 90 degrees.
     *
     * @return <code>true</code> iff none of its interior angles measures 90 degrees.
     */
    public boolean isOblique() {
        return !isRightAngled();
    }

    /**
     * Returns <code>true</code> iff one angle measures more than 90 degrees.
     * 
     * @return <code>true</code> iff one angle measures more than 90 degrees.
     */
    public boolean isObtuseAngled() {
        return !isAcuteAngled();
    }

    /**
     * Returns <code>true</code> iff one of its interior angles measures 90 degrees
     * (a right angle).
     *
     * @return <code>true</code> iff one of its interior angles measures 90 degrees
     *         (a right angle).
     */
    public boolean isRightAngled() {
        // If one of the lines is horizontal and one of them vertical, it's right-angled.
        if((s1.line.isHorizontal() || s2.line.isHorizontal() || s3.line.isHorizontal()) &&
                (s1.line.isVertical() || s2.line.isVertical() || s3.line.isVertical())) {
            return true;
        }
        
        final Rational m1 = s1.line.slope;
        final Rational m2 = s2.line.slope;
        final Rational m3 = s3.line.slope;

        return m1.multiply(m2).equals(Rational.MINUS_ONE) ||
                m2.multiply(m3).equals(Rational.MINUS_ONE) ||
                m3.multiply(m1).equals(Rational.MINUS_ONE);
    }

    /**
     * Returns <code>true</code> iff all sides are unequal.
     *
     * @return <code>true</code> iff all sides are unequal.
     */
    public boolean isScalene() {
        Rational l1 = s1.lengthSquared();
        Rational l2 = s2.lengthSquared();
        Rational l3 = s3.lengthSquared();
        return !l1.equals(l2) && !l1.equals(l3) && !l2.equals(l3);
    }
    
    /**
     * Returns <code>true</code> if <code>side</code> is a side of this triangle.
     *
     * @param side the segment to check.
     * @return <code>true</code> if <code>side</code> is a side of this triangle.
     */
    public boolean isSide(RLineSegment2D side) {
        return this.s1.equals(side) || this.s2.equals(side) || this.s3.equals(side);
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
