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
import compgeom.RPoint2D;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;


public class TestEventQueue {

    @Test
    public void testEventQueue() {

        int[] xs1 = {1, 2, 2, 5, 12, 10, 12, 14, 8, 5, 3, -4};
        int[] ys1 = {2, 5, 7, 1, 6, 5, 4, 5, 5, 1, 3, -3};
        int[] xs2 = {4, 6, 6, 5, 7, 10, 12, 12, 10, 6, 1, 1};
        int[] ys2 = {8, 9, 3, 7, 6, 7, 6, 5, 7, 0, 2, 2};
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2));

        SweepLine line = new SweepLine();
        EventQueue queue = new EventQueue(segments, line);

        assertFalse(queue.isEmpty());

        List<Event> entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(-4, -3)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 3);
        assertTrue(entry.get(0).point.equals(new RPoint2D(1, 2)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(2, 5)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(2, 7)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(3, 3)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(4, 8)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 2);
        assertTrue(entry.get(0).point.equals(new RPoint2D(5, 1)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(5, 7)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(6, 0)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(6, 3)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(6, 9)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(7, 6)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(8, 5)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(10, 5)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 2);
        assertTrue(entry.get(0).point.equals(new RPoint2D(10, 7)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(12, 4)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(12, 5)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 2);
        assertTrue(entry.get(0).point.equals(new RPoint2D(12, 6)));

        entry = new ArrayList<Event>(queue.poll());
        assertTrue(entry.size() == 1);
        assertTrue(entry.get(0).point.equals(new RPoint2D(14, 5)));
        

        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEventQueueIllegalArgumentException() {
        int[] xs1 = {};
        int[] ys1 = {};
        int[] xs2 = {};
        int[] ys2 = {};
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2));
        SweepLine line = new SweepLine();
        new EventQueue(segments, line); // <- !
    }

    @Test(expected = NoSuchElementException.class)
    public void testEventQueueNoSuchElementException() {
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList("0 0 1 1"));
        SweepLine line = new SweepLine();
        EventQueue queue = new EventQueue(segments, line);
        queue.poll(); // START
        queue.poll(); // END
        queue.poll(); // <- !
    }
}
