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
import compgeom.Rational;
import compgeom.RLineSegment2D;
import compgeom.RPoint2D;
import compgeom.util.parser.RationalParser;

import java.math.BigInteger;
import java.util.*;

/**
 * <p>
 * A computational geometry (CG) utility class.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
public final class CGUtil {


    /**
     * No need to instantiate this class.
     */
    private CGUtil() {
    }

    /**
     * Returns <code>true</code> if all <code>points</code> form
     * a clock wise (right) rotation. If <code>points</code> are
     * less than 3 or if all <code>points</code> are collinear,
     * <code>true</code> is also returned.
     *
     * @param points the points to check.
     * @return <code>true</code> if all <code>points</code> form
     *         a clock wise (right) rotation.
     */
    public static boolean allClockWise(List<RPoint2D> points) {
        if (points.size() < 3 || allCollinear(points)) {
            return true;
        }
        for (int i = 2; i < points.size(); i++) {
            RPoint2D a = points.get(i - 2);
            RPoint2D b = points.get(i - 1);
            RPoint2D c = points.get(i);
            if (CGUtil.formsLeftTurn(a, b, c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns <code>true</code> if all <code>points</code> are collinear.
     *
     * @param points the list of points to check.
     * @return <code>true</code> if all <code>points</code> are collinear.
     * @throws IllegalArgumentException if <code>points</code> contains less
     *                                  than 3 points.
     */
    public static boolean allCollinear(List<RPoint2D> points) throws IllegalArgumentException {
        if (points.size() < 3) {
            throw new IllegalArgumentException("List must contain at least 3 points");
        }
        for (int i = 0; i < points.size() - 2; i++) {
            if (!CGUtil.collinear(points.get(i), points.get(i + 1), points.get(i + 2))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns <code>true</code> if all <code>points</code> form
     * a counter clock wise (left) rotation. If <code>points</code> are
     * less than 3 or if all <code>points</code> are collinear,
     * <code>true</code> is also returned.
     *
     * @param points the points to check.
     * @return <code>true</code> if all <code>points</code> form
     *         a counter clock wise (left) rotation.
     */
    public static boolean allCounterClockWise(List<RPoint2D> points) {
        return points.size() < 3 || allCollinear(points) || !allClockWise(points);
    }

    /**
     * Checks whether <code>a</code>, <code>b</code> and <code>c</code>
     * are collinear. Note that the order of the points does not matter:
     * as <code>collinear((1,1), (3,3), (5,5))</code>, so will
     * <code>collinear((1,1), (5,5), (3,3))</code> return <code>true</code>.
     *
     * @param a the starting point.
     * @param b the middle point.
     * @param c the last point.
     * @return <code>true</code> iff the points are collinear.
     */
    public static boolean collinear(RPoint2D a, RPoint2D b, RPoint2D c) {
        return crossProduct(a, b, c).equals(Rational.ZERO);
    }

    /**
     * <p>
     * Finds points in the <code>data</code> String. The <code>data</code>
     * String is first being split on it's white spaces after which each individual
     * token in the array is parsed into a {@link compgeom.Rational}.
     * </p>
     * <p>So for example, the following Strings will be properly parsed and
     * converted into a <code>List</code> of <code>RPoint2D</code>'s:</p>
     * <ul>
     * <li><code>"10 20 30 40 50 60"</code></li>
     * <li><code>"250.99 96/5 -252 11"</code></li>
     * </ul>
     * <p>But these will throw an exception:</p>
     * <ul>
     * <li><code>""</code> (an empty String)</li>
     * <li><code>"250.99 96/5 -252"</code> (uneven amount of numbers)</li>
     * </ul>
     *
     * @param data the String containing the numbers.
     * @return a list of points.
     * @throws IllegalArgumentException if <code>data</code> contains an uneven number of
     *                                  {@link compgeom.Rational}'s, or no none at all.
     *                                  Or if one of the rationals in <code>data</code> is
     *                                  not properly formatted.
     * @see compgeom.Rational#Rational(String)
     */
    public static List<RPoint2D> createRPoint2DList(String data) throws IllegalArgumentException {
        if (data.matches("\\s*")) {
            return new ArrayList<RPoint2D>();
        }
        List<Rational> rationals = new ArrayList<Rational>();
        List<RPoint2D> points = new ArrayList<RPoint2D>();
        String[] tokens = data.trim().split("\\s++");
        for (String t : tokens) {
            BigInteger[] nd = RationalParser.parse(t);
            rationals.add(new Rational(nd[0], nd[1]));
        }
        if (rationals.isEmpty()) {
            throw new IllegalArgumentException("'data' does not contain any valid Rationals");
        }
        if (rationals.size() % 2 == 1) {
            throw new IllegalArgumentException("'data' contains an uneven number of Rationals");
        }
        for (int i = 1; i < rationals.size(); i += 2) {
            points.add(new RPoint2D(rationals.get(i - 1), rationals.get(i)));
        }
        return points;
    }

    /**
     * Helper method to create a <code>java.util.List</code> of
     * <code>RPoint2D</code>'s given an array of x- and y-coordinates.
     *
     * @param xs the x-coordinates.
     * @param ys the y-coordinates.
     * @return a <code>java.util.List</code> of <code>RPoint2D</code>'s
     *         given an array of X- and y-coordinates.
     * @throws IllegalArgumentException if <code>xs</code> and <code>ys</code>
     *                                  don't have the same size.
     */
    public static List<RPoint2D> createRPoint2DList(int[] xs, int[] ys) throws IllegalArgumentException {
        if (xs.length != ys.length) {
            throw new IllegalArgumentException("xs.length != ys.length");
        }
        List<RPoint2D> points = new ArrayList<RPoint2D>();
        for (int i = 0; i < xs.length; i++) {
            points.add(new RPoint2D(xs[i], ys[i]));
        }
        return points;
    }

    /**
     * <p>
     * Finds line segments in the <code>data</code> String. The <code>data</code>
     * String is first being split on it's white spaces after which each individual
     * token in the array is parsed into a {@link compgeom.Rational}.
     * </p>
     * <p>
     * So for example, the following Strings will be properly parsed and
     * converted into a <code>List</code> of <code>RLineSegment2D</code>'s:
     * </p>
     * <ul>
     * <li><code>"250 96 252 96 249 94 251 95 249 95 252 97"</code></li>
     * <li><code>"250 96 251 95"</code></li>
     * </ul>
     * <p>But these will throw an exception:</p>
     * <ul>
     * <li><code>""</code> (an empty String)</li>
     * <li><code>"250 9 252 6 251 5"</code> (6 values: not a multiple of 4)</li>
     * </ul>
     *
     * @param data the String containing the numbers.
     * @return a list of line segments.
     * @throws IllegalArgumentException if <code>data</code> contains a total number of
     *                                  {@link compgeom.Rational}'s that is not a multiple of 4, or
     *                                  none at all.
     *                                  Or if one of the rationals in <code>data</code> is
     *                                  not properly formatted.
     * @see compgeom.Rational#Rational(String)
     */
    public static List<RLineSegment2D> createRLineSegment2DList(String data) throws IllegalArgumentException {
        if (data.matches("\\s*")) {
            return new ArrayList<RLineSegment2D>();
        }
        List<RLineSegment2D> segments = new ArrayList<RLineSegment2D>();
        List<RPoint2D> points = createRPoint2DList(data);
        if (points.size() % 2 == 1) {
            throw new IllegalArgumentException("'data' does not contain a multiple of 4 Rationals");
        }
        for (int i = 1; i < points.size(); i += 2) {
            segments.add(new RLineSegment2D(points.get(i - 1), points.get(i)));
        }
        return segments;
    }

    /**
     * Creates a list of <code>RLineSegment2D</code>s based on four parallel arrays:
     * <pre>
     * segment[0] = point(xs1[0],ys1[0]) <-> point(xs2[0],ys2[0])
     * segment[1] = point(xs1[1],ys1[1]) <-> point(xs2[1],ys2[1])
     * ...
     * segment[n] = point(xs1[n],ys1[n]) <-> point(xs2[n],ys2[n])
     * </pre>
     *
     * @param xs1 the x coordinates from the first point.
     * @param ys1 the y coordinates from the first point.
     * @param xs2 the x coordinates from the second point.
     * @param ys2 the y coordinates from the second point.
     * @return a list of <code>RLineSegment2D</code>s based on four parallel arrays.
     * @throws IllegalArgumentException when the arrays <code>xs1</code>,
     *                                  <code>ys1</code>, <code>xs2</code>
     *                                  and <code>ys2</code> do not have
     *                                  the same size.
     */
    public static List<RLineSegment2D> createRLineSegment2DList(int[] xs1, int[] ys1, int[] xs2, int[] ys2)
            throws IllegalArgumentException {
        if (xs1.length != ys1.length || xs1.length != xs2.length || xs1.length != ys2.length) {
            throw new IllegalArgumentException("all four array must have the same size");
        }
        List<RLineSegment2D> segments = new ArrayList<RLineSegment2D>();
        List<RPoint2D> pts1 = createRPoint2DList(xs1, ys1);
        List<RPoint2D> pts2 = createRPoint2DList(xs2, ys2);
        for (int i = 0; i < pts1.size(); i++) {
            segments.add(new RLineSegment2D(pts1.get(i), pts2.get(i)));
        }
        return segments;
    }

    /**
     * <p>Calculates the cross product of two vectors denoted by the three
     * points <code>a</code>, <code>b</code> and <code>c</code>.</p>
     * <p>In two dimensions, the cross product for <code>U = (U_x,U_y)</code>
     * and <code>V = (V_x,V_y)</code> is:</p>
     * <p>
     * <pre>UxV
     * == det(UV)
     * == (U_x * V_y) - (U_y * V_x)
     * == ((b.x-a.x)*(c.y-a.y)) - ((c.x-a.x)*(b.y-a.y))</pre>
     * </p>
     * <p>where <code>det(A)</code> is the determinant.</p>
     * <p>See
     * <a href="http://mathworld.wolfram.com/CrossProduct.html">http://mathworld.wolfram.com/CrossProduct.html</a>
     * </p>
     *
     * @param a the starting point.
     * @param b the middle point.
     * @param c the last point: it makes the turn to the left or right
     *          (or is collinear with <code>a</code> and <code>b</code>).
     * @return the cross product of two vectors denoted by the three
     *         points <code>a</code>, <code>b</code> and <code>c</code>
     */
    public static Rational crossProduct(RPoint2D a, RPoint2D b, RPoint2D c) {
        Rational xA = a.x, yA = a.y;
        Rational xB = b.x, yB = b.y;
        Rational xC = c.x, yC = c.y;
        return (xB.subtract(xA).multiply(yC.subtract(yA))).subtract(
                xC.subtract(xA).multiply(yB.subtract(yA)));
    }

    /**
     * Checks whether <code>a</code>, <code>b</code> and <code>c</code>
     * form a left turn: where <code>a</code> 'walks' towards
     * <code>c</code> through <code>b</code>.
     *
     * @param a the starting point.
     * @param b the middle point.
     * @param c the last point.
     * @return <code>true</code> iff the path <code>a->b->c</code>
     *         forms a left turn
     */
    public static boolean formsLeftTurn(RPoint2D a, RPoint2D b, RPoint2D c) {
        return crossProduct(a, b, c).isMoreThan(Rational.ZERO);
    }

    /**
     * Checks whether <code>a</code>, <code>b</code> and <code>c</code>
     * form a right turn, where <code>a</code> 'walks' towards
     * <code>c</code> through <code>b</code>.
     *
     * @param a the starting point.
     * @param b the middle point.
     * @param c the last point.
     * @return <code>true</code> iff the path <code>a->b->c</code>
     *         forms a right turn
     */
    public static boolean formsRightTurn(RPoint2D a, RPoint2D b, RPoint2D c) {
        return crossProduct(a, b, c).isLessThan(Rational.ZERO);
    }

    /**
     * <p>
     * Returns the index of the extremal point from a list of points
     * in <code>O(n)</code> time.
     * </p>
     *
     * @param points the list of points to get the extreme point's index from.
     * @param e      the {@link compgeom.util.Extremal}.
     * @return the index of the extremal point from a given list of points.
     * @throws IllegalArgumentException if <code>points</code> is empty.
     */
    public static int getExtremalIndex(List<RPoint2D> points, Extremal e)
            throws IllegalArgumentException {

        if (points.isEmpty()) {
            throw new IllegalArgumentException("points must contain at least one element");
        }

        RPoint2D extremalPoint = points.get(0);
        int extremalIndex = 0;

        for (int i = 1; i < points.size(); i++) {
            RPoint2D p = points.get(i);
            if (e.moreExtremeThan(p, extremalPoint)) {
                extremalPoint = p;
                extremalIndex = i;
            }
        }
        return extremalIndex;
    }

    /**
     * <p>
     * Returns the extremal point from a list of points in <code>O(n)</code> time .
     * </p>
     *
     * @param points the list of point to get the extreme point from.
     * @param e      the {@link compgeom.util.Extremal}.
     * @return the extremal point from a given list of points.
     * @throws IllegalArgumentException if <code>points</code> is empty.
     */
    public static RPoint2D getExtremalPoint(List<RPoint2D> points, Extremal e)
            throws IllegalArgumentException {

        int index = getExtremalIndex(points, e);
        return points.get(index);
    }

    /**
     * Returns a frequency table of all <code>K</code> objects in
     * the <code>keys</code> varargs parameter. For example, if
     * varargs looks like: <code>[a, b, a, a, b, c, a]</code>, this
     * method returns <code>{a:4, b:2, c:1}</code> (not particularly
     * in that order!).
     *
     * @param keys the keys to build a frequency map of.
     * @return a frequency table of all <code>K</code> objects in
     *         the <code>keys</code> varargs parameter.
     */
    public static <K> Map<K, Integer> getFrequencyMap(K... keys) {
        Map<K, Integer> map = new LinkedHashMap<K, Integer>();
        for (K k : keys) {
            putFrequencyMap(map, k);
        }
        return map;
    }

    /**
     * Returns a frequency table of all <code>K</code> objects in
     * the <code>keys</code> collection. For example, if the
     * collection looks like: <code>[a, b, a, a, b, c, a]</code>,
     * this method returns <code>{a:4, b:2, c:1}</code> (not
     * particularly in that order!).
     *
     * @param keys the keys to build a frequency map of.
     * @return a frequency table of all <code>K</code> objects in
     *         the <code>keys</code> collection.
     */
    public static <K> Map<K, Integer> getFrequencyMap(Collection<K> keys) {
        Map<K, Integer> map = new LinkedHashMap<K, Integer>();
        for (K k : keys) {
            putFrequencyMap(map, k);
        }
        return map;
    }

    /**
     * <p>
     * Checks if <code>p1</code> is more extreme than <code>p2</code>. Note
     * that this method returns <code>false</code> in case <code>p1</code>
     * equals <code>p2</code>.
     * </p>
     * <p>
     * This is a convenience method for:
     * {@link Extremal#moreExtremeThan(compgeom.RPoint2D, compgeom.RPoint2D)}
     * </p>
     *
     * @param p1 the first point.
     * @param p2 the second point.
     * @param e  the {@link Extremal}.
     * @return <code>true</code> iff <code>p1</code> is more extreme
     *         than <code>p2</code> based on <code>e</code>.
     */
    public static boolean moreExtremeThan(RPoint2D p1, RPoint2D p2, Extremal e) {
        return e.moreExtremeThan(p1, p2);
    }


    /**
     * Puts a new <code>key</code> in a <code>Map</code> and increasing the
     * count of the frequency if <code>key</code> is already present. If
     * <code>key</code> is not present, the <code>Rational</code> value will
     * be initialized to 1.
     *
     * @param map the map to store <code>key</code> in.
     * @param key the key to store in the map and count the frequency of.
     */
    private static <K> void putFrequencyMap(Map<K, Integer> map, K key) {
        Integer count = map.remove(key);
        if (count == null) count = 0;
        count++;
        map.put(key, count);
    }

    /**
     * Returns the points from a given path but then without collinear points.
     * For example, from the path <code>(1,1) (1,3) (1,5) (3,6) (5,7)</code>
     * this methods returns a new list containing the points:
     * <code>(1,1) (1,5) (5,7)</code>.
     *
     * @param path the list of points.
     * @return the points from a given path but then without collinear points.
     */
    public static List<RPoint2D> removeCollinear(List<RPoint2D> path) {
        if (path.size() < 3) {
            return new ArrayList<RPoint2D>(path);
        }

        List<RPoint2D> cleaned = new ArrayList<RPoint2D>();
        final RPoint2D start = path.get(0);
        cleaned.add(start);
        Rational previousSlope = new RLine2D(start, path.get(1)).slope;
        for (int i = 1; i < path.size() - 1; i++) {
            Rational nextSlope = new RLine2D(path.get(i), path.get(i + 1)).slope;
            if (!nextSlope.equals(previousSlope)) {
                previousSlope = nextSlope;
                cleaned.add(path.get(i));
            }
        }
        cleaned.add(path.get(path.size() - 1));
        return cleaned;
    }

    /**
     * Sorts the array of points <i>in place</i> based on a given
     * {@link Extremal} in <code>O(n*log(n))</code> time.
     * For example, sorting points using <code>sort(RPoint2D[])</code>,
     * the array will be sorted where the points with the <b>least y coordinate</b>
     * (LOWER) will be placed at the start of the array. In case of equal y
     * coordinates, the point with the <b>larger x coordinate</b> (RIGHT) is
     * placed before the other point.
     *
     * @param points the array of points to sort <i>in place</i>.
     * @param e      the {@link Extremal} to sort on.
     */
    public static void sort(RPoint2D[] points, Extremal e) {
        Arrays.sort(points, e.comparator);
    }

    /**
     * Sorts the list of points <i>in place</i> based on a given
     * {@link Extremal} in <code>O(n*log(n))</code> time.
     * For example, sorting points using <code>sort(List)</code>,
     * the List will be sorted where the points with the <b>least y coordinate</b>
     * (LOWER) will be placed at the start of the List. In case of equal y
     * coordinates, the point with the <b>larger x coordinate</b> (RIGHT) is
     * placed before the other point.
     *
     * @param points the list of points to sort <i>in place</i>.
     * @param e      the {@link Extremal} to sort on.
     */
    public static void sort(List<RPoint2D> points, Extremal e) {
        Collections.sort(points, e.comparator);
    }

    /**
     * <p>
     * Sorts the collection of points based on a given {@link Extremal}
     * in <code>O(n*log(n))</code> time and returns
     * the sorted points as an array. For example, sorting points using
     * <code>sortAndGetList(List)</code>, the new List
     * being returned will be sorted where the points with the <b>least y
     * coordinate</b> (LOWER) will be placed at the start of the List. In case
     * of equal y coordinates, the point with the <b>larger x coordinate</b>
     * (RIGHT) is placed before the other point.
     * </p>
     *
     * @param points the collection of points to sort.
     * @param e      the {@link Extremal} to sort on.
     * @return a new sorted List containing the points from the
     *         collection <code>points</code>.
     */
    public static RPoint2D[] sortAndGetArray(Collection<RPoint2D> points, Extremal e) {
        RPoint2D[] array = points.toArray(new RPoint2D[points.size()]);
        sort(array, e);
        return array;
    }

    /**
     * <p>
     * Sorts the collection of points based on a given {@link Extremal}
     * in <code>O(n*log(n))</code> time and returns
     * the sorted points as a new List. For example, sorting points using
     * <code>sortAndGetList(List)</code>, the new List
     * being returned will be sorted where the points with the <b>least y
     * coordinate</b> (LOWER) will be placed at the start of the List. In case
     * of equal y coordinates, the point with the <b>larger x coordinate</b>
     * (RIGHT) is placed before the other point.
     * </p>
     *
     * @param points the collection of points to sort.
     * @param e      the {@link Extremal} to sort on.
     * @return a new sorted List containing the points from the
     *         collection <code>points</code>.
     */
    public static List<RPoint2D> sortAndGetList(Collection<RPoint2D> points, Extremal e) {
        List<RPoint2D> list = new ArrayList<RPoint2D>(points);
        sort(list, e);
        return list;
    }
}
