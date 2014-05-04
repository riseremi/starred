package me.riseremi.ui.windows;

import me.riseremi.core.Global;
import me.riseremi.main.Main;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author remi
 */
public class Window {

    private int titleX, titleY, x = Global.tileWidth, y = Global.tileHeight, width, height;
    private String title = "Window";
    private Color background = Color.DARK_GRAY;
    private Color font = Color.WHITE;
    private boolean isVisible = false;

    public Window(String title, int width, int height, int titleW) {
        this.title = title;
        this.width = width;
        this.height = height;
        //titleX = x + (width >> 1) - (g.getFontMetrics().stringWidth(title) >> 1);
        this.titleX = x + (width >> 1) - (titleW >> 1);
        titleY = y + (Global.tileHeight >> 1);
    }

    public void paint(Graphics g) {
        if (isVisible) {
            g.setColor(background);
            g.fillRect(x, y, width, height);
            g.setColor(font);
            g.drawRect(x, y, width, height);
            g.drawString(title, titleX, titleY);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void toggleVisible() {
        isVisible = !isVisible;
    }
}
