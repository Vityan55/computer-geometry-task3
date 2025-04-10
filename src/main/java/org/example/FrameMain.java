package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame for performing geometric transformations
 * such as rotation, deformation, reflection, and translation.
 */
public class FrameMain extends JFrame {
    Methods methods;

    // Frame dimensions
    private static final int width = 1480;
    private static final int height = 820;

    // UI Components
    private JPanel MainPanel;
    private JPanel DrawPanel;
    private JPanel ButtonPanel;
    private JButton startButton;
    private JButton reset;
    private JRadioButton Deformation;
    private JTextField deformationCountX;
    private JTextField deformationCountY;
    private JRadioButton Rotate;
    private JTextField rotateCount;
    private JRadioButton Reflection;
    private JTextField reflectionCountX;
    private JTextField reflectionCountY;
    private JRadioButton Translation;
    private JTextField translationCountX;
    private JTextField translationCountY;
    private JTextField text1;
    private JTextField text2;
    private JTextField text3;
    private JTextField text4;
    private JTextField text5;
    private JTextField text6;
    private JTextField text7;
    private JTextField text8;
    private JTextField text9;
    private JTextField text10;
    private JTextField text11;
    private JTextField text12;
    private JTextField text13;
    private JTextField text14;

    // Coordinates for drawing lines and triangles
    Point[] lines = {
            new Point(600, 200),
            new Point(1000, 350),
            new Point(600, 300),
            new Point(1000, 450)
    };
    Point[] triangle1 = {
            new Point(200, 300),
            new Point(200, 380),
            new Point(280, 380)
    };
    Point[] triangle2 = {
            new Point(400, 380),
            new Point(400, 500),
            new Point(500, 500)
    };

    /**
     * Constructor initializes the frame and components, sets default shapes,
     * and binds event listeners to the buttons.
     */
    public FrameMain() {
        // Set initial coordinates
        lines[0].x = 600; lines[0].y = 200;
        lines[1].x = 1000; lines[1].y = 350;
        lines[2].x = 600; lines[2].y = 300;
        lines[3].x = 1000; lines[3].y = 450;

        triangle1[0].x = 300; triangle1[0].y = 300;
        triangle1[1].x = 300; triangle1[1].y = 380;
        triangle1[2].x = 380; triangle1[2].y = 380;

        triangle2[0].x = 400; triangle2[0].y = 380;
        triangle2[1].x = 400; triangle2[1].y = 500;
        triangle2[2].x = 500; triangle2[2].y = 500;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null); // Center window
        setFocusable(true);
        setContentPane(MainPanel); // Set main panel
        setVisible(true);

        // Initialize transformation utility class
        methods = new Methods(0, getWidth(), 0, getHeight());

        // Reset button restores original shape positions
        reset.addActionListener(e -> {
            // Reset coordinates
            lines[0].x = 600; lines[0].y = 200;
            lines[1].x = 1000; lines[1].y = 350;
            lines[2].x = 600; lines[2].y = 300;
            lines[3].x = 1000; lines[3].y = 450;

            triangle1[0].x = 300; triangle1[0].y = 300;
            triangle1[1].x = 300; triangle1[1].y = 380;
            triangle1[2].x = 380; triangle1[2].y = 380;

            triangle2[0].x = 400; triangle2[0].y = 380;
            triangle2[1].x = 400; triangle2[1].y = 500;
            triangle2[2].x = 500; triangle2[2].y = 500;

            repaint(); // Redraw shapes
        });

        // Start button applies selected transformation
        startButton.addActionListener(e -> {
            if (Rotate.isSelected()) {
                // Rotation transformation
                double fi = Double.parseDouble(rotateCount.getText());
                fi *= Math.PI / 180; // Convert to radians
                methods.matrixConstruct(0, fi);
                methods.transformCoordinates(lines, 0, 4);
                methods.transformCoordinates(triangle1, 0, 3);
                methods.transformCoordinates(triangle2, 0, 3);
            }

            if (Deformation.isSelected()) {
                // Deformation transformation
                double x = Double.parseDouble(deformationCountX.getText());
                double y = Double.parseDouble(deformationCountY.getText());
                methods.matrixConstruct(1, x, y);
                methods.transformCoordinates(lines, 1, 4);
                methods.transformCoordinates(triangle1, 1, 3);
                methods.transformCoordinates(triangle2, 1, 3);
            }

            if (Reflection.isSelected()) {
                // Reflection transformation
                double x = Double.parseDouble(reflectionCountX.getText());
                double y = Double.parseDouble(reflectionCountY.getText());
                methods.matrixConstruct(2, y, x); // Swap arguments for reflection
                methods.transformCoordinates(lines, 2, 4);
                methods.transformCoordinates(triangle1, 2, 3);
                methods.transformCoordinates(triangle2, 2, 3);
            }

            if (Translation.isSelected()) {
                // Translation transformation
                double x = Double.parseDouble(translationCountX.getText());
                double y = Double.parseDouble(translationCountY.getText());
                methods.matrixConstruct(3, x, y);
                methods.transformCoordinates(lines, 3, 4);
                methods.transformCoordinates(triangle1, 3, 3);
                methods.transformCoordinates(triangle2, 3, 3);
            }

            repaint(); // Redraw updated shapes
        });
    }

    /**
     * Inner class for drawing shapes and axes.
     */
    private class DrawPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = getGraphics2D((Graphics2D) g);
            g2d.fillPolygon(
                    new int[]{triangle1[0].x, triangle1[1].x, triangle1[2].x},
                    new int[]{triangle1[0].y, triangle1[1].y, triangle1[2].y}, 3);
            g2d.fillPolygon(
                    new int[]{triangle2[0].x, triangle2[1].x, triangle2[2].x},
                    new int[]{triangle2[0].y, triangle2[1].y, triangle2[2].y}, 3);
        }

        private Graphics2D getGraphics2D(Graphics2D g) {
            Graphics2D g2d = g;

            // Draw coordinate axes
            int y0 = height / 2;
            g2d.drawLine(-1000, y0, 10000, y0);
            int x0 = width / 2;
            g2d.drawLine(x0, -1000, x0, 1000);

            // Draw geometric shapes
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));

            g2d.drawLine(lines[0].x, lines[0].y, lines[1].x, lines[1].y);
            g2d.drawLine(lines[2].x, lines[2].y, lines[3].x, lines[3].y);
            return g2d;
        }
    }

    /**
     * Instantiates custom drawing panel.
     */
    private void createUIComponents() {
        DrawPanel = new DrawPanel();
    }
}
