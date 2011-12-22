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

import compgeom.algorithms.ShamosHoey;
import compgeom.util.CGUtil;
import compgeom.util.Extremal;

import java.util.*;

/**
 * <p>
 * A class that represents a polygon in 2D space.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class RPolygon2D implements Iterable<RPoint2D>, RBoundSurface2D {

    /**
     * the points of this polygon
     */
    private final List<RPoint2D> points;

    /**
     * the points of this polygon
     */
    private Set<RLineSegment2D> segments;

    /**
     * a pre calculated has code
     */
    private final int hash;

    /**
     * is this polygon simple?
     */
    private Boolean simple;

    /**
     * is this polygon convex?
     */
    private Boolean convex;

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
     * Create a new polygon given an array of x- and y coordinates.
     * The order of the point can be changed while creating the polygon.
     * For example, if the point <code>C</code> in <code>A,B,C,D,E</code>
     * has the lowest y coordinate, that point is then placed at the start
     * of the internal list of points, which then looks like:
     * <code>C,D,E,A,B</code>. This is done because polygons can then be
     * compared in linear time. Polygons
     * <code>new RPolygon2D(A,B,C)</code> and <code>new RPolygon2D(C,B,A)</code>
     * will be considered equal.
     *
     * @param xs the x coordinates.
     * @param ys the y coordinates.
     * @throws IllegalArgumentException if <code>pts</code> contains less
     *                                  than 3 points or if all points are
     *                                  collinear or if <code>xs.length</code>
     *                                  does not equal <code>ys.length</code>.
     */
    public RPolygon2D(int[] xs, int[] ys) throws IllegalArgumentException {
        this(CGUtil.createRPoint2DList(xs, ys));
    }

    /**
     * Creates a polygon with the points from <code>pts</code> added to it.
     * The order of the point can be changed while creating the polygon.
     * For example, if the point <code>C</code> in <code>A,B,C,D,E</code>
     * has the lowest y coordinate, that point is then placed at the start
     * of the internal list of points, which then looks like:
     * <code>C,D,E,A,B</code>. This is done because polygons can then be
     * compared in linear time. Polygons
     * <code>new RPolygon2D(A,B,C)</code> and <code>new RPolygon2D(C,B,A)</code>
     * will be considered equal.
     *
     * @param pts the points to add to this polygon.
     * @throws IllegalArgumentException if <code>pts</code> contains less
     *                                  than 3 points or if all points are
     *                                  collinear.
     */
    public RPolygon2D(RPoint2D... pts) throws IllegalArgumentException {
        this(Arrays.asList(pts));
    }

    /**
     * Creates a polygon with the points from <code>pts</code> added to it.
     * The order of the point can be changed while creating the polygon.
     * For example, if the point <code>C</code> in <code>A,B,C,D,E</code>
     * has the lowest y coordinate, that point is then placed at the start
     * of the internal list of points, which then looks like:
     * <code>C,D,E,A,B</code>. This is done because polygons can then be
     * compared in linear time. Polygons
     * <code>new RPolygon2D(A,B,C)</code> and <code>new RPolygon2D(C,A,B)</code>
     * will be considered equal.
     *
     * @param pts the points to add to this polygon.
     * @throws IllegalArgumentException if <code>pts</code> contains less
     *                                  than 3 points, if all points are
     *                                  collinear or if there are two successive
     *                                  points that are equal.
     */
    public RPolygon2D(List<RPoint2D> pts) throws IllegalArgumentException {
        if (pts.size() < 3) {
            throw new IllegalArgumentException("invalid polygon: it must contain at least points");
        }
        for(int i = 1; i < pts.size(); i++) {
            if(pts.get(i-1).equals(pts.get(i))) {
                throw new IllegalArgumentException("'pts' cannot contain two equal points in a row");
            }
        }
        if (CGUtil.allCollinear(pts)) {
            throw new IllegalArgumentException("invalid polygon: all points are collinear");
        }
        points = rearrange(pts);
        segments = this.getSegments();
        hash = segments.hashCode();
        simple = null;
        convex = null;
        initMinMax();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(RPoint2D p) {
        // Create a horizontal line segment starting at 'p' moving right.
        Rational x = Rational.max(p.x, this.maxX).abs();
        RLineSegment2D ray = new RLineSegment2D(p, new RPoint2D(x.plusOne(), p.y));

        int cuts = 0;

        for(RLineSegment2D s : this.segments) {
            // Immediately return true if 'p' lies on a segment.
            if(s.contains(p)) return true;

            // Find the possible intersection point.
            RPoint2D intersection = ray.intersection(s);

            // Get the uppermost point from the segment
            RPoint2D upper = s.p1.y.isMoreThan(s.p2.y) ? s.p1 : s.p2;

            // Only count a cut if there is an intersection, and that intersection
            // does not pass through the uppermost point of 's'.
            if(!(intersection == null || intersection.equals(upper))) {
                cuts++;
            }
        }

        // The point is inside this polygon if there are an uneven
        // number of 'cuts' through the segments of this polygon.
        return cuts%2 == 1;
    }

    /**
     * Returns <code>true</code> iff <code>o</code> is a <code>RPolygon2D</code>
     * and if all points in <code>this</code> are also in <code>o</code>
     * in the same order.
     *
     * @param o the other polygon.
     * @return <code>true</code> iff <code>o</code> is a <code>RPolygon2D</code>
     *         and if all points in <code>this</code> are also in <code>o</code>
     *         in the same order.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPolygon2D that = (RPolygon2D) o;
        return this.segments.equals(that.segments);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<RPoint2D> getPoints() {
        return new ArrayList<RPoint2D>(points);
    }
    
    /**
     * Returns a copy of the list of points from this polygon where
     * the first and last points are the same (making a closed 'walk').
     *
     * @return a copy of the list of points from this polygon forming
     *         a closed walk.
     */
    public List<RPoint2D> getPointsClosed() {
        List<RPoint2D> copy = new ArrayList<RPoint2D>(points);
        copy.add(copy.get(0));
        return copy;
    }

    /**
     * Returns the set of line segments this polygon is made of. Note that
     * a "closed" polygon is returned: from a polygon made of the points
     * <code>(0,0)</code>, <code>(1,1)</code> and <code>(1,0)</code>, the
     * following segments are returned: <code>(0,0)->(1,1)</code>,
     * <code>(1,1)->(1,0)</code> and <code>(1,0)->(0,0)</code>.
     *
     * @return the set of line segments this polygon is made of.
     */
    public Set<RLineSegment2D> getSegments() {
        if(segments == null) {
            segments = new LinkedHashSet<RLineSegment2D>();
            int N = points.size();
            RPoint2D a, b;
            for(int i = 1; i < N; i++) {
                a = points.get(i-1);
                b = points.get(i);
                if(!a.equals(b)) {
                    segments.add(new RLineSegment2D(a, b));
                }
            }
            a = points.get(N-1);
            b = points.get(0);
            if(!a.equals(b)) {
                segments.add(new RLineSegment2D(a, b)); // close the polygon
            }
        }
        return new LinkedHashSet<RLineSegment2D>(segments);
    }

    /**
     * Returns a pre-calculated hash code based on the segments of this polygon.
     *
     * @return a pre-calculated hash code based on the segments of this polygon.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Initializes all minimum and maximum coordinates.
     */
    private void initMinMax() {
        RPoint2D first = points.get(0);
        this.minX = first.x;
        this.minY = first.y;
        this.maxX = first.x;
        this.maxY = first.y;
        for(int i = 1; i < this.points.size(); i++) {
            RPoint2D p = points.get(i);
            Rational x = p.x;
            Rational y = p.y;

            if(x.isLessThan(minX)) minX = x;
            if(y.isLessThan(minY)) minY = y;
            if(x.isMoreThan(maxX)) maxX = x;
            if(y.isMoreThan(maxY)) maxY = y;
        }
    }

    /**
     * Returns true iff this polygon is complex (not simple).
     *
     * @return true iff this polygon is not simple.
     * @see RPolygon2D#isSimple() 
     */
    public boolean isComplex() {
        return !this.isSimple();
    }

    /**
     * Returns <code>true</code> iff this polygon is concave (not convex).
     *
     * @return <code>true</code> iff this polygon is concave (not convex).
     */
    public boolean isConcave() {
        return !isConvex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCongruentTo(RBoundSurface2D that) {
        throw new RuntimeException("to be implemented");
    }

    /**
     * <p>
     * Returns <code>true</code> iff this polygon is convex.
     * A polygon is convex if all interior angles are less
     * than 180 degrees: all the vertices point 'outwards',
     * away from the center <sup>1</sup>.
     * </p>
     * <p>
     * 1. <a href="http://www.mathopenref.com/polygonconvex.html">
     * http://www.mathopenref.com/polygonconvex.html</a>
     * </p>
     * 
     * @return <code>true</code> iff this polygon is convex.
     */
    public boolean isConvex() {
        // Check is this method has already been invoked.
        if(convex == null) {
            if(this.points.size() == 3) {
                this.convex = true;
            } else if(this.isComplex()) {
                // Complex polygons are not convex.
                this.convex = false;
            } else {
                // Check if all points in this polygon form the same turn.

                // remove all collinear points
                List<RPoint2D> temp = CGUtil.removeCollinear(this.getPointsClosed());

                RPoint2D a = temp.get(0);
                RPoint2D b = temp.get(1);
                RPoint2D c = temp.get(2);

                // Check the first turn (a, b, c cannot be collinear since we removed those).
                boolean firstTurn = CGUtil.formsLeftTurn(a, b, c);

                for(int i = 3; i < temp.size(); i++) {
                    a = temp.get(i-2);
                    b = temp.get(i-1);
                    c = temp.get(i);

                    // All points should form the same turn as the first turn.
                    if(CGUtil.formsLeftTurn(a, b, c) != firstTurn) {
                        return false;
                    }
                }

                // All point apparently formed the same turn: convex = true.
                this.convex = true;
            }
        }
        return this.convex;
    }

    /**
     * <p>
     * Returns <code>true</code> iff this polygon is simple. <i>"A polygon <code>P</code>
     * is said to be simple (or Jordan) if the only points of the plane
     * belonging to two polygon edges of <code>P</code> are the polygon
     * vertices of <code>P</code>." <sup>1</sup></i>
     * </p>
     * <p>
     * 1. <a href="http://mathworld.wolfram.com/about/author.html">Weisstein,
     * Eric W.</a> "Simple Polygon." <i><a href="http://mathworld.wolfram.com/">
     * From MathWorld</a></i>--A Wolfram Web Resource.
     * <a href="http://mathworld.wolfram.com/SimplePolygon.html">
     * http://mathworld.wolfram.com/SimplePolygon.html</a> 
     * </p>
     *
     * @return <code>true</code> iff this polygon is simple.
     */
    public boolean isSimple() {
        if(simple == null) {
            Set<RLineSegment2D> segments = getSegments();
            simple = !ShamosHoey.intersectionExists(segments, true);
        }
        return simple;
    }
    
    /**
     * Returns an <code>Iterator&lt;RPoint2D&gt;</code> of the
     * points in this polygon.
     *
     * @return an <code>Iterator&lt;RPoint2D&gt;</code> of the
     *         points in this polygon.
     */
    @Override
    public Iterator<RPoint2D> iterator() {
        return points.iterator();
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
     * Returns the points <code>pts</code> reordered so that the first
     * point is the one with the lowest y coordinate while keeping the
     * 'path' of the points in tact.
     *
     * @param pts the points.
     * @return the points <code>pts</code> reordered so that the first
     *         point is the one with the lowest y coordinate while keeping the
     *         'path' of the points in tact.
     */
    private List<RPoint2D> rearrange(List<RPoint2D> pts) {
        final int indexLowest = CGUtil.getExtremalIndex(pts, Extremal.LOWER_LEFT);
        List<RPoint2D> temp = new ArrayList<RPoint2D>();
        for (int i = indexLowest; i < pts.size(); i++) {
            temp.add(pts.get(i));
        }
        for (int i = 0; i < indexLowest; i++) {
            temp.add(pts.get(i));
        }
        return temp;
    }

    /**
     * Get the number of points in this polygon.
     *
     * @return the number of points in this polygon.
     */
    public int size() {
        return points.size();
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        return String.format("poly {\n  points = %s\n  segments = %s\n}", points, this.getSegments());
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
