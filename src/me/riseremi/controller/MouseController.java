package me.riseremi.controller;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import me.riseremi.cards.BasicCard;
import me.riseremi.cards.Deck;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Friend;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.network.messages.MessageAttack;
import org.rising.framework.network.Client;

/**
 *
 * @author Riseremi
 */
public class MouseController implements MouseListener, MouseMotionListener {

    @Getter private static final Rectangle mouseRect = new Rectangle(1, 1);
    private static Random rnd = new Random();

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("click");
        Core_v1 core = Core_v1.getInstance();
        final Player user = core.getPlayer();
        final Friend friend = core.getFriend();
        final Deck deck = user.getDeck();

        final int friendX = friend.getX();
        final int friendY = friend.getY();

        final int playerX = user.getX();
        final int playerY = user.getY();

        final int x = (int) core.getSelectionCursor().getX() / 32;
        final int y = (int) core.getSelectionCursor().getY() / 32;

        final int mapX = core.getWorld().getX();
        final int mapY = core.getWorld().getY();

        final int mX = (x - mapX);
        final int mY = (y - mapY);

        boolean thereIsFriend = (mX == friendX) && (mY == friendY);
        boolean thereIsPlayer = (mX == playerX) && (mY == playerY);

        //check if there are entity
        int entitiesThere = 0;
        Entity target = user;

//        for (Entity ee : core.getEntities().getEntities()) {
//            if (mX == ee.getX() && mY == ee.getY()) {
//                entitiesThere++;
//                target = ee;
//            }
//        }
        if (entitiesThere > 0) {
            thereIsFriend = thereIsPlayer = true;
        }

        final int addX = (x - mapX) - user.getX();
        final int addY = (y - mapY) - user.getY();

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

        //activate selection mode after click on a thumbnail
        if (e.getButton() == MouseEvent.BUTTON1 && deck.getActiveCard() != null
                && !core.isTileSelectionMode() && core.isNextTurnAvailable() /*&& core.isConnected()*/) {
            deck.getActiveCard().useByTo(user, target);
        } else if (e.getButton() == MouseEvent.BUTTON1 && core.isTileSelectionMode()
                && core.isTheyNear(core.getPlayer(), mX, mY, deck.getJustUsedCard().getUseRadius())) {
            if (core.getWorld().getNullLayer().getTile(x - mapX, y - mapY) != 0) {

                String attackMessage = user.getId() + ":" + target.getId() + ":" + deck.getJustUsedCard().getId();

                System.out.println(attackMessage);
                switch (deck.getJustUsedCard().getEffect()) {
                    case BLINK:
                        if (!thereIsFriend && !thereIsPlayer) {
                            user.addToPosition(addX, addY);
                            core.getWorld().changePosition(addX, addY);
                            //network.sendData(new SetPositionMessage(user.getId(), mX, mY));
                        }
                        break;
                    case MAGICAL_DAMAGE:
                        if (thereIsFriend || thereIsPlayer) {
                            //network.sendData(new NetworkMessage(NetworkMessage.ATTACK_TEST, attackMessage));
                            target.dealMagicalDamage(deck.getJustUsedCard().getPower());
                        }
                        break;
                    case PHYSICAL_DAMAGE:
                        if (thereIsFriend || thereIsPlayer) {
                            //network.sendData(new NetworkMessage(NetworkMessage.ATTACK_TEST, attackMessage));
                            target.dealPhysicalDamage(deck.getJustUsedCard().getPower());
                        }
                        break;
                    case HEAL:
                        if (thereIsFriend || thereIsPlayer) {
                            //network.sendData(new NetworkMessage(NetworkMessage.ATTACK_TEST, attackMessage));
                            target.heal(deck.getJustUsedCard().getPower());
                        }
                        break;
                    case AP:
                        if (thereIsFriend || thereIsPlayer) {
                            //network.sendData(new NetworkMessage(NetworkMessage.ATTACK_TEST, attackMessage));
                            target.addAPInNextTurn(deck.getJustUsedCard().getPower());
                        }
                        break;
                }//blink must be here
                Main.addToChat("You used a " + deck.getJustUsedCard().getName() + "\r\n");
                deck.getJustUsedCard().setEffect(BasicCard.Effect.NONE);
                user.decreaseActionPoint(deck.getJustUsedCard().getCost());

                user.decreaseBloodCostHP(deck.getJustUsedCard().getBloodCost());
                //network.sendData(new NetworkMessage(NetworkMessage.PURE_ATTACK_TEST, user.getClass().getSimpleName() + ":" + deck.getJustUsedCard().getBloodCost()));
                //network.sendData(new PureDamageMessage(user.getId(), deck.getJustUsedCard().getBloodCost()));

                try {
                    MessageAttack message;
                    message = new MessageAttack(user.getId(), target.getId(), deck.getJustUsedCard().getId());
                    Client.getInstance().send(message);
                } catch (IOException ex) {
                }

                user.setCanMove(true);
                core.setCardJustUsed(true);
                core.setTileSelectionMode(false);
                user.getDeck().removeCard(deck.getJustUsedCard());
                user.getDeck().setJustUsedCard(null);
            }
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
