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

import compgeom.Rational;
import compgeom.RLineSegment2D;
import compgeom.RPoint2D;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public class TestSweepLine {

    // test containing corner cases that went wrong

    @Test
    public void test_1() {
        // (closed) polygon
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(
                "3 0 4 8    4 8 8 5    5 2 8 5    5 2 6 2    5 1 6 2    3 0 5 1"));

        SweepLine SL = new SweepLine();
        EventQueue queue = new EventQueue(segments, SL);

        while(!queue.isEmpty()) {
            Set<Event> events = queue.poll();
            SL.handle(events);
        }

        assertTrue(SL.hasIntersections());
        assertTrue(SL.getIntersections().size() == 6);
        assertTrue(SL.size() == 0);
    }

    @Test
    public void test_2() {
        // horizontal, vertical, more than 2 segments through 1 point
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(
                "-5 -5 5 5    -5 5 5 -5    -1 0 1 0    0 0 0 6    4 1 4 -5    -1 0 6 0"));

        SweepLine SL = new SweepLine();
        EventQueue queue = new EventQueue(segments, SL);

        while(!queue.isEmpty()) {
            Set<Event> events = queue.poll();
            SL.handle(events);
        }

        assertTrue(SL.hasIntersections());
        assertTrue(SL.getIntersections().size() == 3);
        assertTrue(SL.size() == 0);
    }
}
