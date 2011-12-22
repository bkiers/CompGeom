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

import compgeom.RPoint2D;

import java.util.*;

/**
 * <p>
 * An enum used to describe an extremal point in the plane. For example, in case
 * of {@link #UPPER_LEFT}, two points are first compared to see whose y coordinate
 * (UPPER) is the most and in case two points have the same y coordinate, their
 * x coordinates (LEFT) are compared to see which one is the least.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 28, 2010
 * </p>
 */
public enum Extremal {

    /**
     * Denotes the extremal whose first priority is 'up' and whose second priority is 'left'
     */
    UPPER_LEFT  (Direction.UPPER , Direction.LEFT ),

    /**
     * Denotes the extremal whose first priority is 'up' and whose second priority is 'right'.
     */
    UPPER_RIGHT (Direction.UPPER , Direction.RIGHT),

    /**
     * Denotes the extremal whose first priority is 'low' and whose second priority is 'left'.
     */
    LOWER_LEFT  (Direction.LOWER , Direction.LEFT ),

    /**
     * Denotes the extremal whose first priority is 'low' and whose second priority is 'right'.
     */
    LOWER_RIGHT (Direction.LOWER , Direction.RIGHT),

    /**
     * Denotes the extremal whose first priority is 'left' and whose second priority is 'up'.
     */
    LEFT_UPPER (Direction.LEFT , Direction.UPPER),

    /**
     * Denotes the extremal whose first priority is 'left' and whose second priority is 'low'.
     */
    LEFT_LOWER (Direction.LEFT , Direction.LOWER),

    /**
     * Denotes the extremal whose first priority is 'right' and whose second priority is 'up'.
     */
    RIGHT_UPPER (Direction.RIGHT , Direction.UPPER),

    /**
     * Denotes the extremal whose first priority is 'right' and whose second priority is 'low'.
     */
    RIGHT_LOWER (Direction.RIGHT , Direction.LOWER);

    /**
     * The direction.
     */
    private static enum Direction { UPPER, LOWER, LEFT, RIGHT }

    /**
     * The primary direction.
     */
    private final Direction primary;

    /**
     * The secondary direction.
     */
    private final Direction secondary;

    /**
     * <p>
     * The comparator associated with this extremal. For example, sorting
     * points using {@link Extremal#LOWER_RIGHT#comparator}, the end result will
     * be a sorted collection where the points with the <b>least y coordinate</b>
     * (LOWER) will be placed at the start of the collection. In case of equal y
     * coordinates, the one with the <b>larger x coordinate</b> (RIGHT) is placed
     * before the other point.
     * </p>
     * <p>
     * See: <br />
     * {@link compgeom.util.CGUtil#sort(java.util.List, Extremal)}  <br />
     * {@link compgeom.util.CGUtil#sortAndGetList(java.util.Collection, Extremal)}
     * </p>
     */
    public final Comparator<RPoint2D> comparator;

    /**
     * Creates a new Extremal enum whose primary direction is p, and
     * secondary direction is s.
     *
     * @param p the primary direction.
     * @param s the secondary direction.
     */
    private Extremal(Direction p, Direction s) {
        primary = p;
        secondary = s;
        comparator = new Comparator<RPoint2D>(){
            @Override
            public int compare(RPoint2D p1, RPoint2D p2) {
                if(p1.equals(p2)) {
                    return 0;
                }
                return moreExtremeThan(p1, p2) ? -1 : 1;
            }
        };
    }

    /**
     * <p>
     * Checks if <code>p1</code> is more extreme than <code>p2</code>. Note
     * that this method returns <code>false</code> in case <code>p1</code>
     * equals <code>p2</code>.
     * </p>
     * <p>
     * For example, the following: <br />
     * </p>
     * <pre>
     * RPoint2D p1 = new RPoint2D(4,4);
     * RPoint2D p2 = new RPoint2D(5,3);
     * System.out.println(Extremal.UPPER_LEFT.moreExtremeThan(p1, p2));
     * </pre>
     * <p>
     * will print <code>true</code> since <code>p1</code> lies higher
     * than <code>p2</code>. And although <code>p3</code> and <code>p4</code>
     * have the same y coordinate:
     * </p>
     * <pre>
     * RPoint2D p3 = new RPoint2D(4,4);
     * RPoint2D p4 = new RPoint2D(5,4);
     * System.out.println(Extremal.UPPER_LEFT.moreExtremeThan(p3, p4));
     * </pre>
     * <p>
     * <code>true</code> will still be printed since the second priority, LEFT,
     * is met: <code>p3</code>'s x coordinate is less than <code>p4</code>'s
     * x coordinate.
     * </p>
     *
     * @param p1 the first point.
     * @param p2 the second point.
     * @return <code>true</code> iff <code>p1</code> is more extreme
     *         than <code>p2</code>.
     */
    public boolean moreExtremeThan(RPoint2D p1, RPoint2D p2) {
        // First check if the primary direction is conclusive...
        if((primary == Direction.UPPER      && p1.y.isMoreThan(p2.y)) ||
                (primary == Direction.LOWER && p1.y.isLessThan(p2.y)) ||
                (primary == Direction.LEFT  && p1.x.isLessThan(p2.x)) ||
                (primary == Direction.RIGHT && p1.x.isMoreThan(p2.x))) {
            return true;
        }
        // else see if we need to check the secondary direction.
        else if(((primary == Direction.UPPER || primary == Direction.LOWER) && p1.y.equals(p2.y)) ||
                ((primary == Direction.LEFT || primary == Direction.RIGHT)  && p1.x.equals(p2.x))) {
            // Check the secondary direction.
            if((secondary == Direction.UPPER      && p1.y.isMoreThan(p2.y)) ||
                    (secondary == Direction.LOWER && p1.y.isLessThan(p2.y)) ||
                    (secondary == Direction.LEFT  && p1.x.isLessThan(p2.x)) ||
                    (secondary == Direction.RIGHT && p1.x.isMoreThan(p2.x))) {
                return true;
            }
        }
        return false;
    }
}
