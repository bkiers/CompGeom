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
package compgeom;

import java.util.List;

/**
 * <p>
 * Represents a bound surface in 2 dimensional space.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: May 10, 2010
 * </p>
 */
public interface RBoundSurface2D extends RCGObject {

    /**
     * Returns <code>true</code> if <code>this</code> contains
     * the point <code>p</code> in it's interior.
     *
     * @param p the point to check if it's inside <code>this</code>.
     * @return <code>true</code> if <code>this</code> contains
     *         the point <code>p</code> in it's interior.
     */
    boolean contains(RPoint2D p);

    /**
     * Returns the points this geometric figure is made of.
     *
     * @return the points this geometric figure is made of.
     */
    List<RPoint2D> getPoints();

    /**
     * <p>
     * Returns <code>true</code> if <code>this</code> and <code>that</code>
     * have the same shape and size. More formally: two geometric figures
     * are said to exhibit geometric congruence (or "be geometrically
     * congruent") iff one can be transformed into the other by an
     * isometry<sup>1</sup>.
     * </p>
     * <p>
     * 1. <a href="http://mathworld.wolfram.com/about/author.html">Weisstein, Eric W.</a>
     * "Geometric Congruence." From <i><a href="http://mathworld.wolfram.com/">MathWorld</a></i>
     * --A Wolfram Web Resource. <a href="http://mathworld.wolfram.com/GeometricCongruence.html">
     * http://mathworld.wolfram.com/GeometricCongruence.html</a>
     * </p>
     *
     * @param that the other figure.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         have the same shape and size.
     */
    boolean isCongruentTo(RBoundSurface2D that);

    /**
     * Returns the maximum x coordinate from this figure.
     *
     * @return the maximum x coordinate from this figure.
     */
    Rational maxX();

    /**
     * Returns the maximum y coordinate from this figure.
     *
     * @return the maximum y coordinate from this figure.
     */
    Rational maxY();

    /**
     * Returns the minimum x coordinate from this figure.
     *
     * @return the minimum x coordinate from this figure.
     */
    Rational minX();

    /**
     * Returns the minimum y coordinate from this figure.
     *
     * @return the minimum y coordinate from this figure.
     */
    Rational minY();

    /**
     * Returns the bounding box of this figure aligned with
     * the x- and y-axis.
     *
     * @return the bounding box of this figure aligned with
     *         the x- and y-axis.
     */
    RRectangle xyAlignedRectangle();
}
