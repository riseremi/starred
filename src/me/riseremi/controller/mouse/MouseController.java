package me.riseremi.controller.mouse;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import lombok.Getter;
import me.riseremi.cards.BasicCard;
import me.riseremi.cards.Hand;
import me.riseremi.cards.Effect;
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
import org.rising.framework.network.Message;

/**
 *
 * @author Riseremi
 */
public class MouseController implements MouseListener, MouseMotionListener {

    @Getter
    private static final Rectangle mouseRect = new Rectangle(1, 1);
    private static final Random rnd = new Random();

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("click");
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

        //System.out.println("cx: " + mX + "/cy: " + mY);
        boolean thereIsFriend = (realX == friendX) && (realY == friendY);
        boolean thereIsPlayer = (realX == playerX) && (realY == playerY);
        boolean thereIsObstacle = true;
        try {
            thereIsObstacle = CheckObstacles.checkObstacle(core.getWorld(), realX, realY);//mX == playerX) && (mY == playerY);
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
//        int entitiesThere = 0;
//        if (entitiesThere > 0) {
//            thereIsFriend = thereIsPlayer = true;
//        }
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
        boolean near = justUsedCard != null
                ? core.rangeMatches(user, realX, realY, justUsedCard)
                : false;

        if (near && !thereIsObstacle) {

            final Client instance = Client.getInstance();
            final int justUsedCardId = justUsedCard.getId();
            final int userId = user.getId();
            final int targetId = target.getId();

            final Effect[] effects = justUsedCard.getEffects();
            ArrayList<Message> messagesToSend = new ArrayList<>();
            boolean attackMessageSent = false;

            for (Effect effect : effects) {
                switch (effect.getEffectType()) {
                    case BLINK:
                        if (!thereIsFriend && !thereIsPlayer) {
                            //instance.send(new MessageSetPosition(userId, mX, mY));
                            messagesToSend.add(new MessageSetPosition(userId, realX, realY));
                        }
                        break;
                    case BLINK_OPPONENT:
                        if (!thereIsFriend && !thereIsPlayer) {
                            //instance.send(new MessageSetPosition(userId, mX, mY));
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
                            //instance.send(new MessageAttack(userId, targetId, justUsedCardId));
                            messagesToSend.add(new MessageAttack(userId, targetId, justUsedCardId));
                            attackMessageSent = true;
                        }
                        break;
                    case UNDRAW_CARD:
                        if (!attackMessageSent) {
                            messagesToSend.add(new MessageAttack(userId, friend.getId(), justUsedCardId));
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
            Main.addToChat("You used a " + deck.getJustUsedCard().getName() + "\r\n");
            deck.getJustUsedCard().setEffects(new Effect[]{new Effect()});
            user.decreaseActionPoint(deck.getJustUsedCard().getCost());

            user.setCanMove(true);
            core.setCardJustUsed(true);
            core.setTileSelectionMode(false);
            user.getHand().removeCard(deck.getJustUsedCard());
            user.getHand().setJustUsedCard(null);
        }

        //activate selection mode after click on a thumbnail
        if (e.getButton() == MouseEvent.BUTTON1 && deck.getActiveCard() != null
                && !core.isTileSelectionMode() && core.isNextTurnAvailable() /*&& core.isConnected()*/) {
            deck.getActiveCard().setAsSelectedCard(user, target);
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
        final Hand deck = core.getPlayer().getHand();

        if (core.isTileSelectionMode()) {
            //core.setSelectionCursor(new Rectangle(e.getX() / 32 * 32, e.getY() / 32 * 32, 32, 32));
            core.getSelectionCursor().setPosition(e.getX() / 32 * 32, e.getY() / 32 * 32);
        }
        //rect intersections
        deck.switchPaint(mouseRect);
    }
}
