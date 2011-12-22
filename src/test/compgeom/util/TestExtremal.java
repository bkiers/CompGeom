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
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 28, 2010
 * </p>
 */
public class TestExtremal {

    @Test
    public void testMoreExtremeThan() {
        List<RPoint2D> points = CGUtil.createRPoint2DList("2 1 1 2 -1 2 -2 1 -2 -1 -1 -2 1 -2 2 -1");

        // the same points
        assertFalse(Extremal.LEFT_LOWER.moreExtremeThan(points.get(0), points.get(0)));

        assertTrue(Extremal.LEFT_LOWER.moreExtremeThan(points.get(4), points.get(3)));
        assertFalse(Extremal.LEFT_LOWER.moreExtremeThan(points.get(3), points.get(4)));

        assertTrue(Extremal.LEFT_UPPER.moreExtremeThan(points.get(3), points.get(4)));
        assertFalse(Extremal.LEFT_UPPER.moreExtremeThan(points.get(4), points.get(3)));

        assertTrue(Extremal.RIGHT_LOWER.moreExtremeThan(points.get(7), points.get(0)));
        assertFalse(Extremal.RIGHT_LOWER.moreExtremeThan(points.get(0), points.get(7)));

        assertTrue(Extremal.RIGHT_UPPER.moreExtremeThan(points.get(0), points.get(7)));
        assertFalse(Extremal.RIGHT_UPPER.moreExtremeThan(points.get(7), points.get(0)));

        assertTrue(Extremal.LOWER_LEFT.moreExtremeThan(points.get(5), points.get(6)));
        assertFalse(Extremal.LOWER_LEFT.moreExtremeThan(points.get(6), points.get(5)));

        assertTrue(Extremal.LOWER_RIGHT.moreExtremeThan(points.get(6), points.get(5)));
        assertFalse(Extremal.LOWER_RIGHT.moreExtremeThan(points.get(5), points.get(6)));

        assertTrue(Extremal.UPPER_LEFT.moreExtremeThan(points.get(2), points.get(1)));
        assertFalse(Extremal.UPPER_LEFT.moreExtremeThan(points.get(1), points.get(2)));

        assertTrue(Extremal.UPPER_RIGHT.moreExtremeThan(points.get(1), points.get(2)));
        assertFalse(Extremal.UPPER_RIGHT.moreExtremeThan(points.get(2), points.get(1)));
    }
}
