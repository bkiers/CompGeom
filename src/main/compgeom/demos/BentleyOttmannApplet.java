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

import compgeom.RLineSegment2D;
import compgeom.RPoint2D;
import compgeom.algorithms.BentleyOttmann;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * <p>
 * Author: Bart Kiers, bart@big-o.nl <br />
 * Date: May 16, 2010
 * </p>
 */
public class BentleyOttmannApplet extends JApplet {

    private static final String INSTRUCTION = "Move over the grid and click the mouse button to add line segments";

    private SegmentModel segmentModel;
    private PointModel pointModel;
    private JList segmentList;
    private JButton btnReset;
    private JButton btnRemove;
    private JButton btnFind;

    private DrawingPanel drawingPanel;
    private JLabel lblCoordinate;

    public void init() {
        setupGUI();
    }

    private void setupGUI() {
        super.setSize(700, 620);
        super.setLayout(new BorderLayout(5, 5));

        JPanel main = new JPanel(new BorderLayout());
        JPanel mainRightPanel = new JPanel(new GridLayout(3, 1, 3, 3));

        // point list
        JPanel mainPointPanel = new JPanel();
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "segments");
        mainPointPanel.setBorder(border);
        segmentModel = new SegmentModel();
        pointModel = new PointModel();
        segmentList = new JList(segmentModel);
        segmentList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                repaint();
            }
        });
        segmentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Object o = segmentList.getSelectedValue();
                if (o == null) return;
                repaint();
            }
        });
        segmentList.setFont(new Font("Default", Font.PLAIN, 10));
        JPanel pointPanel = new JPanel(new BorderLayout(5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnReset = new JButton("reset");
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                segmentModel.clear();
                pointModel.clear();
                repaint();
            }
        });
        btnRemove = new JButton("remove");
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int[] selected = segmentList.getSelectedIndices();
                if (selected.length == 0) return;
                for (int i = selected.length - 1; i >= 0; i--) {
                    segmentModel.remove(selected[i]);
                }
                repaint();
            }
        });
        buttonPanel.add(btnReset);
        buttonPanel.add(btnRemove);
        pointPanel.add(buttonPanel, BorderLayout.SOUTH);
        JScrollPane scroll = new JScrollPane(segmentList);
        scroll.setPreferredSize(new Dimension(160, 120));
        pointPanel.add(scroll, BorderLayout.CENTER);
        mainPointPanel.add(pointPanel);
        mainRightPanel.add(mainPointPanel);

        // drawing panel
        drawingPanel = new DrawingPanel();
        JPanel bottom = new JPanel(new BorderLayout());
        JPanel bottomLeft = new JPanel(new FlowLayout());
        btnFind = new JButton("find intersections");
        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Set<RLineSegment2D> segmentSet = new HashSet<RLineSegment2D>(segmentModel.getSegments());
                if(segmentSet.size() < 2) return;
                Set<RPoint2D> ips = BentleyOttmann.intersections(segmentSet);
                for(RPoint2D ip : ips) {
                    pointModel.addElement(ip);
                }
                repaint();
            }
        });
        bottomLeft.add(btnFind);
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
        private RPoint2D first = null;
        private RPoint2D mousePointer = null;

        DrawingPanel() {
            TitledBorder border = BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "grid");
            super.setBorder(border);
            super.setPreferredSize(new Dimension(800, 600));
            MouseAdapter mouseEvents = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    RPoint2D clicked = convert(e.getX(), e.getY());
                    if(first != null && !clicked.equals(first)) {
                        segmentModel.addElement(new RLineSegment2D(first, clicked));
                        first = null;
                    } else {
                        first = clicked;
                    }
                    pointModel.clear();
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    lblCoordinate.setText(INSTRUCTION);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    mousePointer = convert(e.getX(), e.getY());
                    lblCoordinate.setText(String.format("(%d,%d)  ",
                            mousePointer.x.intValue(), mousePointer.y.intValue()));
                    if(first != null) repaint();
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

        void drawPoint(RPoint2D point, Graphics2D g2d) {
            int[] screenCoordinates = convert(point);
            g2d.fillOval(screenCoordinates[0] - (dim / 2),
                    screenCoordinates[1] - (dim / 2), dim, dim);
        }

        void drawHollowPoint(RPoint2D point, Graphics2D g2d) {
            int[] screenCoordinates = convert(point);
            g2d.drawOval(screenCoordinates[0] - (dim / 2),
                    screenCoordinates[1] - (dim / 2), dim, dim);
        }


        void drawPoints(Graphics2D g2d) {
            g2d.setColor(Color.RED);
            for (RPoint2D p : pointModel.getSegments()) {
                drawHollowPoint(p, g2d);
            }
        }

        void drawSegment(RLineSegment2D segment, Graphics2D g2d) {
            drawLine(segment.p1, segment.p2, g2d);
            drawPoint(segment.p1, g2d);
            drawPoint(segment.p2, g2d);
        }

        void drawSegments(Graphics2D g2d) {
            for (RLineSegment2D s : segmentModel.getSegments()) {
                drawSegment(s, g2d);
            }
            int[] selectedSegments = segmentList.getSelectedIndices();
            if (selectedSegments.length == 0) return;
            g2d.setColor(Color.MAGENTA);
            for (int i : selectedSegments) {
                drawSegment((RLineSegment2D) segmentModel.get(i), g2d);
            }
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

            if(first != null) {
                this.drawPoint(first, g2d);
                this.drawLine(first, mousePointer, g2d);
            }

            if(!segmentModel.isEmpty()) {
                this.drawSegments(g2d);
            }

            if(!pointModel.isEmpty()) {
                drawPoints(g2d);
            }
        }
    }

    class PointModel extends DefaultListModel {

        PointModel() {
            super();
        }

        java.util.List<RPoint2D> getSegments() {
            java.util.List<RPoint2D> points = new ArrayList<RPoint2D>();
            for (int i = 0; i < super.size(); i++) {
                points.add((RPoint2D) super.get(i));
            }
            return points;
        }
    }

    class SegmentModel extends DefaultListModel {

        SegmentModel() {
            super();
        }

        java.util.List<RLineSegment2D> getSegments() {
            java.util.List<RLineSegment2D> segments = new ArrayList<RLineSegment2D>();
            for (int i = 0; i < super.size(); i++) {
                segments.add((RLineSegment2D) super.get(i));
            }
            return segments;
        }
    }
}
