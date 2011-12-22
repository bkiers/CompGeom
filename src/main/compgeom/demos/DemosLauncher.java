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
package compgeom.demos;

import javax.swing.*;

/**
 * <p>
 * A class that launches a dialogue box that lets the user choose
 * a demo to run.
 * </p>
 *
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: May 3, 2010
 * </p>
 */
public class DemosLauncher {

    /**
     * Launches a demo.
     *
     * @param demo the Runnable containing the frame of the demo.
     */
    private static void launch(Runnable demo) {
        if(demo == null) return;
        try {
            SwingUtilities.invokeAndWait(demo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches a dialogue box that lets the user choose a demo to run.
     *
     * @param args command line parameters, which are ignored.
     */
    public static void main(String[] args) {
        Object[] possibilities = {
                "a - Closest pair of points",
                "b - Convex hull",
                "c - Rotating calipers",
                "d - Bentley-Ottmann algorithm"
        };
        String s = (String)JOptionPane.showInputDialog(
                            null,
                            "\nSelect a demo to launch:\n\n",
                            "Demo Launcher",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            possibilities[0]);
        if (s == null) return;
        Runnable demo = null;
        switch(s.charAt(0)) {
            case 'a':
                demo = new Runnable(){ public void run() {new ClosestPointPairFrame();}};
                break;
            case 'b':
                demo = new Runnable(){ public void run() {new ConvexHullFrame();}};
                break;
            case 'c':
                demo = new Runnable(){ public void run() {new RotatingCalipersFrame();}};
                break;
            case 'd':
                demo = new Runnable(){ public void run() {new BentleyOttmannFrame();}};
                break;
        }
        launch(demo);
    }
}
