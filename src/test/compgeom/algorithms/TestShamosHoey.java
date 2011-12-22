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
 * Date: May 3, 2010
 * </p>
 */
public class TestShamosHoey {

    @Test
    public void testIntersection_Null() {
        int[] xs1 = {0,1,2,4};
        int[] ys1 = {3,1,1,2};
        int[] xs2 = {4,3,3,6};
        int[] ys2 = {4,3,0,-1};
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2));

        assertTrue(ShamosHoey.intersection(segments) == null);
    }

    @Test
    public void testIntersectionExists_False() {
        int[] xs1 = {0,1,2,4};
        int[] ys1 = {3,1,1,2};
        int[] xs2 = {4,3,3,6};
        int[] ys2 = {4,3,0,-1};
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2));

        assertFalse(ShamosHoey.intersectionExists(segments));
    }

    @Test
    public void testIntersection() {
        int[] xs1 = {0, 1, 1, 2, 3, 6, 7};
        int[] ys1 = {0, 7, 7, 4, 3, 2, 4};
        int[] xs2 = {6, 8, 6, 7, 5, 6, 9};
        int[] ys2 = {2, 8, 2, 4, 5, 9, 6};
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2));

        assertTrue(ShamosHoey.intersection(segments).equals(new RPoint2D(6,2)));
    }

    @Test
    public void testIntersectionExists_True() {
        int[] xs1 = {0, 1, 1, 2, 3, 6, 7};
        int[] ys1 = {0, 7, 7, 4, 3, 2, 4};
        int[] xs2 = {6, 8, 6, 7, 5, 6, 9};
        int[] ys2 = {2, 8, 2, 4, 5, 9, 6};
        Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>(CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2));

        assertTrue(ShamosHoey.intersectionExists(segments));
    }

    @Test
    public void testRandomDense() {
        Random rand = new Random();
        final int tests = 50;
        final int maxSegments = 20;
        final int maxSegmentLength = 20;
        final int maxDim = 100;

        int test = 0;
        while(test++ < tests) {

            Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>();

            try {
                while(segments.size() < maxSegments) {
                    int x1 = rand.nextInt(maxDim) - (maxDim/2);
                    int y1 = rand.nextInt(maxDim) - (maxDim/2);
                    int x2 = x1 + rand.nextInt(maxSegmentLength) + 1;
                    int y2 = y1 + rand.nextInt(maxSegmentLength) + 1;

                    RPoint2D p1 = new RPoint2D(x1, y1);
                    RPoint2D p2 = new RPoint2D(x2, y2);

                    if(p1.equals(p2)) continue;

                    segments.add(new RLineSegment2D(p1, p2));
                }

                boolean b1 = ShamosHoey.intersectionExists(segments);
                boolean b2 = BentleyOttmann.intersectionsNaive(segments).size() > 0;

                if(b1 != b2) {
                    System.out.printf("%s != %s\nsegments = %s", b1, b2, segments);
                }

                assertTrue(b1 == b2);

                if(test%100 == 0) {
                    System.out.println("TestShamosHoey.testRandomDense() :: "+test+"/"+tests);
                }
            } catch(Exception e) {
                System.out.println("e.getMessage() :: "+e.getMessage()+"\nsegments :: "+segments);
                assertTrue(false);
            }
        }
    }

    @Test
    public void testRandomSparse() {
        Random rand = new Random();
        final int tests = 50;
        final int maxSegments = 20;
        final int maxSegmentLength = 20;
        final int maxDim = 1000;

        int test = 0;
        while(test++ < tests) {

            Set<RLineSegment2D> segments = new HashSet<RLineSegment2D>();

            try {
                while(segments.size() < maxSegments) {
                    int x1 = rand.nextInt(maxDim) - (maxDim/2);
                    int y1 = rand.nextInt(maxDim) - (maxDim/2);
                    int x2 = x1 + rand.nextInt(maxSegmentLength) + 1;
                    int y2 = y1 + rand.nextInt(maxSegmentLength) + 1;

                    RPoint2D p1 = new RPoint2D(x1, y1);
                    RPoint2D p2 = new RPoint2D(x2, y2);

                    if(p1.equals(p2)) continue;

                    segments.add(new RLineSegment2D(p1, p2));
                }

                boolean b1 = ShamosHoey.intersectionExists(segments);
                boolean b2 = BentleyOttmann.intersectionsNaive(segments).size() > 0;

                if(b1 != b2) {
                    System.out.printf("%s != %s\nsegments = %s", b1, b2, segments);
                }

                assertTrue(b1 == b2);


                if(test%100 == 0) {
                    System.out.println("TestShamosHoey.testRandomSparse() :: "+test+"/"+tests);
                }
            } catch(Exception e) {
                System.out.println("e.getMessage() :: "+e.getMessage()+"\nsegments :: "+segments);
                assertTrue(false);
            }
        }
    }
}
