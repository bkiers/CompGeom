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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 28, 2010
 * </p>
 */
public class TestPair {

    @Test
    public void testPair() {
        new Pair<String>("abc", "abc");
        new Pair<String>("abc", "def");
        new Pair<String>("def", "abc");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPairIllegalArgumentException_1() {
        new Pair<String>("abc", null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPairIllegalArgumentException_2() {
        new Pair<String>(null, "abc");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPairIllegalArgumentException_3() {
        new Pair<String>(null, null);
    }

    @Test
    public void testEquals() {
        Pair<String> a = new Pair<String>("abc", "abc");
        Pair<String> b = new Pair<String>("abc", "def");
        Pair<String> c = new Pair<String>("def", "abc");

        assertTrue(a.equals(a));
        assertTrue(b.equals(c));
        assertFalse(a.equals(b));
        assertFalse(a.equals(c));
    }

    @Test
    public void testHashCode() {
        Pair<String> a = new Pair<String>("abc", "abc");
        Pair<String> b = new Pair<String>("abc", "def");
        Pair<String> c = new Pair<String>("def", "abc");

        assertTrue(a.hashCode() == a.hashCode());
        assertTrue(b.hashCode() == c.hashCode());
        assertFalse(a.hashCode() == c.hashCode());
        assertFalse(a.hashCode() == b.hashCode());
    }
}
