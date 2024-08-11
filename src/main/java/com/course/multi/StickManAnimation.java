package com.course.multi;

import javax.swing.*;
import java.awt.*;

public class StickManAnimation extends JPanel implements Runnable {
    private int xPosition = 50;
    private int yPosition = 200;
    private final int frameWidth = 800;
    private final int frameHeight = 400;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stick Man Running");
        StickManAnimation animation = new StickManAnimation();
        frame.add(animation);
        frame.setSize(animation.frameWidth, animation.frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new Thread(animation).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawStickMan(g, xPosition, yPosition);
    }

    private void drawStickMan(Graphics g, int x, int y) {
        // Head
        g.drawOval(x - 10, y - 30, 20, 20);

        // Body
        g.drawLine(x, y - 10, x, y + 30);

        // Arms
        g.drawLine(x, y, x - 20, y - 10); // Left arm
        g.drawLine(x, y, x + 20, y - 10); // Right arm

        // Legs
        g.drawLine(x, y + 30, x - 20, y + 50); // Left leg
        g.drawLine(x, y + 30, x + 20, y + 50); // Right leg
    }

    @Override
    public void run() {
        while (true) {
            xPosition += 5;
            if (xPosition > frameWidth) {
                xPosition = -50; // Reset position when off-screen
            }
            repaint();

            try {
                Thread.sleep(100); // Delay for smooth animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
