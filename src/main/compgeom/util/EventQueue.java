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
package compgeom.util;

import compgeom.RLine2D;
import compgeom.RLineSegment2D;
import compgeom.RPoint2D;
import compgeom.Rational;

import java.util.*;

/**
 * <p>
 * A queue of points mapped to segments. The points are sorted by their x coordinate.
 * When comparing two points, the point with the lower x coordinate is considered
 * lower than the other point. In case of a tie, the point with the lower y coordinate
 * will become the lowest point.
 * </p>
 * <p>
 * This queue is backed up by a {@link TreeMap}: the creation cost of this
 * class is therefore <code>O(n*log(n))</code>.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 3, 2010
 * </p>
 */
public final class EventQueue {

    /**
     * The sorted map holding the points -> events.
     */
    private TreeMap<RPoint2D, List<Event>> events;

    /**
     * Creates a new queue from a set of line segments. The sweep line is
     * initialized as well: while adding the segments to the queue, the minimum
     * delta-x and maximum delta-y between all end-points of the line segments
     * is also found and with these values, the sweep line's slope is created.
     * That way, we're sure that the slope of the sweep line does not equal
     * the slop of any of the line segments. It also registers <code>this</code>
     * in the sweep line so that when the sweep line encounters an intersection
     * between two Events, a new INTERSECTION-Event can be added from within the
     * sweep line.
     *
     * @param segments the line segments.
     * @param line     the sweep line.
     * @throws IllegalArgumentException if <code>segments</code> is empty.
     */
    public EventQueue(Set<RLineSegment2D> segments, SweepLine line) {
        if(segments.isEmpty()) {
            throw new IllegalArgumentException("'segments' cannot be empty");
        }

        events = new TreeMap<RPoint2D, List<Event>>(new Comparator<RPoint2D>() {
            @Override
            public int compare(RPoint2D a, RPoint2D b) {
                int dX = a.x.compareTo(b.x);
                return dX != 0 ? dX : a.y.compareTo(b.y);
            }
        });

        init(segments, line);
    }

    /**
     * Initializes this queue and the sweep line.
     *
     * @param segments the line segments.
     * @param line     the sweep line.
     */
    private void init(Set<RLineSegment2D> segments, SweepLine line) {

        Rational minY = Rational.POSITIVE_INFINITY;
        Rational maxY = Rational.NEGATIVE_INFINITY;
        Rational minDeltaX = Rational.POSITIVE_INFINITY;
        TreeSet<Rational> xs = new TreeSet<Rational>();

        for (RLineSegment2D s : segments) {
            xs.add(s.p1.x);
            xs.add(s.p2.x);
            if(s.minY.isLessThan(minY)) minY = s.minY;
            if(s.maxY.isMoreThan(maxY)) maxY = s.maxY;
            offer(s.p1, new Event(Event.Type.START, s.p1, s, line));
            offer(s.p2, new Event(Event.Type.END, s.p2, s, line));
        }

        Rational[] xsArray = xs.toArray(new Rational[xs.size()]);
        for(int i = 1; i < xsArray.length; i++) {
            Rational tempDeltaX = xsArray[i].subtract(xsArray[i-1]);
            if(tempDeltaX.isLessThan(minDeltaX)) {
                minDeltaX = tempDeltaX;
            }
        }

        Rational deltaY = maxY.subtract(minY);
        Rational slope = deltaY.divide(minDeltaX).multiply(Rational.THOUSAND).negate();
        
        line.setLine(new RLine2D(slope, RPoint2D.ORIGIN));
        line.setQueue(this);
    }

    /**
     * Returns true iff this queue is empty.
     *
     * @return true iff this queue is empty.
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }

    /**
     * Offer a new event <code>s</code> at point <code>p</code> in this queue.
     * END Events are added to the front, the other Event types are appended
     * to the end. 
     *
     * @param p the event point.
     * @param e the event.
     */
    public void offer(RPoint2D p, Event e) {
        List<Event> existing = events.remove(p);
        if (existing == null) existing = new LinkedList<Event>();

        // END events should be at the start of the list
        if(e.type == Event.Type.END) {
            existing.add(0, e);
        } else {
            existing.add(e);
        }
        events.put(p, existing);
    }

    /**
     * Get, and remove, the first (lowest) item from this queue.
     *
     * @return the first (lowest) item from this queue.
     * @throws NoSuchElementException if there are no more elements in the queue.
     */
    public Set<Event> poll() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        Map.Entry<RPoint2D, List<Event>> entry =  events.pollFirstEntry();
        return new LinkedHashSet<Event>(entry.getValue());
    }

    /**
     * Returns a String representation of this queue.
     *
     * @return a String representation of this queue.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("EventQueue {\n");
        for (Map.Entry<RPoint2D, List<Event>> entry : events.entrySet()) {
            b.append("  ").append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return b.append("}").toString();
    }
}
