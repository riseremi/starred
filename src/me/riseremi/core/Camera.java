package me.riseremi.core;

import java.awt.Graphics;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class Camera {

    private int x, y;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void translate(Graphics g) {
        g.translate(x, y);
    }

    public void untranslate(Graphics g) {
        g.translate(-x, -y);
    }

    public void addY(int y) {
        this.y += y;
    }

    public void addX(int x) {
        this.x += x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
