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
import compgeom.util.CGUtil;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: May 8, 2010
 * </p>
 */
public class TestBentleyOttmann {

    @Test
    public void test_1() {
        // simple test
        String data = "-5 -5 5 5   -5 5 5 -5   -1 0 1 0   -1 0 6 0   0 0 0 6   4 1 4 -5";
        Set<RLineSegment2D> segs = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(data));

        Set<RPoint2D> naive = BentleyOttmann.intersectionsNaive(segs);
        Set<RPoint2D> quick = BentleyOttmann.intersections(segs);

        assertTrue(naive.equals(quick));
    }

    @Test
    public void test_2() {
        // all vertical and horizontal segments
        String data = "2 0 2 5   1 1 6 1   0 3 6 3   4 -1 4 7   3 6 6 6   6 6 6 3   5 5 8 5   8 5 8 3";
        Set<RLineSegment2D> segs = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(data));

        Set<RPoint2D> naive = BentleyOttmann.intersectionsNaive(segs);
        Set<RPoint2D> quick = BentleyOttmann.intersections(segs);

        assertTrue(naive.equals(quick));
    }

    @Test
    public void testRandom() {
        final Random r = new Random();
        final int box = 1000;
        final int numSegments = 50;
        final int maxSizeSegments = 10;
        final int tests = 50;

        Set<RLineSegment2D> segs = null;

        try {
            for(int i = 1; i <= tests; i++) {
                segs = new HashSet<RLineSegment2D>();

                while(segs.size() < numSegments) {
                    int x1 = r.nextInt(box) - (box/2);
                    int y1 = r.nextInt(box) - (box/2);
                    int deltaX = r.nextInt(maxSizeSegments)+1;
                    int deltaY = r.nextInt(maxSizeSegments)+1;
                    int x2 = x1 + (r.nextBoolean() ? -deltaX : deltaX);
                    int y2 = y1 + (r.nextBoolean() ? -deltaY : deltaY);

                    RPoint2D p1 = new RPoint2D(x1,y1);
                    RPoint2D p2 = new RPoint2D(x2,y2);

                    if(!p1.equals(p2)) {
                        RLineSegment2D s = new RLineSegment2D(p1, p2);
                        segs.add(s);
                    }
                }

                Set<RPoint2D> naive = BentleyOttmann.intersectionsNaive(segs);
                Set<RPoint2D> quick = BentleyOttmann.intersections(segs);

                assertTrue("Problem with segs = "+segs, naive.equals(quick));

                if(i%100 == 0) {
                    System.out.println("TestBentleyOttmann.testRandom() :: "+i+"/"+tests);
                }
            }
        } catch(Exception e) {
            System.out.println("e.getMessage() = "+e.getMessage()+"\nsegs = "+segs);
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
