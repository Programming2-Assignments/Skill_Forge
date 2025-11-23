package org.ui.admin;

import javax.swing.*;
import java.awt.*;


public class ModernWhiteButton extends JButton {

    private Color baseColor   = new Color(250, 250, 250);
    private Color hoverColor  = new Color(245, 245, 245);
    private Color pressColor  = new Color(230, 230, 230);

    private boolean hovered = false;
    private boolean pressed = false;

    public ModernWhiteButton(String text) {
        super(text);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.BLACK);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(getFont().deriveFont(Font.PLAIN, 15f));
        setHorizontalAlignment(CENTER);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true;  repaint(); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { hovered = false; repaint(); }
            @Override public void mousePressed (java.awt.event.MouseEvent e) { pressed = true; repaint(); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) { pressed = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        int arc = 22;
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Pick color based on state
        Color fill;
        if (pressed)
            fill = pressColor;
        else if (hovered)
            fill = hoverColor;
        else
            fill = baseColor;

        // Outer soft shadow
        g2.setColor(new Color(0, 0, 0, 75));
        g2.fillRoundRect(4, 5, getWidth() - 8, getHeight() - 8, arc, arc);

        // Button Body
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // ------- Inner subtle highlight -------
        g2.setColor(new Color(255, 255, 255, 120));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);

        super.paintComponent(g2);
        g2.dispose();
    }
}
