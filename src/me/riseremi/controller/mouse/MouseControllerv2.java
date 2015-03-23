package me.riseremi.controller.mouse;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import lombok.Getter;
import me.riseremi.cards.Deck;
import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;

/**
 *
 * @author Remi Weiss <riseremi at icloud.com>
 */
public class MouseControllerv2 implements MouseListener, MouseMotionListener {

    @Getter
    private static final Rectangle mouseRect = new Rectangle(1, 1);

    @Override
    public void mouseClicked(MouseEvent e) {
        Core_v1 core = Core_v1.getInstance();
        
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        final int blockX = e.getX() / Global.tileWidth * Global.tileWidth;
        final int blockY = e.getY() / Global.tileHeight * Global.tileHeight;

        final int realX = e.getX() / Global.tileWidth * Global.tileWidth;
        final int realY = e.getY() / Global.tileHeight * Global.tileHeight;

        Core_v1.getInstance().getSelectionCursor().setPosition(blockX, blockY);

        //big card
        final Core_v1 core = Core_v1.getInstance();
        final Deck deck = core.getPlayer().getDeck();

        mouseRect.x = e.getX();
        mouseRect.y = e.getY();

        //rect intersections
        deck.switchPaint(mouseRect);

    }

}
