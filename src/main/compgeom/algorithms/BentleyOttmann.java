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
package compgeom.algorithms;

import compgeom.RLineSegment2D;
import compgeom.RPoint2D;
import compgeom.util.Event;
import compgeom.util.EventQueue;
import compgeom.util.SweepLine;

import java.util.*;

/**
 * <p>
 * The Bentley-Ottmann algorithm is a sweep line algorithm for listing all
 * crossings in a set of line segments. It extends the Shamos-Hoey algorithm,
 * a similar algorithm for testing whether or not a set of line segments
 * has any crossings. For an input consisting of <code>n</code> line segments with
 * <code>k</code> crossings, the Bentley-Ottmann algorithm takes
 * <code>O((n + k) log n)</code> time, a significant improvement on a naive algorithm 
 * that tests every pair of segments<sup>1</sup> in <code>O(n<sup>2</sup>)</code> time.
 * </p>
 * <p>
 * 1. <a href="http://en.wikipedia.org/wiki/Bentley%E2%80%93Ottmann_algorithm">
 * http://en.wikipedia.org/wiki/Bentley%E2%80%93Ottmann_algorithm</a>
 * </p>
 * <p>
 * See:
 * <ul>
 * <li>{@link ShamosHoey}</li>
 * <li>{@link compgeom.util.EventQueue}</li>
 * <li>{@link compgeom.util.SweepLine}</li>
 * </ul>
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 14, 2010
 * </p>
 */
public final class BentleyOttmann {

    /**
     * No need to instantiate this class
     */
    private BentleyOttmann() {
    }

    /**
     * Finds a set of points representing all intersections between a
     * given set of line segments in <code>O((n + k) * log(n))</code> time,
     * where <code>n</code> is the number of segments, and <code>k</code>
     * is the number of intersections.
     *
     * @param segments the line segments.
     * @return a set of points representing all intersections between a
     *         given set of line segments in <code>O((n + k) * log(n))</code>
     *         time.
     */
    public static Set<RPoint2D> intersections(Set<RLineSegment2D> segments) {
        return intersectionsMap(segments).keySet();
    }

    /**
     * <p>
     * Finds a set of points mapped to sets of line segments in
     * <code>O((n + k) * log(n))</code> time, where <code>n</code> is the
     * number of segments, and <code>k</code> is the number of intersections.
     * The set of points represents all intersections between a given set of
     * line segments. Note that not every segment intersects with the other
     * segments in the set. For example, the segments <code>a</code>,
     * <code>b</code> and <code>c</code>: <code>a = (0,0)->(4,4)</code>,
     * <code>b = (1,1)->(3,3)</code>, <code>c = (1,3)->(3,1)</code> all pass
     * through the point <code>(2,2)</code>, yet <code>a</code> and
     * <code>b</code> do not intersect each other.
     * </p>
     * 
     * @param segments the line segments.
     * @return a set of points representing all intersections between a
     *         given set of line segments in <code>O((n + k) * log(n))</code>
     *         time.
     * @see compgeom.RLineSegment2D#intersection(compgeom.RLineSegment2D) 
     */
    public static Map<RPoint2D, Set<RLineSegment2D>> intersectionsMap(Set<RLineSegment2D> segments) {

        if(segments.size() < 2) {
            return new HashMap<RPoint2D, Set<RLineSegment2D>>();
        }

        SweepLine sweepLine = new SweepLine();
        EventQueue queue = new EventQueue(segments, sweepLine);

        while(!queue.isEmpty()) {
            Set<Event> events = queue.poll();
            sweepLine.handle(events);
        }

        return sweepLine.getIntersections();
    }

    /**
     * Finds all intersections between a given set of line segments in
     * <code>O(n<sup>2</sup>)</code> time. Used for debugging and unit
     * testing the Bentley-Ottmann algorithm.
     *
     * @param segments the line segments.
     * @return all intersections between a given set of line segments.
     */
    protected static Set<RPoint2D> intersectionsNaive(Set<RLineSegment2D> segments) {
        RLineSegment2D[] array = segments.toArray(new RLineSegment2D[segments.size()]);
        Set<RPoint2D> intersections = new HashSet<RPoint2D>();
        for(int i = 0; i < array.length-1; i++) {
            for(int j = i+1; j < array.length; j++) {
                RPoint2D p = array[i].intersection(array[j]);
                if(p == null) continue;
                intersections.add(p);
            }
        }
        return intersections;
    }
}

