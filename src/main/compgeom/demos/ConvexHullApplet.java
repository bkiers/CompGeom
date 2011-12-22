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

import compgeom.RPoint2D;
import compgeom.RPolygon2D;
import compgeom.RRectangle;
import compgeom.algorithms.GrahamScan;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A "Graham Scan" demo.</p>
 * <p>See: {@link compgeom.algorithms.GrahamScan}</p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
@SuppressWarnings("serial")
public class ConvexHullApplet extends JApplet {

    private static final String INSTRUCTION = "Move over the grid and click the mouse button to add points";

    private PointModel pointModel;
    private JList pointList;
    private JButton btnReset;
    private JButton btnRemove;

    private DrawingPanel drawingPanel;
    private JLabel lblCoordinate;

    @Override
    public void init() {
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    setupGUI();
                }
            });
        }
        catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    private void setupGUI() {
        super.setSize(700, 620);
        super.setLayout(new BorderLayout(5, 5));

        JPanel main = new JPanel(new BorderLayout());
        JPanel mainRightPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        // point list
        JPanel mainPointPanel = new JPanel();
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "points");
        mainPointPanel.setBorder(border);
        pointModel = new PointModel();
        pointList = new JList(pointModel);
        pointList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                repaint();
            }
        });
        pointList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Object o = pointList.getSelectedValue();
                if (o == null) return;
                repaint();
            }
        });
        pointList.setFont(new Font("Default", Font.PLAIN, 10));
        JPanel pointPanel = new JPanel(new BorderLayout(5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnReset = new JButton("reset");
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pointModel.clear();
                repaint();
            }
        });
        btnRemove = new JButton("remove");
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int[] selected = pointList.getSelectedIndices();
                if (selected.length == 0) return;
                for (int i = selected.length - 1; i >= 0; i--) {
                    pointModel.remove(selected[i]);
                }
                repaint();
            }
        });
        buttonPanel.add(btnReset);
        buttonPanel.add(btnRemove);
        pointPanel.add(buttonPanel, BorderLayout.SOUTH);
        JScrollPane scroll = new JScrollPane(pointList);
        scroll.setPreferredSize(new Dimension(160, 120));
        pointPanel.add(scroll, BorderLayout.CENTER);
        mainPointPanel.add(pointPanel);
        mainRightPanel.add(mainPointPanel);

        // drawing panel
        drawingPanel = new DrawingPanel();
        JPanel bottom = new JPanel(new BorderLayout());
        JPanel bottomLeft = new JPanel(new FlowLayout());
        lblCoordinate = new JLabel(INSTRUCTION);
        bottomLeft.add(lblCoordinate);
        bottom.add(bottomLeft, BorderLayout.WEST);
        main.add(drawingPanel, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        super.add(mainRightPanel, BorderLayout.EAST);
        super.add(main, BorderLayout.CENTER);
    }

    class DrawingPanel extends JPanel {

        final int dim = 8;

        DrawingPanel() {
            TitledBorder border = BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "grid");
            super.setBorder(border);
            super.setPreferredSize(new Dimension(800, 600));
            MouseAdapter mouseEvents = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pointModel.addElement(convert(e.getX(), e.getY()));
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    lblCoordinate.setText(INSTRUCTION);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    RPoint2D p = convert(e.getX(), e.getY());
                    lblCoordinate.setText(String.format("(%d,%d)  ",
                            p.x.intValue(), p.y.intValue()));
                }
            };
            super.addMouseMotionListener(mouseEvents);
            super.addMouseListener(mouseEvents);
        }

        private int[] convert(RPoint2D p) {
            return new int[]{
                    (super.getWidth() / 2) + p.x.intValue(),
                    (super.getHeight() / 2) - p.y.intValue()
            };
        }

        private RPoint2D convert(int screenX, int screenY) {
            int x = screenX - (super.getWidth() / 2);
            int y = (super.getHeight() / 2) - screenY;
            return new RPoint2D(x, y);
        }

        void drawLine(RPoint2D p1, RPoint2D p2, Graphics2D g2d) {
            int[] screenCoordinates1 = convert(p1);
            int[] screenCoordinates2 = convert(p2);
            g2d.drawLine(screenCoordinates1[0], screenCoordinates1[1],
                    screenCoordinates2[0], screenCoordinates2[1]);
        }

        void drawPoint(RPoint2D p, Graphics2D g2d) {
            int[] screenCoordinates = convert(p);
            g2d.fillOval(screenCoordinates[0] - (dim / 2),
                    screenCoordinates[1] - (dim / 2), dim, dim);
        }

        void drawPoints(Graphics2D g2d) {
            for (RPoint2D p : pointModel.getPoints()) {
                drawPoint(p, g2d);
            }
            int[] selectedPoints = pointList.getSelectedIndices();
            if (selectedPoints.length == 0) return;
            g2d.setColor(Color.MAGENTA);
            for (int i : selectedPoints) {
                highlightPoint((RPoint2D) pointModel.get(i), g2d);
            }
        }

        void drawConvexHull(RPolygon2D polygon, Graphics2D g2d) {
            List<RPoint2D> convexHull;
            try {
                convexHull = GrahamScan.getConvexHull(polygon);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return;
            }
            g2d.setColor(Color.BLUE);
            for (int i = 1; i < convexHull.size(); i++) {
                RPoint2D p1 = convexHull.get(i - 1);
                RPoint2D p2 = convexHull.get(i);
                drawLine(p1, p2, g2d);
                drawPoint(p1, g2d);
                drawPoint(p2, g2d);
            }
        }

        private void highlightPoint(RPoint2D p, Graphics2D g2d) {
            int[] screenCoordinates = convert(p);
            int diff = 3;
            g2d.drawOval(screenCoordinates[0] - ((dim / 2) + diff),
                    screenCoordinates[1] - ((dim / 2) + diff), dim + (2 * diff) - 1, dim + (2 * diff) - 1);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = super.getWidth();
            int h = super.getHeight();
            g2d.clearRect(0, 0, w, h);
            g2d.setColor(Color.GRAY);
            g2d.drawString("y", (w / 2) - 3, 25);
            g2d.drawLine(w / 2, 30, w / 2, h - 20);
            g2d.drawString("x", w - 20, (h / 2) + 3);
            g2d.drawLine(20, (h / 2) - 2, w - 25, (h / 2) - 2);

            if (pointModel.getSize() > 0) {
                drawPoints(g2d);
            }

            if (pointModel.getSize() > 2) {
                RPolygon2D polygon = new RPolygon2D(pointModel.getPoints());
                drawConvexHull(polygon, g2d);
            }
        }
    }

    class PointModel extends DefaultListModel {

        PointModel() {
            super();
        }

        List<RPoint2D> getPoints() {
            List<RPoint2D> points = new ArrayList<RPoint2D>();
            for (int i = 0; i < super.size(); i++) {
                points.add((RPoint2D) super.get(i));
            }
            return points;
        }
    }

    class RectangleWrapper {

        String name;
        RRectangle rectangle;

        RectangleWrapper(String name, RRectangle rectangle) {
            this.name = name;
            this.rectangle = rectangle;
        }

        @Override
        public String toString() {
            return String.format("%s, area: ~%.02f", name, rectangle.area());
        }
    }
}
