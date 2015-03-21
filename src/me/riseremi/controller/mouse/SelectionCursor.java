package me.riseremi.controller.mouse;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import lombok.Getter;
import lombok.Setter;
import me.riseremi.core.Global;

/**
 * Rectangled cursor that appears only when you use a card (tile/target
 * selection).
 *
 * @author Remi Weiss <riseremi at icloud.com>
 */
public class SelectionCursor {

    private final Rectangle rectangle;
    @Getter @Setter private int realX, realY;

    public SelectionCursor() {
        rectangle = new Rectangle();
        rectangle.width = Global.tileWidth;
        rectangle.height = Global.tileHeight;
    }

    public void setPosition(int blockX, int blockY) {
        rectangle.x =
        rectangle.y = blockY;
    }

    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

}
