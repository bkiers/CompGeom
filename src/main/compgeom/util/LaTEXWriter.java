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

import compgeom.Rational;
import compgeom.RLineSegment2D;
import compgeom.RPoint2D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * A class used for debugging: it creates L<font size=-3><sup>A</sup></font>T<sub>E</sub>X
 * source files of geometric objects. All methods that generate <code>.tex</code> files, or print the
 * contents of such files, use the  <a href="http://sourceforge.net/projects/pgf"><code>{tikz}</code></a>
 * package.
 * </p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Apr 25, 2010
 * </p>
 */
public class LaTEXWriter {

    /**
     * No need to instantiate this class.
     */
    private LaTEXWriter() {
    }

    /**
     * <p>
     * Creates a L<font size=-3><sup>A</sup></font>T<sub>E</sub>X String of a set of segments.
     * The drawing will be displayed with the following characteristics:
     * </p>
     * <ul>
     * <li>landscape</li>
     * <li>visible grid</li>
     * <li>visible x- and y-axis</li>
     * <li>visible axis values</li>
     * <li>axis values step is 2: <code>0, 2, 4, ...</code> or <code>1, 3, 5, ...</code></li>
     * <li>line segment end with dots</li>
     * <li>the size of the dots is 2</li>
     * <li>the scale of the drawing is 1.0</li>
     * </ul>
     * <p>
     * In short, this method calls: <code>toLaTeXString(segments, true, true, true, true, 2, true, 2, 1.0)</code>
     * </p>
     * <p>
     * Note that the L<font size=-3><sup>A</sup></font>T<sub>E</sub>X file uses the
     * <a href="http://sourceforge.net/projects/pgf"><code>{tikz}</code></a> package.
     * </p>
     *
     * @param segments the line segments.
     * @return a L<font size=-3><sup>A</sup></font>T<sub>E</sub>X String of a set of segments.
     * @see #toLaTeXString(java.util.Set, boolean, boolean, boolean, boolean, int, boolean, int, double)
     */
    public static String toLaTeXString(Set<RLineSegment2D> segments) {
        return toLaTeXString(segments, true, true, true, true, 2, true, 2, 1.0);
    }

    /**
     * <p>
     * Creates a L<font size=-3><sup>A</sup></font>T<sub>E</sub>X String of a set of segments.
     * </p>
     * <p>
     * Note that the L<font size=-3><sup>A</sup></font>T<sub>E</sub>X file uses the
     * <a href="http://sourceforge.net/projects/pgf"><code>{tikz}</code></a> package
     * which will need to be already installed.
     * </p>
     *
     * @param segments      the line segments.
     * @param landscape     if <code>true</code>, the image will be landscape, else in portrait.
     * @param grid          if <code>true</code>, the grid will be displayed.
     * @param axis          if <code>true</code>, the x- and/or y-axis will be displayed.
     * @param axisValues    if <code>true</code>, the values of the x- and y-axis are displayed.
     * @param axisValueStep if <code>true</code>, the value by which the axisValues should be increased.
     *                      Must be a positive number > 0.
     * @param dots          if <code>true</code>, the endings of the segments are displayed as dots.
     * @param dotSize       the size of the dots of the segment endings.
     * @param scale         the scale of the image. It must be a positive value between 0.5 and 10.0.
     * @return a L<font size=-3><sup>A</sup></font>T<sub>E</sub>X String of a set of segments.
     * @throws IllegalArgumentException if scale is not between 0.5 and 10.0or if axisValueStep
     *                                  is less than 1.
     */
    public static String toLaTeXString(Set<RLineSegment2D> segments, boolean landscape,
                                       boolean grid, boolean axis, boolean axisValues,
                                       int axisValueStep, boolean dots, int dotSize, double scale)
            throws IllegalArgumentException {

        if (scale < 0.5 || scale > 10.0) {
            throw new IllegalArgumentException("scale must be between 0.5 and 10.0");
        }

        if (axisValueStep < 1) {
            throw new IllegalArgumentException("axisValueStep must be more than 0");
        }

        Rational minX = Rational.POSITIVE_INFINITY, minY = Rational.POSITIVE_INFINITY,
                maxX = Rational.NEGATIVE_INFINITY, maxY = Rational.NEGATIVE_INFINITY;

        Set<RPoint2D> points = new HashSet<RPoint2D>();

        for (RLineSegment2D s : segments) {
            points.add(s.p1);
            points.add(s.p2);
        }

        for (RPoint2D p : points) {
            if (p.x.isLessThan(minX)) minX = p.x;
            if (p.x.isMoreThan(maxX)) maxX = p.x;
            if (p.y.isLessThan(minY)) minY = p.y;
            if (p.y.isMoreThan(maxY)) maxY = p.y;
        }

        StringBuilder b = new StringBuilder();

        b.append("\\documentclass{article}").append('\n');
        b.append("\\usepackage{fullpage}").append('\n');
        b.append("\\special{").append(landscape ? "landscape" : "portrait").append("}").append('\n');
        b.append("\\usepackage{tikz}").append('\n');
        b.append("\\begin{document}").append("\n\n");
        b.append("\\begin{tikzpicture}[scale=").append(scale).append("]").append("\n\n");

        if (grid) {
            b.append(String.format("\\draw[step=1cm,help lines] (%s,%s) grid (%s,%s);\n\n",
                    minX, minY, maxX, maxY));
        }

        final Rational aBit = new Rational("1/10");
        final Rational half = new Rational("1/2");
        if (axis && minY.isLessThanEq(Rational.ZERO) && maxY.isMoreThanEq(Rational.ZERO)) {
            b.append(String.format("\\draw (%s,0) node{$x$};\n", maxX.add(half)));
            b.append(String.format("\\draw [<->,very thick] (%s,0) -- (%s,0);\n\n",
                    minX.subtract(aBit), maxX.add(aBit)));
        }
        if (axis && minX.isLessThanEq(Rational.ZERO) && maxX.isMoreThanEq(Rational.ZERO)) {
            b.append(String.format("\\draw (0,%s) node{$y$};\n", maxY.add(half)));
            b.append(String.format("\\draw [<->,very thick] (0,%s) -- (0,%s);\n\n",
                    minY.subtract(aBit), maxY.add(aBit)));
        }

        if (axisValues) {
            b.append(String.format("\\foreach \\x/\\xtext in {%s,%s,...,%s}\n    \\draw (\\x,%s) node{$\\xtext$};\n\n",
                    minX, minX.add(new Rational(axisValueStep)), maxX, minY.subtract(new Rational("0.4"))));
            b.append(String.format("\\foreach \\y/\\ytext in {%s,%s,...,%s}\n    \\draw (%s,\\y) node{$\\ytext$};\n\n",
                    minY, minY.add(new Rational(axisValueStep)), maxY, minX.subtract(new Rational("0.6"))));
        }

        b.append("\\draw ");
        for (RLineSegment2D s : segments) {
            b.append(s.p1).append(" -- ").append(s.p2).append(' ');
        }
        b.append(";\n\n");

        if (dots) {
            b.append("\\filldraw ");
            for (RPoint2D p : points) {
                b.append(p).append(" circle (").append(dotSize).append("pt) ");
            }
            b.append(";\n\n");
        }

        b.append("\\end{tikzpicture}").append("\n\n");
        b.append("\\end{document}").append("\n");

        return b.toString();
    }

    /**
     * <p>
     * Writes a L<font size=-3><sup>A</sup></font>T<sub>E</sub>X String of a set
     * of segments to a file. The drawing will be displayed with the following
     * characteristics:
     * </p>
     * <ul>
     * <li>landscape</li>
     * <li>visible grid</li>
     * <li>visible x- and y-axis</li>
     * <li>visible axis values</li>
     * <li>axis values step is 2: <code>0, 2, 4, ...</code> or <code>1, 3, 5, ...</code></li>
     * <li>line segment end with dots</li>
     * <li>the size of the dots is 2</li>
     * <li>the scale of the drawing is 1.0</li>
     * </ul>
     * <p>
     * In short, this method calls:
     * <code>writeLaTeXFile(segments, true, true, true, true, 2, true, 2, 1.0, fileName)</code>
     * </p>
     * <p>
     * Note that the L<font size=-3><sup>A</sup></font>T<sub>E</sub>X file uses the
     * <a href="http://sourceforge.net/projects/pgf"><code>{tikz}</code></a> package.
     * </p>
     *
     * @param segments the line segments.
     * @param fileName the name of the file to write to.
     * @throws java.io.IOException if the file cannot be written to disk.
     * @see #writeLaTeXFile(java.util.Set, boolean, boolean, boolean, boolean, int, boolean, int, double, String)
     */
    public static void writeLaTeXFile(Set<RLineSegment2D> segments, String fileName) throws IOException {
        writeLaTeXFile(segments, true, true, true, true, 2, true, 2, 1.0, fileName);
    }

    /**
     * <p>
     * Writes a L<font size=-3><sup>A</sup></font>T<sub>E</sub>X String of a set
     * of segments to a file.
     * </p>
     * <p>
     * Note that the L<font size=-3><sup>A</sup></font>T<sub>E</sub>X file uses the
     * <a href="http://sourceforge.net/projects/pgf"><code>{tikz}</code></a> package.
     * </p>
     *
     * @param segments      the line segments.
     * @param landscape     if <code>true</code>, the image will be landscape, else in portrait.
     * @param grid          if <code>true</code>, the grid will be displayed.
     * @param axis          if <code>true</code>, the x- and/or y-axis will be displayed.
     * @param axisValues    if <code>true</code>, the values of the x- and y-axis are displayed.
     * @param axisValueStep if <code>true</code>, the value by which the axisValues should be increased.
     *                      Must be a positive number > 0.
     * @param dots          if <code>true</code>, the endings of the segments are displayed as dots.
     * @param dotSize       the size of the dots of the segment endings.
     * @param scale         the scale of the image. It must be a positive value between 0.5 and 10.0.
     * @param fileName      the name of the file to write to.
     * @throws IllegalArgumentException if scale is not between 0.5 and 10.0 or if axisValueStep
     *                                  is less than 1.
     * @throws IOException              if the file cannot be written to disk.
     */
    public static void writeLaTeXFile(Set<RLineSegment2D> segments, boolean landscape,
                                      boolean grid, boolean axis, boolean axisValues,
                                      int axisValueStep, boolean dots, int dotSize,
                                      double scale, String fileName)
            throws IllegalArgumentException, IOException {

        String contents = toLaTeXString(segments, landscape, grid,
                axis, axisValues, axisValueStep, dots, dotSize, scale);
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(contents);
        out.close();
    }
}
