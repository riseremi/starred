package me.riseremi.ui;

import me.riseremi.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Remi
 */
public class HPBar {

    public static final int INDENT = 16, BAR_HEIGHT = 8;
    private final int x, y;
    private final int barWidth, barHeight, indent;
    private final Color ACTIVE = new Color(6, 204, 113),
            SHADOW = new Color(6, 204, 113),
//            SHADOW = new Color(39, 174, 96),
            INACTIVE = new Color(231, 76, 60);

    public HPBar(int x, int y, int indent, int maxWidth, int barHeight) {
        this.x = x;
        this.y = y;
        this.indent = indent;
        this.barWidth = maxWidth - indent * 2;
        this.barHeight = barHeight;
    }

    public void paint(Graphics g, Entity target) {
        Graphics2D g2 = (Graphics2D) g;

        int maxHP = target.getMaxHp();
        int curHP = target.getHp();

        g2.translate(x, y);

        g2.setColor(INACTIVE);
        int hp = (barWidth);
        g2.fillRect(indent, indent / 2, hp, barHeight);

        g2.setColor(ACTIVE);
        hp = (barWidth * curHP / maxHP);
        g2.fillRect(indent, indent / 2, hp, barHeight - 1);

        g2.setColor(SHADOW);
        hp = (barWidth * curHP / maxHP);
        g2.fillRect(indent, indent / 2 + (barHeight - barHeight / 4), hp, barHeight / 4);

        g2.setColor(Color.WHITE);
        g2.drawString(target.getName() + " " + curHP + "/" + maxHP, indent, indent);

        g2.translate(-x, -y);
    }

}
