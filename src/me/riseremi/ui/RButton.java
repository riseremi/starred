package me.riseremi.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import javax.swing.JButton;

/**
 *
 * @author Riseremi
 */
@SuppressWarnings("serial")
public class RButton extends JButton {

    private final Color COLOR_BORDER_OUTSIDE = new Color(185, 185, 185);
    private final Color COLOR_BORDER_INSIDE = new Color(252, 252, 252);
    private Color COLOR_BACKGROUND = new Color(242, 242, 242);
    private final Color COLOR_BACKGROUND_HOVER = new Color(232, 242, 252);
    private final Color COLOR_BACKGROUND_NORMAL = new Color(242, 242, 242);
    private final Color COLOR_TEXT = Color.BLACK;
    private boolean paintButton;

    public RButton(String text) {
        this(text, true);
    }

    public RButton(String text, boolean paintButton) {
        super(text);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.paintButton = paintButton;

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                COLOR_BACKGROUND = COLOR_BACKGROUND_HOVER;
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                COLOR_BACKGROUND = COLOR_BACKGROUND_NORMAL;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        if (paintButton) {
            g2.setPaint(COLOR_BACKGROUND);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setPaint(COLOR_TEXT);

            final String text = getText();
            int w = g2.getFontMetrics().stringWidth(text) / 2;
            g2.drawString(text, getWidth() / 2 - w, getHeight() / 2 + getLabelHeight(g2, text) / 4);

            g2.setColor(COLOR_BORDER_OUTSIDE);
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            g2.setColor(COLOR_BORDER_INSIDE);
            g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        }
        
        if (getIcon() != null) {
            getIcon().paintIcon(this, g2, 0, 0);
        }

        g2.dispose();
    }

    protected float getLabelHeight(Graphics2D g2, String label) {
        FontRenderContext frc = g2.getFontRenderContext();
        return g2.getFont().getLineMetrics(label, frc).getHeight();
    }
}
