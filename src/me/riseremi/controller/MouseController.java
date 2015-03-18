package me.riseremi.controller;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Random;
import lombok.Getter;
import me.riseremi.cards.BasicCard;
import me.riseremi.cards.Deck;
import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Friend;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.map.world.CheckObstacles;
import me.riseremi.network.messages.MessageAttack;
import me.riseremi.network.messages.MessageSetPosition;
import org.rising.framework.network.Client;

/**
 *
 * @author Riseremi
 */
public class MouseController implements MouseListener, MouseMotionListener {

    @Getter
    private static final Rectangle mouseRect = new Rectangle(1, 1);
    private static Random rnd = new Random();

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("click");
        Core_v1 core = Core_v1.getInstance();

        final Player user = core.getPlayer();
        final Friend friend = core.getFriend();
        final Deck deck = user.getDeck();

        Entity target = friend;

        final int friendX = friend.getX();
        final int friendY = friend.getY();

        final int playerX = user.getX();
        final int playerY = user.getY();

        //in tiles
        final int cursorX = (int) core.getSelectionCursor().getX() / 32;
        final int cursorY = (int) core.getSelectionCursor().getY() / 32;

        final int mX = cursorX - core.getCamera().getX() / Global.tileWidth;
        final int mY = cursorY - core.getCamera().getY() / Global.tileHeight;

        System.out.println("cx: " + mX + "/cy: " + mY);

        boolean thereIsFriend = (mX == friendX) && (mY == friendY);
        boolean thereIsPlayer = (mX == playerX) && (mY == playerY);
        boolean thereIsObstacle = true;
        try {
            thereIsObstacle = CheckObstacles.checkObstacle(core.getWorld(), mX, mY);//mX == playerX) && (mY == playerY);
        } catch (CloneNotSupportedException ex) {
        }

        if (thereIsPlayer) {
            System.out.println("PLAYER DETECTED");
            target = user;

        }
        if (thereIsFriend) {
            System.out.println("FRIEND DETECTED");
            target = friend;
        }
        if (thereIsObstacle) {
            System.out.println("OBSTACLE DETECTED");
        }

        //check if there are entity
        int entitiesThere = 0;
        if (entitiesThere > 0) {
            thereIsFriend = thereIsPlayer = true;
        }

        //
        if (e.getButton() == MouseEvent.BUTTON3 && !core.isTileSelectionMode() /*&& core.isConnected()*/) {
            if (deck.getActiveCard() != null) {
                deck.removeCard(deck.getActiveCard());
            }
        }

        if (e.getButton() == MouseEvent.BUTTON3
                && core.isTileSelectionMode() && core.isNextTurnAvailable() /*&& core.isConnected()*/) {
            user.setCanMove(true);
            deck.setJustUsedCard(null);
            core.setTileSelectionMode(false);
        }

        final BasicCard justUsedCard = deck.getJustUsedCard();
        boolean near = justUsedCard != null ? core.isTheyNear(user, mX, mY, justUsedCard.getUseRadius()) : false;
        if (near && !thereIsObstacle) {
            try {
                final Client instance = Client.getInstance();
                final int justUsedCardId = justUsedCard.getId();
                final int userId = user.getId();
                final int targetId = target.getId();

                final BasicCard.Effect[] effects = justUsedCard.getEffects();

                for (BasicCard.Effect effect : effects) {
                    switch (effect) {
                        case BLINK:
                            if (!thereIsFriend && !thereIsPlayer) {
                                instance.send(new MessageSetPosition(userId, mX, mY));
                            }
                            break;
                        case AP:
                        case BLOOD:
                        case MDMG:
                        case PDMG:
                        case HEAL:
                            if (thereIsFriend || thereIsPlayer) {
                                instance.send(new MessageAttack(userId, targetId, justUsedCardId));
                            }
                            break;

                    }
                }
            } catch (IOException ex) {
            }
            Main.addToChat("You used a " + deck.getJustUsedCard().getName() + "\r\n");
            deck.getJustUsedCard().setEffects(new BasicCard.Effect[]{BasicCard.Effect.NONE});
            user.decreaseActionPoint(deck.getJustUsedCard().getCost());

            user.setCanMove(true);
            core.setCardJustUsed(true);
            core.setTileSelectionMode(false);
            user.getDeck().removeCard(deck.getJustUsedCard());
            user.getDeck().setJustUsedCard(null);
        }

        //activate selection mode after click on a thumbnail
        if (e.getButton() == MouseEvent.BUTTON1 && deck.getActiveCard() != null
                && !core.isTileSelectionMode() && core.isNextTurnAvailable() /*&& core.isConnected()*/) {
            deck.getActiveCard().useByTo(user, target);
        }
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
        mouseRect.x = e.getX();
        mouseRect.y = e.getY();

        final Core_v1 core = Core_v1.getInstance();
        final Deck deck = core.getPlayer().getDeck();

        if (core.isTileSelectionMode()) {
            core.setSelectionCursor(new Rectangle(e.getX() / 32 * 32, e.getY() / 32 * 32, 32, 32));
        }
        //rect intersections
        deck.switchPaint(mouseRect);
    }
}
