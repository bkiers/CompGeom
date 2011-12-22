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

import compgeom.RPolygon2D;

/**
 * TODO
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: May 16, 2010
 * </p>
 */
public final class Partition {

    /**
     * No need to instantiate this class
     */
    private Partition() {
    }

    /**
     * Partitions a simple polygon into one or more monotone pieces.
     * It does so in <code>O(n * log(n))</code> time where <code>n</code>
     * is the number of points in <code>polygon</code>.
     *
     * @param polygon the simple polygon to partition.
     * @return an array of <code>RPolygon2D</code>'s that
     *         represent the monotone pieces of <code>polygon</code>.
     * @throws IllegalArgumentException if <code>polygon</code> is not
     *                                  simple (complex).
     */
    public static RPolygon2D[] monotone(RPolygon2D polygon) throws IllegalArgumentException {
        if (polygon.isComplex()) {
            throw new IllegalArgumentException("cannot partition a complex polygon");
        }
        return null;
    }
}
