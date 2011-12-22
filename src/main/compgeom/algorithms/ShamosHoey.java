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

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * The Shamos-Hoey algorithm tells, given a set of line segments,
 * if any two segment intersect in <code>O(n * log(n))</code> time
 * using a <a href="http://en.wikipedia.org/wiki/Sweep_line_algorithm">
 * sweep line</a> approach.
 * </p>
 * <p>
 * See:
 * <ul>
 * <li>{@link compgeom.util.EventQueue}</li>
 * <li>{@link compgeom.util.SweepLine}</li>
 * </ul>
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class ShamosHoey {

    /**
     * Returns the first intersection point discovered while sweeping
     * from left to right in <code>O(n * log(n))</code> time. Note that
     * not necessarily the leftmost intersection point is returned.
     * For example, given the three segments <code>a = (1,1)->(7,4)</code>,
     * <code>b = (1,3)->(7,3)</code> and <code>c = (2,2)->(2,4)</code>, the
     * intersection between <code>a</code> and <code>b</code> (at
     * <code>(5,3)</code>) will be discovered before the intersection
     * between <code>b</code> and <code>c</code> (at <code>(2,3)</code>)
     * when sweeping from left to right.
     *
     * @param segments the line segments.
     * @return the first intersection point discovered while sweeping
     *         from left to right in <code>O(n * log(n))</code> time,
     *         or <code>null</code> if no intersection exists.
     */
    public static RPoint2D intersection(Set<RLineSegment2D> segments) {
        return intersection(segments, false);
    }

    /**
     * <p>
     * Returns the first intersection point discovered while sweeping
     * from left to right in <code>O(n * log(n))</code> time. Note that
     * not necessarily the leftmost intersection point is returned.
     * For example, given the three segments <code>a = (1,1)->(7,4)</code>,
     * <code>b = (1,3)->(7,3)</code> and <code>c = (2,2)->(2,4)</code>, the
     * intersection between <code>a</code> and <code>b</code> (at
     * <code>(5,3)</code>) will be discovered before the intersection
     * between <code>b</code> and <code>c</code> (at <code>(2,3)</code>)
     * when sweeping from left to right.
     * </p>
     * <p>
     * When <code>ignoreSegmentEndings</code> is <code>true</code>, all
     * intersections points that are formed by two end points of line
     * segments are ignored. For example, the intersection between
     * <code>(1,1)->(3,3)</code> and <code>(3,3)->(4,4)</code> is ignored,
     * but the intersection between <code>(1,1)->(3,3)</code> and
     * <code>(2,2)->(3,1)</code> is not.
     * </p>
     *
     * @param segments             the line segments.
     * @param ignoreSegmentEndings whether or not to ignore intersections
     *                             points that are formed by two end points
     *                             of line segments.
     * @return the first intersection point discovered while sweeping
     *         from left to right in <code>O(n * log(n))</code> time,
     *         or <code>null</code> if no intersection exists.
     */
    public static RPoint2D intersection(Set<RLineSegment2D> segments, boolean ignoreSegmentEndings) {

        if (segments.size() < 2) {
            return null;
        }

        SweepLine sweepLine = new SweepLine(ignoreSegmentEndings);
        EventQueue queue = new EventQueue(segments, sweepLine);

        while (!queue.isEmpty()) {
            Set<Event> events = queue.poll();
            sweepLine.handle(events);
            if (sweepLine.hasIntersections()) {
                Map<RPoint2D, Set<RLineSegment2D>> intersections = sweepLine.getIntersections();
                return intersections.keySet().toArray(new RPoint2D[intersections.size()])[0];
            }
        }

        return null;
    }

    /**
     * Returns <code>true</code> if an intersection point exists
     * given a set of line segments, in <code>O(n * log(n))</code>
     * time.
     *
     * @param segments the line segments.
     * @return <code>true</code> if an intersection point exists.
     */
    public static boolean intersectionExists(Set<RLineSegment2D> segments) {
        return intersection(segments, false) != null;
    }

    /**
     * <p>
     * Returns <code>true</code> if an intersection point exists
     * given a set of line segments, in <code>O(n * log(n))</code>
     * time.
     * </p>
     * <p>
     * When <code>ignoreSegmentEndings</code> is <code>true</code>, all
     * intersections points that are formed by two end points of line
     * segments are ignored. For example, the intersection between
     * <code>(1,1)->(3,3)</code> and <code>(3,3)->(4,4)</code> is ignored,
     * but the intersection between <code>(1,1)->(3,3)</code> and
     * <code>(2,2)->(3,1)</code> is not.
     * </p>
     *
     * @param segments             the line segments.
     * @param ignoreSegmentEndings whether or not to ignore intersections
     *                             points that are formed by two end points
     *                             of line segments.
     * @return <code>true</code> if an intersection point exists.
     */
    public static boolean intersectionExists(Set<RLineSegment2D> segments, boolean ignoreSegmentEndings) {
        return intersection(segments, ignoreSegmentEndings) != null;
    }
}
