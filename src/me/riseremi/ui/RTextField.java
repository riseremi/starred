package me.riseremi.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import javax.swing.JTextField;

/**
 *
 * @author Riseremi
 */
@SuppressWarnings("serial")
public class RTextField extends JTextField {
    private final Color COLOR_BORDER_OUTSIDE = new Color(185, 185, 185);
    private final Color COLOR_BACKGROUND = new Color(255, 255, 255);

    public RTextField(String text) {
        super(text);
        setBorder(javax.swing.BorderFactory.createEmptyBorder());
        setHorizontalAlignment(JTextField.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2);

        g2.setColor(COLOR_BORDER_OUTSIDE);
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2.dispose();
    }

    protected float getLabelHeight(Graphics2D g2, String label) {
        FontRenderContext frc = g2.getFontRenderContext();
        return g2.getFont().getLineMetrics(label, frc).getHeight();
    }
}
