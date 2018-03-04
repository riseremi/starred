package me.riseremi.controller.mouse;

import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;
import me.riseremi.map.world.World;

import java.awt.*;

/**
 * Rectangle cursor that appears only when you use a card (tile/target
 * selection).
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class SelectionCursor {

    private final Rectangle rectangle;
    private int realX, realY;
    private final Color color = new Color(231, 76, 60);
    private static Core_v1 core;

    public SelectionCursor(World mainWorld, Core_v1 mainCore) {
        rectangle = new Rectangle();
        rectangle.width = Global.tileWidth;
        rectangle.height = Global.tileHeight;
        core = mainCore;
    }

    private void updateRealPosition() {
        realX = (rectangle.x - core.getCamera().getX()) / Global.tileWidth;
        realY = (rectangle.y - core.getCamera().getY()) / Global.tileHeight;
    }

    public int getX() {
        return rectangle.x;
    }

    public int getY() {
        return rectangle.y;
    }

    public void setPosition(int blockX, int blockY) {
        rectangle.x = blockX;
        rectangle.y = blockY;
    }

    public void paint(Graphics g) {
        updateRealPosition();
        g.setColor(color);
        g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public int getRealX() {
        return this.realX;
    }

    public int getRealY() {
        return this.realY;
    }

    public void setRealX(int realX) {
        this.realX = realX;
    }

    public void setRealY(int realY) {
        this.realY = realY;
    }
}
