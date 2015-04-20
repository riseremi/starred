package me.riseremi.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import javax.swing.JTextField;
import me.riseremi.main.Main;

/**
 *
 * @author Riseremi
 */
@SuppressWarnings("serial")
public class RTextField extends JTextField {

    private final Color COLOR_BORDER_OUTSIDE = new Color(185, 185, 185);
    private final Color COLOR_BACKGROUND = new Color(245, 247, 246);
    private final Color COLOR_BACKGROUND_DISABLED = new Color(225, 227, 226);

    public RTextField(String text) {
        super(text);
        setBorder(javax.swing.BorderFactory.createEmptyBorder());
        setHorizontalAlignment(JTextField.CENTER);
        setFont(Main.MAIN_FONT);
        setBackground(COLOR_BACKGROUND);
        setForeground(new Color(33, 33, 33));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        setBackground(isEditable() ? COLOR_BACKGROUND : COLOR_BACKGROUND_DISABLED);
        super.paintComponent(g2);
        
        g2.dispose();
    }

    protected float getLabelHeight(Graphics2D g2, String label) {
        FontRenderContext frc = g2.getFontRenderContext();
        return g2.getFont().getLineMetrics(label, frc).getHeight();
    }
}
