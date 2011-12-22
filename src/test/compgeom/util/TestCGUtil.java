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
import compgeom.Rational;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public class TestCGUtil {

    // Create a 2D array of points to use in the test methods
    private static final int N = 101;
    private static final RPoint2D[][] p = new RPoint2D[N][N];

    static {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                p[i][j] = new RPoint2D(i, j);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllCollinearInvalid() {
        CGUtil.allCollinear(Arrays.asList(p[0][0], p[1][1])); // less than 3 points
    }

    @Test
    public void testAllCollinear() {
        assertTrue(CGUtil.allCollinear(Arrays.asList(p[0][0], p[1][1], p[2][2], p[3][3])));
        assertTrue(CGUtil.allCollinear(Arrays.asList(p[0][0], p[11][11], p[22][22], p[33][33])));
        assertTrue(CGUtil.allCollinear(Arrays.asList(p[0][0], p[55][55], p[12][12], p[3][3])));

        assertFalse(CGUtil.allCollinear(Arrays.asList(p[0][0], p[2][1], p[2][2], p[3][3])));
    }

    @Test
    public void testCollinear() {
        RPoint2D[] a = {p[0][0], new RPoint2D(-2, -2), p[0][0], p[0][0]};
        RPoint2D[] b = {p[0][0], p[1][1], p[5][5], p[5][5]};
        RPoint2D[] c = {p[0][0], p[50][50], p[5][0], p[0][5]};
        boolean[] expected = {true, true, false, false};
        for (int i = 0; i < a.length; i++) {
            assertTrue(CGUtil.collinear(a[i], b[i], c[i]) == expected[i]);
        }
    }

    @Test
    public void testCrossProduct() {
        RPoint2D[] a = {p[0][0], new RPoint2D(-2, -2), p[0][0], p[0][0]};
        RPoint2D[] b = {p[0][0], p[1][1], p[5][5], p[5][5]};
        RPoint2D[] c = {p[0][0], p[50][50], p[5][0], p[0][5]};
        Rational[] expected = {new Rational("0"), new Rational("0"), new Rational("-25"), new Rational("25")};
        for (int i = 0; i < a.length; i++) {
            assertTrue(CGUtil.crossProduct(a[i], b[i], c[i]).equals(expected[i]));
        }
    }

    @Test
    public void testFormsLeftTurn() {
        RPoint2D[] a = {p[0][0], new RPoint2D(-2, -2), p[0][0], p[0][0]};
        RPoint2D[] b = {p[0][0], p[1][1], p[5][5], p[5][5]};
        RPoint2D[] c = {p[0][0], p[50][50], p[5][0], p[0][5]};
        boolean[] expected = {false, false, false, true};
        for (int i = 0; i < a.length; i++) {
            assertTrue(CGUtil.formsLeftTurn(a[i], b[i], c[i]) == expected[i]);
        }
    }

    @Test
    public void testFormsRightTurn() {
        RPoint2D[] a = {p[0][0], new RPoint2D(-2, -2), p[0][0], p[0][0]};
        RPoint2D[] b = {p[0][0], p[1][1], p[5][5], p[5][5]};
        RPoint2D[] c = {p[0][0], p[50][50], p[5][0], p[0][5]};
        boolean[] expected = {false, false, true, false};
        for (int i = 0; i < a.length; i++) {
            assertTrue(CGUtil.formsRightTurn(a[i], b[i], c[i]) == expected[i]);
        }
    }

    @Test
    public void testCreateRPoint2DListString() {
        int[] xs = {1, 2, 3};
        int[] ys = {10, 20, 30};
        List<RPoint2D> points = CGUtil.createRPoint2DList(xs, ys);

        assertTrue(points.equals(CGUtil.createRPoint2DList("1.00000 10/1 4/2 20 9/3 30")));
        assertTrue(points.equals(CGUtil.createRPoint2DList("1 10 2 20    3   30")));
        assertTrue(points.equals(CGUtil.createRPoint2DList("1 10   2 20    3 30")));
        assertTrue(points.equals(CGUtil.createRPoint2DList("1 10 \n\n\n 2 20 3 \n\r\r\n 30")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRPoint2DListStringIllegalArgumentException_1() {
        // no values
        CGUtil.createRPoint2DList("no values");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRPoint2DListStringIllegalArgumentException_2() {
        // uneven number of values
        CGUtil.createRPoint2DList("1,10 2,20 3");
    }

    @Test
    public void testCreateRLineSegment2DListString() {
        int[] xs1 = {1, 3};
        int[] ys1 = {10, 30};
        int[] xs2 = {2, 4};
        int[] ys2 = {20, 40};
        List<RLineSegment2D> segments = CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2);

        assertTrue(segments.equals(CGUtil.createRLineSegment2DList(" 1 10   2 20  3 30  4 40   ")));
        assertTrue(segments.equals(CGUtil.createRLineSegment2DList(" 1   10 2  20/1 3 30 8/2 40.0")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRLineSegment2DListStringIllegalArgumentException_1() {
        // no values
        CGUtil.createRLineSegment2DList("no values");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRLineSegment2DListStringIllegalArgumentException_2() {
        // not a multiple of 4
        CGUtil.createRLineSegment2DList("1,10 -- 2,20 3,4");
    }

    @Test
    public void testCreateRPoint2DList() {
        int[] xs = {1, 2, 3, 4, 5, 6, 7};
        int[] ys = {10, 20, 30, 40, 50, 60, 70};
        List<RPoint2D> points = CGUtil.createRPoint2DList(xs, ys);
        for (int i = 0; i < points.size(); i++) {
            RPoint2D p = points.get(i);
            RPoint2D expected = new RPoint2D(xs[i], ys[i]);
            assertTrue(p.equals(expected));
        }

        // test empty arrays
        CGUtil.createRPoint2DList(new int[]{}, new int[]{});
    }

    @Test
    public void testCreateRLineSegment2DList() {
        final int[] emp = {};

        int[] xs1 = {1, 2, 2, 5, 12, 10, 12};
        int[] ys1 = {2, 5, 7, 1, 6, 5, 4};
        int[] xs2 = {4, 6, 6, 5, 7, 10, 12};
        int[] ys2 = {8, 9, 3, 7, 6, 7, 6};
        List<RLineSegment2D> segments = CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2);

        assertTrue(segments.size() == 7);
        assertTrue(CGUtil.createRLineSegment2DList(emp, emp, emp, emp).size() == 0);

        for (int i = 0; i < xs1.length; i++) {
            assertTrue(segments.get(i).equals(new RLineSegment2D(
                    new RPoint2D(xs2[i], ys2[i]), new RPoint2D(xs1[i], ys1[i]))));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRLineSegment2DList_IllegalArgumentException() {
        int[] xs1 = {1, 2, 2, 5, 12, 10, 12};
        int[] ys1 = {2, 5, 7, 1, 6, 5, 4};
        int[] xs2 = {4, 6, 6, 5, 7, 10, 12};
        int[] ys2 = {8, 9, 3, 7, 6, 7};        // one less
        CGUtil.createRLineSegment2DList(xs1, ys1, xs2, ys2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRPoint2DsIllegalArgumentException() {
        int[] xs = {1, 2, 3, 4, 5, 6, 7};
        int[] ys = {10, 20, 30, 40, 50, 60};
        CGUtil.createRPoint2DList(xs, ys);
    }

    @Test
    public void testGetExtremalIndex() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("2 1 1 2 -1 2 -2 1 -2 -1 -1 -2 1 -2 2 -1");

        assertTrue(CGUtil.getExtremalIndex(points, Extremal.RIGHT_UPPER) == 0);
        assertTrue(CGUtil.getExtremalIndex(points, Extremal.UPPER_RIGHT) == 1);
        assertTrue(CGUtil.getExtremalIndex(points, Extremal.UPPER_LEFT) == 2);
        assertTrue(CGUtil.getExtremalIndex(points, Extremal.LEFT_UPPER) == 3);
        assertTrue(CGUtil.getExtremalIndex(points, Extremal.LEFT_LOWER) == 4);
        assertTrue(CGUtil.getExtremalIndex(points, Extremal.LOWER_LEFT) == 5);
        assertTrue(CGUtil.getExtremalIndex(points, Extremal.LOWER_RIGHT) == 6);
        assertTrue(CGUtil.getExtremalIndex(points, Extremal.RIGHT_LOWER) == 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExtremalIndex_Invalid() {
        List<RPoint2D> points = CGUtil.createRPoint2DList(new int[]{}, new int[]{});
        CGUtil.getExtremalIndex(points, Extremal.RIGHT_UPPER);
    }

    @Test
    public void testGetExtremalPoint() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("2 1 1 2 -1 2 -2 1 -2 -1 -1 -2 1 -2 2 -1");

        assertTrue(CGUtil.getExtremalPoint(points, Extremal.RIGHT_UPPER).equals(new RPoint2D(2,1)));
        assertTrue(CGUtil.getExtremalPoint(points, Extremal.UPPER_RIGHT).equals(new RPoint2D(1,2)));
        assertTrue(CGUtil.getExtremalPoint(points, Extremal.UPPER_LEFT).equals(new RPoint2D(-1,2)));
        assertTrue(CGUtil.getExtremalPoint(points, Extremal.LEFT_UPPER).equals(new RPoint2D(-2,1)));
        assertTrue(CGUtil.getExtremalPoint(points, Extremal.LEFT_LOWER).equals(new RPoint2D(-2,-1)));
        assertTrue(CGUtil.getExtremalPoint(points, Extremal.LOWER_LEFT).equals(new RPoint2D(-1,-2)));
        assertTrue(CGUtil.getExtremalPoint(points, Extremal.LOWER_RIGHT).equals(new RPoint2D(1,-2)));
        assertTrue(CGUtil.getExtremalPoint(points, Extremal.RIGHT_LOWER).equals(new RPoint2D(2,-1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExtremalPoint_Invalid() {
        List<RPoint2D> points = CGUtil.createRPoint2DList(new int[]{}, new int[]{});
        CGUtil.getExtremalPoint(points, Extremal.RIGHT_UPPER);
    }

    @Test
    public void testGetFrequencyMap_Varargs() {
        Map<String, Integer> map = CGUtil.getFrequencyMap();

        assertTrue(map.isEmpty());

        map = CGUtil.getFrequencyMap("a", "a", "b", "b", "a", "c", "a");

        assertTrue(map.get("a") == 4);
        assertTrue(map.get("b") == 2);
        assertTrue(map.get("c") == 1);
    }

    @Test
    public void testGetFrequencyMap_Collection() {
        Map<String, Integer> map = CGUtil.getFrequencyMap(new ArrayList<String>());

        assertTrue(map.isEmpty());

        map = CGUtil.getFrequencyMap(Arrays.asList("a", "a", "b", "b", "a", "c", "a"));

        assertTrue(map.get("a") == 4);
        assertTrue(map.get("b") == 2);
        assertTrue(map.get("c") == 1);
    }

    @Test
    public void testMoreExtremeThan() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("2 1 1 2 -1 2 -2 1 -2 -1 -1 -2 1 -2 2 -1");

        // the same points
        assertFalse(CGUtil.moreExtremeThan(points.get(0), points.get(0), Extremal.LEFT_LOWER));

        assertTrue(CGUtil.moreExtremeThan(points.get(4), points.get(3), Extremal.LEFT_LOWER));
        assertFalse(CGUtil.moreExtremeThan(points.get(3), points.get(4), Extremal.LEFT_LOWER));

        assertTrue(CGUtil.moreExtremeThan(points.get(3), points.get(4), Extremal.LEFT_UPPER));
        assertFalse(CGUtil.moreExtremeThan(points.get(4), points.get(3), Extremal.LEFT_UPPER));

        assertTrue(CGUtil.moreExtremeThan(points.get(7), points.get(0), Extremal.RIGHT_LOWER));
        assertFalse(CGUtil.moreExtremeThan(points.get(0), points.get(7), Extremal.RIGHT_LOWER));

        assertTrue(CGUtil.moreExtremeThan(points.get(0), points.get(7), Extremal.RIGHT_UPPER));
        assertFalse(CGUtil.moreExtremeThan(points.get(7), points.get(0), Extremal.RIGHT_UPPER));

        assertTrue(CGUtil.moreExtremeThan(points.get(5), points.get(6), Extremal.LOWER_LEFT));
        assertFalse(CGUtil.moreExtremeThan(points.get(6), points.get(5), Extremal.LOWER_LEFT));

        assertTrue(CGUtil.moreExtremeThan(points.get(6), points.get(5), Extremal.LOWER_RIGHT));
        assertFalse(CGUtil.moreExtremeThan(points.get(5), points.get(6), Extremal.LOWER_RIGHT));

        assertTrue(CGUtil.moreExtremeThan(points.get(2), points.get(1), Extremal.UPPER_LEFT));
        assertFalse(CGUtil.moreExtremeThan(points.get(1), points.get(2), Extremal.UPPER_LEFT));

        assertTrue(CGUtil.moreExtremeThan(points.get(1), points.get(2), Extremal.UPPER_RIGHT));
        assertFalse(CGUtil.moreExtremeThan(points.get(2), points.get(1), Extremal.UPPER_RIGHT));
    }

    /*
                  |
               c  2  b
            d     1     a
          --2--1--+--1--2--
            e     1     h
               f  2  g
                  |
    */
    @Test
    public void testSort() {
        //               a     b      c      d      e       f      g      h
        String data = " 2 1   1 2   -1 2   -2 1   -2 -1   -1 -2   1 -2   2 -1";
        List<RPoint2D> points = CGUtil.createRPoint2DList(data);
        final RPoint2D a = points.get(0);
        final RPoint2D b = points.get(1);
        final RPoint2D c = points.get(2);
        final RPoint2D d = points.get(3);
        final RPoint2D e = points.get(4);
        final RPoint2D f = points.get(5);
        final RPoint2D g = points.get(6);
        final RPoint2D h = points.get(7);

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.UPPER_RIGHT);
        assertTrue(points.equals(Arrays.asList(b,c,a,d,h,e,g,f)));

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.UPPER_LEFT);
        assertTrue(points.equals(Arrays.asList(c,b,d,a,e,h,f,g)));

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.LEFT_UPPER);
        assertTrue(points.equals(Arrays.asList(d,e,c,f,b,g,a,h)));

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.LEFT_LOWER);
        assertTrue(points.equals(Arrays.asList(e,d,f,c,g,b,h,a)));

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.LOWER_RIGHT);
        assertTrue(points.equals(Arrays.asList(g,f,h,e,a,d,b,c)));

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.LOWER_LEFT);
        assertTrue(points.equals(Arrays.asList(f,g,e,h,d,a,c,b)));

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.RIGHT_LOWER);
        assertTrue(points.equals(Arrays.asList(h,a,g,b,f,c,e,d)));

        points = CGUtil.createRPoint2DList(data);
        CGUtil.sort(points, Extremal.RIGHT_UPPER);
        assertTrue(points.equals(Arrays.asList(a,h,b,g,c,f,d,e)));
    }

    @Test
    public void testSortAndGetList() {
        //               a     b      c      d      e       f      g      h
        String data = " 2 1   1 2   -1 2   -2 1   -2 -1   -1 -2   1 -2   2 -1";
        List<RPoint2D> points = CGUtil.createRPoint2DList(data);
        final RPoint2D a = points.get(0);
        final RPoint2D b = points.get(1);
        final RPoint2D c = points.get(2);
        final RPoint2D d = points.get(3);
        final RPoint2D e = points.get(4);
        final RPoint2D f = points.get(5);
        final RPoint2D g = points.get(6);
        final RPoint2D h = points.get(7);

        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.UPPER_RIGHT).equals(Arrays.asList(b,c,a,d,h,e,g,f)));
        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.UPPER_LEFT).equals(Arrays.asList(c,b,d,a,e,h,f,g)));
        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.LEFT_UPPER).equals(Arrays.asList(d,e,c,f,b,g,a,h)));
        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.LEFT_LOWER).equals(Arrays.asList(e,d,f,c,g,b,h,a)));
        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.LOWER_RIGHT).equals(Arrays.asList(g,f,h,e,a,d,b,c)));
        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.LOWER_LEFT).equals(Arrays.asList(f,g,e,h,d,a,c,b)));
        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.RIGHT_LOWER).equals(Arrays.asList(h,a,g,b,f,c,e,d)));
        assertTrue(CGUtil.sortAndGetList(CGUtil.createRPoint2DList(data),Extremal.RIGHT_UPPER).equals(Arrays.asList(a,h,b,g,c,f,d,e)));
    }

    @Test
    public void testRemoveCollinearPoints() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("2 0 4 2 6 4 5 7 0 4 1 2 2 0");
        assertTrue(CGUtil.removeCollinear(points).equals(CGUtil.createRPoint2DList("2 0 6 4 5 7 0 4 2 0")));
        points = CGUtil.createRPoint2DList("2 1 5 1 6 4 4 6 2 5 2 3 2 1");
        assertTrue(CGUtil.removeCollinear(points).equals(CGUtil.createRPoint2DList("2 1 5 1 6 4 4 6 2 5 2 1")));
    }

    @Test
    public void testRemoveCollinear() {
        List<RPoint2D> points =   CGUtil.createRPoint2DList("1 1 1 3 1 5 3 6 5 7 6 5 7 3 5 1 3 1 1 1");
        List<RPoint2D> removed = CGUtil.removeCollinear(points);
        assertTrue(removed.equals(CGUtil.createRPoint2DList("1 1     1 5     5 7     7 3 5 1     1 1")));
    }

    @Test
    public void testAllClockWise() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("1 1 3 3 6 3 6 1 4 0");
        assertTrue(CGUtil.allClockWise(points));

        points = CGUtil.createRPoint2DList("1 1 4 0 6 1 6 3 3 3");
        assertFalse(CGUtil.allClockWise(points));

        points = CGUtil.createRPoint2DList("1 1 4 2 6 1 6 3 3 3");
        assertFalse(CGUtil.allClockWise(points));

        assertTrue(CGUtil.allClockWise(CGUtil.createRPoint2DList("1 1 3 3"))); // less than 3 points

        assertTrue(CGUtil.allClockWise(CGUtil.createRPoint2DList("1 1 3 3 5 5 10 10"))); // all collinear
    }

    @Test
    public void testAllCounterClockWise() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("1 1 3 3 6 3 6 1 4 0");
        assertFalse(CGUtil.allCounterClockWise(points));

        points = CGUtil.createRPoint2DList("1 1 4 0 6 1 6 3 3 3");
        assertTrue(CGUtil.allCounterClockWise(points));

        points = CGUtil.createRPoint2DList("1 1 4 2 6 1 6 3 3 3");
        assertFalse(CGUtil.allClockWise(points));

        assertTrue(CGUtil.allCounterClockWise(CGUtil.createRPoint2DList("1 1 3 3"))); // less than 3 points

        assertTrue(CGUtil.allCounterClockWise(CGUtil.createRPoint2DList("1 1 3 3 5 5 10 10"))); // all collinear
    }
}

