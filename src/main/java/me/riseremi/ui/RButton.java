package me.riseremi.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import javax.swing.JButton;
import me.riseremi.main.Main;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
@SuppressWarnings("serial")
public class RButton extends JButton {

    private final Color COLOR_BACKGROUND_DISABLED = new Color(189, 195, 199);
    private Color COLOR_BACKGROUND_HOVER = new Color(86, 183, 254);
    private Color COLOR_BACKGROUND_NORMAL = new Color(66, 163, 244);
    private Color COLOR_BACKGROUND = COLOR_BACKGROUND_NORMAL;
    private final Color COLOR_TEXT = Color.WHITE;
    private final boolean decorative;
    private int labelX, labelY;

    public RButton(String text, boolean decorative) {
        super(text);
        this.setBorderPainted(false);
        this.setFocusPainted(false);

        this.setFont(Main.MAIN_FONT);

        this.decorative = decorative;
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
        if (!decorative) {
            g2.setPaint(isEnabled() ? COLOR_BACKGROUND : COLOR_BACKGROUND_DISABLED);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setPaint(COLOR_TEXT);

            final String text = getText();

            if (labelX == 0) {
                labelX = getWidth() / 2 - g2.getFontMetrics().stringWidth(text) / 2;
            }

            if (labelY == 0) {
                labelY = (int) (getHeight() / 2 + getLabelHeight(g2, text) / 4);
            }

            g2.drawString(text, labelX, labelY);
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
