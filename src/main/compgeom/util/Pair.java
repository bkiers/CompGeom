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

/**
 * <p>
 * A class that represents a pair of arbitrary objects of the same type.
 * Note that the order
 * of the objects {@link #m} and {@link #n} is irrelevant:
 * <code>new Pair<String>("a", "b")</code> equals
 * <code>new Pair<String>("b", "a")</code>.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 28, 2010
 * </p>
 */
public class Pair<T> {

    /**
     * one of the items of <code>this</code> pair.
     */
    public final T m;

    /**
     * the other item of <code>this</code> pair.
     */
    public final T n;

    /**
     * a pre-calculated hash code.
     */
    private final int hash;

    /**
     * Creates a new pair of objects of the same type.
     *
     * @param m one of the item of <code>this</code> pair.
     * @param n the other item of <code>this</code> pair.
     * @throws IllegalArgumentException if either <code>m</code>
     *                                  or <code>n</code> is
     *                                  <code>null</code>.
     */
    public Pair(T m, T n) throws IllegalArgumentException {
        if(m == null || n == null) {
            throw new IllegalArgumentException("'m' and 'n' cannot be null.");
        }
        this.m = m;
        this.n = n;
        this.hash = 17 * ((m.hashCode() + n.hashCode()) * (m.hashCode() + n.hashCode()));
    }

    /**
     * <p>
     * Returns a pre-calculated hash code:
     *  <code>17 * ((m.hashCode() + n.hashCode()) * (m.hashCode() + n.hashCode()))</code>.
     * Note that <code>new Pair<String>("a", "b")</code> returns the same hash code as
     * <code>new Pair<String>("b", "a")</code>. In other words: the order of the objects
     * {@link #m} and {@link #n} is irrelevant.
     * </p>
     *
     * @return a pre-calculated hash code:
     *         <code>17 * ((m.hashCode() + n.hashCode()) * (m.hashCode() + n.hashCode()))</code>.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * <p>
     * Returns <code>true</code> if <code>this</code> and <code>that</code>
     * are equal. They are considered equals iff<br />
     * - <code>this.m</code> equals <code>((Pair)o).m</code> AND
     * <code>this.n</code> equals <code>((Pair)o).n</code> <br />
     * OR <br />
     * - <code>this.m</code> equals <code>((Pair)o).n</code> AND
     * <code>this.n</code> equals <code>((Pair)o).m</code><br />
     * <br />
     * In other words: the order of the objects {@link #m} and {@link #n} is
     * irrelevant.
     * </p>
     *
     * @param o the Object to which <code>this</code> is going to be compared.
     * @return <code>true</code> if <code>this</this>' {@link #m} and {@link #n}
     *         equals <code>(Pair)o</code>'s {@link #m} and {@link #n}, regardless
     *         of their order.
     */
    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Pair<?> that = (Pair<?>)o;
        return (this.m.equals(that.m) && this.n.equals(that.n)) ||
                (this.m.equals(that.n) && this.n.equals(that.m));
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        return String.format("Pair<%s>[%s, %s]", m.getClass().getName(), m, n);
    }
}
