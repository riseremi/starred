package me.riseremi.map.layer;

import java.awt.Graphics;

/**
 *
 * @author LPzhelud
 */
public abstract class Layer {

    private int width, height, blocksX, blocksY, x, y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Layer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public final void paintComponent(Graphics g) {
        paintLayer(g);
    }

    abstract void paintLayer(Graphics g);

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }

    public int getBlocksX() {
        return blocksX;
    }

    public int getBlocksY() {
        return blocksY;
    }

    public void setBlocksX(int x) {
        this.blocksX = x;
    }

    public void setBlocksY(int y) {
        this.blocksY = y;
    }
}
