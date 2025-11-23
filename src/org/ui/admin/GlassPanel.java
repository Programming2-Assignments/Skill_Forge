package org.ui.admin;

import javax.swing.*;
import java.awt.*;

/**
 * Modern glass-like panel with gradient, glow, and smooth rounded corners.
 * Perfect for dark futuristic UI dashboards.
 */
public class GlassPanel extends JPanel {

    private int arc = 22;
    private float opacity = 0.18f; // transparency level (0 = invisible, 1 = solid)

    public GlassPanel() {
        setOpaque(false);
    }

    public GlassPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // ===== Gradient Glass Background =====
        Color top = new Color(255, 255, 255, (int) (opacity * 255));
        Color bottom = new Color(255, 255, 255, (int) (opacity * 160));

        GradientPaint gradient = new GradientPaint(
                0, 0, top,
                0, height, bottom
        );

        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        // ===== Soft Glow Border =====
        g2.setColor(new Color(255, 255, 255, 45));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(1, 1, width - 2, height - 2, arc, arc);

        // ===== Inner Shadow for Depth =====
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(2, 2, width - 4, height - 4, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
