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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import compgeom.*;
import compgeom.algorithms.ClosestPointPair;
import compgeom.util.Pair;

/**
 * <p>A "Closest Pair of Points" demo.</p>
 * <p>See: {@link compgeom.algorithms.ClosestPointPair}</p>
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: Mar 11, 2010
 * </p>
 */
@SuppressWarnings("serial")
public class ClosestPointPairFrame extends JFrame {

    private static final String INSTRUCTION = "Move over the grid and click the mouse button to add points";

    private PointModel pointModel;
    private JList pointList;
    private JButton btnReset;
    private JButton btnRemove;

    private DrawingPanel drawingPanel;
    private JLabel lblCoordinate;

    public ClosestPointPairFrame(RPoint2D... points) {
        this();
        for (int i = 0; i < points.length; i++) {
            pointModel.add(i, points[i]);
        }
        repaint();
    }

    public ClosestPointPairFrame() {
        super("Closest Pair of Points");
        setupGUI();
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    private void setupGUI() {
        super.setSize(700, 620);
        super.setLayout(new BorderLayout(5, 5));

        JPanel main = new JPanel(new BorderLayout());
        JPanel mainRightPanel = new JPanel(new GridLayout(3, 1, 3, 3));

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

        void drawConvexHull(List<RPoint2D> points, Graphics2D g2d) {
            g2d.setColor(Color.BLACK);
            for (int i = 0; i < points.size(); i++) {
                drawPoint(points.get(i), g2d);
            }
            Pair<RPoint2D> closest = ClosestPointPair.find(points);
            g2d.setColor(Color.RED);
            drawPoint(closest.m, g2d);
            drawPoint(closest.n, g2d);
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
                drawConvexHull(pointModel.getPoints(), g2d);
            }
        }
    }

    class PointModel extends DefaultListModel {

        PointModel() {
            super();
        }

        java.util.List<RPoint2D> getPoints() {
            java.util.List<RPoint2D> points = new ArrayList<RPoint2D>();
            for (int i = 0; i < super.size(); i++) {
                points.add((RPoint2D) super.get(i));
            }
            return points;
        }
    }

    public static void main(String[] args) {
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    new ClosestPointPairFrame();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
