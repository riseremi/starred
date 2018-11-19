package me.riseremi.controller.mouse;

import me.riseremi.cards.Card;
import me.riseremi.cards.Effect;
import me.riseremi.cards.Hand;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Friend;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.map.world.CheckObstacles;
import me.riseremi.network.messages.MessageAttack;
import me.riseremi.network.messages.MessageSetPosition;
import org.rising.framework.network.Client;
import org.rising.framework.network.Message;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author riseremi <riseremi at icloud.com>
 */
public class MouseController implements MouseListener, MouseMotionListener {

    private static final Rectangle mouseRect = new Rectangle(1, 1);

    @Override
    public void mouseClicked(MouseEvent e) {
        Core_v1 core = Core_v1.getInstance();

        final Player user = core.getPlayer();
        final Friend friend = core.getFriend();
        final Hand deck = user.getHand();

        Entity target = friend;

        final int friendX = friend.getX();
        final int friendY = friend.getY();

        final int playerX = user.getX();
        final int playerY = user.getY();

        final int realX = core.getSelectionCursor().getRealX();
        final int realY = core.getSelectionCursor().getRealY();

        boolean thereIsFriend = (realX == friendX) && (realY == friendY);
        boolean thereIsPlayer = (realX == playerX) && (realY == playerY);
        boolean thereIsObstacle = true;
        try {
            thereIsObstacle = CheckObstacles.checkObstacle(core.getWorld(), realX, realY);
        } catch (CloneNotSupportedException ignored) {
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

        // Right click, remove card from the hand.
        if (e.getButton() == MouseEvent.BUTTON3 && !core.isTileSelectionMode()) {
            if (deck.getActiveCard() != null) {
                deck.removeCard(deck.getActiveCard());
            }
        }

        // Right click with raised card, cancel range selection.
        if (e.getButton() == MouseEvent.BUTTON3 && core.isTileSelectionMode() && core.isNextTurnAvailable()) {
            user.setCanMove(true);
            user.resetRaisedCard();
            core.setTileSelectionMode(false);
        }

        final Card raisedCard = user.getRaisedCard();
        boolean near = raisedCard != null && core.rangeMatches(user, realX, realY, raisedCard);
        System.out.println("Range valid: " + near);

        if (near && !thereIsObstacle) {
            final Client instance = Core_v1.getClient();
            final int raisedCardId = raisedCard.getId();
            final int userId = user.getId();
            final int targetId = target.getId();

            final List<Effect> effects = raisedCard.getEffects();
            ArrayList<Message> messagesToSend = new ArrayList<>();
            boolean attackMessageSent = false;


            for (Effect effect : effects) {
                System.out.println("Applying card effect: " + effect);
                switch (effect.getEffectType()) {
                    case BLINK:
                        if (!thereIsFriend && !thereIsPlayer) {
                            messagesToSend.add(new MessageSetPosition(userId, realX, realY));
                        }
                        break;
                    case BLINK_OPPONENT:
                        if (!thereIsFriend && !thereIsPlayer) {
                            messagesToSend.add(new MessageSetPosition(friend.getId(), realX, realY));
                        }
                        break;
                    case ADD_AP:
                    case DAMAGE:
                    case NONE:
                    case HEAL:
                    case BLOODCOST:
                    case DRAW_CARD:
                        if ((thereIsFriend || thereIsPlayer) && !attackMessageSent) {
                            messagesToSend.add(new MessageAttack(userId, targetId, raisedCardId));
                            attackMessageSent = true;
                        }
                        break;
                    case UNDRAW_CARD:
                        if (!attackMessageSent) {
                            messagesToSend.add(new MessageAttack(userId, friend.getId(), raisedCardId));
                            attackMessageSent = true;
                        }
                        break;
                }
            }
            try {
                for (Message message : messagesToSend) {
                    instance.send(message);
                }
            } catch (IOException ex) {
                System.out.println("Cannot send network message.");
            }
            Main.addToChat("You used a " + raisedCard.getName() + "\r\n");
            user.subtractActionPoints(raisedCard.getApcost());

            user.setCanMove(true);
            core.setCardJustUsed(true);
            core.setTileSelectionMode(false);
            user.getHand().removeCard(raisedCard.toDrawableCard());
            user.resetRaisedCard();
        }

        //activate selection mode after click on a thumbnail
        if (e.getButton() == MouseEvent.BUTTON1 && deck.getActiveCard() != null
                && !core.isTileSelectionMode() && core.isNextTurnAvailable()) {
            user.setRaisedCard(deck.getActiveCard().toCard());
        }

        if (mouseRect.x < 64 && mouseRect.y < 64) try {
            // assume we're clicking on the "Next turn" button
            Core_v1.getInstance().endTurn();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    public static Rectangle getMouseRect() {
        return mouseRect;
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
        final Hand deck = core.getPlayer().getHand();

        if (core.isTileSelectionMode()) {
            core.getSelectionCursor().setPosition(e.getX() / 32 * 32, e.getY() / 32 * 32);
        }
        //rect intersections
        deck.switchPaint(mouseRect);
    }
}
