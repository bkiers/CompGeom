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

import compgeom.RLineSegment2D;
import compgeom.RLine2D;
import compgeom.RPoint2D;

import java.util.*;

/**
 * <p>
 * A representation of a sweep line holding an ordered set of events sorted by
 * their intersection point with this sweep line.
 * </p>
 *
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public class SweepLine {

    /**
     * The ordered set of events.
     */
    private TreeSet<Event> events;

    /**
     * A map holding all intersection points mapped to the Events
     * that form these intersections.
     */
    private Map<RPoint2D, Set<Event>> intersections;

    /**
     * The line sweeping from left to right.
     */
    private RLine2D sweepLine;

    /**
     * The point of the current Event.
     */
    private RPoint2D currentEventPoint;

    /**
     * A flag to indicate if we're slightly before or after the line.
     */
    private boolean before;

    /**
     * The queue associated with this sweep line.
     */
    protected EventQueue queue;

    /**
     * Whether to ignore intersections of line segments when both
     * their end points form the intersection point.
     */
    private boolean ignoreSegmentEndings;

    /**
     * Creates a new sweep line.
     */
    public SweepLine() {
        this(false);
    }

    /**
     * Creates a new sweep line.
     *
     * @param ignoreEndings Whether to ignore intersections of line
     *                      segments when both their end points form
     *                      the intersection point.
     */
    public SweepLine(boolean ignoreEndings) {
        events = new TreeSet<Event>();
        intersections = new HashMap<RPoint2D, Set<Event>>();
        sweepLine = null;
        currentEventPoint = null;
        before = true;
        ignoreSegmentEndings = ignoreEndings;
    }

    /**
     * Returns the Event above a given Event <code>e</code>, or
     * <code>null</code> if no such Event exists.
     *
     * @param e the event.
     * @return the Event above a given Event <code>e</code>, or
     *         <code>null</code> if no such Event exists.
     */
    public Event above(Event e) {
        return events.higher(e);
    }

    /**
     * Returns the Event below a given Event <code>e</code>, or
     * <code>null</code> if no such Event exists.
     *
     * @param e the event.
     * @return the Event below a given Event <code>e</code>, or 
     *         <code>null</code> if no such Event exists.
     */
    public Event below(Event e) {
        return events.lower(e);
    }

    /**
     * Checks if an intersection exists between two Events 'a' and 'b'.
     *
     * @param a the first Event.
     * @param b the second Event.
     */
    private void checkIntersection(Event a, Event b) {
        // Return immediately in case either of the events is null, or
        // if one of them is an INTERSECTION event.
        if(a == null || b == null || a.type == Event.Type.INTERSECTION ||
                b.type == Event.Type.INTERSECTION) {
            return;
        }

        // Get the intersection point between 'a' and 'b'.
        RPoint2D p = a.segment.intersection(b.segment);

        // No intersection exists.
        if(p == null) return;

        // If the intersection is formed by both the segment endings AND
        // ignoreSegmentEndings is true, return from this method.
        if(a.segment.hasEnding(p) && b.segment.hasEnding(p) && ignoreSegmentEndings) return;

        // Add the intersection.
        Set<Event> existing = intersections.remove(p);
        if(existing == null) existing = new HashSet<Event>();
        existing.add(a);
        existing.add(b);
        intersections.put(p, existing);

        // If the intersection occurs to the right of the sweep line, OR
        // if the intersection is on the sweep line and it's above the
        // current event-point, add it as a new Event to the queue.
        if(p.isRightOf(sweepLine) || (sweepLine.contains(p) && p.y.isMoreThan(currentEventPoint.y))) {
            Event intersection = new Event(Event.Type.INTERSECTION, p, null, this);
            queue.offer(p, intersection);
        }
    }

    /**
     * Returns the Events from this sweep line as a List.
     *
     * @return the Events from this sweep line as a List.
     */
    protected List<Event> events() {
        return new ArrayList<Event>(events);
    }

    /**
     * Returns all intersection points and the line segments passing
     * through them discovered so far in this sweep line as a Map.
     *
     * @return Returns all intersection points and the line segments passing
     *         through them discovered so far in this sweep line as a Map.
     */
    public Map<RPoint2D, Set<RLineSegment2D>> getIntersections() {
        Map<RPoint2D, Set<RLineSegment2D>> segments = new HashMap<RPoint2D, Set<RLineSegment2D>>();
        for(Map.Entry<RPoint2D, Set<Event>> entry : this.intersections.entrySet()) {
            Set<RLineSegment2D> set = new HashSet<RLineSegment2D>();
            for(Event e : entry.getValue()) {
                set.add(e.segment);
            }
            segments.put(entry.getKey(), set);
        }
        return segments;
    }

    /**
     * Handles a set of events.
     *
     * @param events the events to process.
     */
    public void handle(Set<Event> events) {
        if(events.size() == 0) return;

        Event[] array = events.toArray(new Event[events.size()]);
        sweepTo(array[0]);

        // If we shouldn't ignore segment endings, and there are more than
        // one events in the set, these events must intersect with each other.
        if(!ignoreSegmentEndings && events.size() > 1) {
            for(int i = 0; i < array.length-1; i++) {
                for(int j = i+1; j < array.length; j++) {
                    this.checkIntersection(array[i], array[j]);
                }
            }
        }

        // Handle each individual event from the set.
        for(Event e : events) {
            handle(e);
        }
    }

    /**
     * Handle an individual event.
     *
     * @param event the event to process.
     */
    private void handle(Event event) {

        switch(event.type) {
            case START:
                before = false;
                insert(event);
                checkIntersection(event, above(event));
                checkIntersection(event, below(event));
                break;
            case END:
                before = true;
                remove(event);
                checkIntersection(above(event), below(event));
                break;
            case INTERSECTION:
                before = true;
                Set<Event> set = intersections.get(event.point);
                Stack<Event> toInsert = new Stack<Event>();
                for(Event e : set) {
                    // If we the Event was not already removed, we want to insert it later on.
                    if(remove(e)) {
                        toInsert.push(e);
                    }
                }
                before = false;
                // Insert all Events that we were able to remove.
                while(!toInsert.isEmpty()) {
                    Event e = toInsert.pop();
                    insert(e);
                    checkIntersection(e, above(e));
                    checkIntersection(e, below(e));
                }
                break;
        }
    }

    /**
     * Returns <code>true</code> iff at least one intersection has
     * been discovered.
     *
     * @return <code>true</code> iff at least one intersection has
     *         been discovered.
     */
    public boolean hasIntersections() {
        return !this.intersections.isEmpty();
    }

    /**
     * Returns <code>true</code> iff an Event <code>e</code> was
     * successfully inserted in this sweep line.
     *
     * @param e the Event to insert.
     * @return <code>true</code> iff an Event <code>e</code> was
     *         successfully inserted in this sweep line.
     */
    public boolean insert(Event e) {
        return events.add(e);
    }

    /**
     * Returns the intersection between this sweep line and a given
     * Event <code>e</code>, or <code>null</code> if no intersection
     * exists.
     *
     * @param e the Event.
     * @return the intersection between this sweep line and a given
     *         Event <code>e</code>, or <code>null</code> if no intersection
     *         exists.
     */
    protected RPoint2D intersection(Event e) {
        if (e.type == Event.Type.INTERSECTION) {
            return e.point;
        } else {
            return e.segment.intersection(this.sweepLine);
        }
    }

    /**
     * Returns <code>true</code> iff we're slightly before the sweep line
     * or after it.
     *
     * @return <code>true</code> iff we're slightly before the sweep line
     *         or after it.
     */
    protected boolean isBefore() {
        return before;
    }

    /**
     * Returns <code>true</code> iff an Event <code>e</code> was
     * successfully removed from this sweep line.
     *
     * @param e the Event to remove.
     * @return <code>true</code> iff an Event <code>e</code> was
     *         successfully removed from this sweep line.
     */
    public boolean remove(Event e) {
        return events.remove(e);
    }

    /**
     * Set a new line.
     *
     * @param line the new line.
     */
    protected void setLine(RLine2D line) {
        this.sweepLine = line;
    }

    /**
     * Set the queue associated with this sweep line.
     *
     * @param q the queue.         
     */
    protected void setQueue(EventQueue q) {
        queue = q;
    }

    /**
     * Returns the number of events currently in this sweep line.
     *
     * @return the number of events currently in this sweep line.
     */
    public int size() {
        return events.size();
    }

    /**
     * Let this sweep line move to a new point.
     *
     * @param e the Event to move to.
     */
    private void sweepTo(Event e) {
        currentEventPoint = e.point;
        sweepLine = new RLine2D(sweepLine.slope, currentEventPoint);
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("SweepLine {\n");
        b.append("  line          = ").append(sweepLine).append("\n");
        b.append("  intersections = ").append(getIntersections()).append("\n");
        List<Event> reversed = new ArrayList<Event>(events);
        Collections.reverse(reversed);
        for (Event e : reversed) {
            b.append("  ").append(e).append("\n");
        }
        return b.append("}").toString();
    }
}
