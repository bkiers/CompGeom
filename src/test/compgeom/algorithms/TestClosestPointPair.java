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

import compgeom.RPoint2D;
import compgeom.util.CGUtil;
import compgeom.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 28, 2010
 * </p>
 */
public class TestClosestPointPair {

    @Test
    public void testFindNaive() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("1 1 2 2 3 2 3 4 1 4 3 6");
        assertTrue(ClosestPointPair.findNaive(points).equals(new Pair<RPoint2D>(new RPoint2D(2,2), new RPoint2D(3,2))));
    }

    @Test
    public void testFind() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("1 1 2 2 3 2 3 4 1 4 3 6");
        assertTrue(ClosestPointPair.find(points).equals(new Pair<RPoint2D>(new RPoint2D(2,2), new RPoint2D(3,2))));

        // all points on the same horizontal line
        points = CGUtil.createRPoint2DList("0 0 2 0 5 0 11 0 -50 0");
        assertTrue(ClosestPointPair.find(points).equals(new Pair<RPoint2D>(new RPoint2D(0,0), new RPoint2D(2,0))));

        // all points on the same vertical line
        points = CGUtil.createRPoint2DList("0 0 0 2 0 5 0 11 0 -50");
        assertTrue(ClosestPointPair.find(points).equals(new Pair<RPoint2D>(new RPoint2D(0,0), new RPoint2D(0,2))));
    }

    @Test
    public void testRandom() {
        Random r = new Random();
        final int maxPoints = 70;
        final int grid = 1000;
        int tests = 50;

        while(--tests > 0) {
            List<RPoint2D> points = new ArrayList<RPoint2D>();
            for(int i = 0; i < maxPoints; i++) {
                int x = r.nextInt(grid);
                int y = r.nextInt(grid);
                RPoint2D p = new RPoint2D(x-(grid/2), y-(grid/2));
                points.add(p);
            }

            Pair<RPoint2D> pairB = ClosestPointPair.findNaive(points);
            Pair<RPoint2D> pairA = ClosestPointPair.find(points);
            
            boolean equal = pairA.m.distanceSquared(pairA.n).equals(pairB.m.distanceSquared(pairB.n));

            if(!equal) {
                System.out.printf("points = %s\npairA = %s (%s)\npairB = %s (%s)\n",
                        points, pairA, pairA.m.distanceSquared(pairA.n), pairB, pairB.m.distanceSquared(pairB.n));
            }
            
            assertTrue(equal);
        }
    }
}
