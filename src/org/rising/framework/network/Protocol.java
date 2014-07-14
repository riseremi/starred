package org.rising.framework.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import me.riseremi.cards.BasicCard;
import me.riseremi.cards.CardsArchive;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.network.messages.MessageAttack;
import me.riseremi.network.messages.MessageChat;
import me.riseremi.network.messages.MessageConnect;
import me.riseremi.network.messages.MessagePing;
import me.riseremi.network.messages.MessageSetFriendId;
import me.riseremi.network.messages.MessageSetName;
import me.riseremi.network.messages.MessageSetPlayerId;
import me.riseremi.network.messages.MessageSetPosition;

/**
 *
 * @author Riseremi
 */
public class Protocol {

    private static Random rnd = new Random();
    private static ArrayList<Entity> players = new ArrayList<>();

    public static void processMessageOnServerSide(final Message message, int id) throws IOException {
        Message.Type type = message.getType();

        switch (type) {
            case CONNECT:
                System.out.println("Connection id: " + id);
                Player p = new Player(((MessageConnect) message).getName(), 0, id, Entity.Type.PLAYER);

                players.add(p);

                if (players.size() == 2) {
                    for (Entity e : players) {
                        Server.getInstance().sendToOne(new MessageSetPlayerId(e.getId()), e.getId());
                        Server.getInstance().sendToAllExcludingOne(new MessageSetFriendId(e.getId()), e.getId());

                        Server.getInstance().sendToAll(new MessageSetName(e.getName(), e.getId()));
                        //Server.getInstance().sendToOne(new MessageSetName(e.getName(), e.getId()), e.getId());

                    }
                }
                break;
            case CHAT_MESSAGE:
                Server.getInstance().sendToAll(message);
                break;
            case SET_POSITION:
                Server.getInstance().sendToAll(message);
                break;
            case TURN_ENDED:
                Server.getInstance().sendToAllExcludingOne(message, id);
                break;
            case ATTACK_TEST:
                Server.getInstance().sendToAll(message);
                break;
            case PING_MESSAGE:
                Server.getInstance().sendToAllExcludingOne(message, id);
                break;
            default:
                Server.getInstance().sendToAll(message);
        }
    }

    public static void processMessageOnClientSide(Message message) {
        Message.Type type = message.getType();
        Core_v1 core = Core_v1.getInstance();
        int id;
        String name, text;

        switch (type) {
            case SET_PLAYER_ID:
                core.getPlayer().setId(((MessageSetPlayerId) message).getId());
                break;
            case SET_FRIEND_ID:
                core.getFriend().setId(((MessageSetFriendId) message).getId());
                break;
            case CHAT_MESSAGE:
                id = ((MessageChat) message).getId();
                text = ((MessageChat) message).getText();

                Main.addToChat(core.getPlayerById(id).getName() + ": " + text);
                break;
            case SET_NAME:
                id = ((MessageSetName) message).getId();
                name = ((MessageSetName) message).getName();

                core.getPlayerById(id).setName(name);
                break;
            case SET_POSITION:
                MessageSetPosition msgSP = ((MessageSetPosition) message);
                System.out.println(msgSP.getId());
                final Entity entity = core.getPlayerById(msgSP.getId());
                final int x = msgSP.getX();
                final int y = msgSP.getY();
                System.out.println("x: " + x + " y:" + y);
                entity.setPosition(x, y);
                break;
            case TURN_ENDED:
                core.startTurn();
                break;
            case ATTACK_TEST:
                MessageAttack msgA = ((MessageAttack) message);
                BasicCard card;
                try {
                    System.out.println(msgA.getCardId());
                    card = CardsArchive.get(msgA.getCardId());
                    card.applyEffectFromTo(core.getPlayerById(msgA.getUserId()), core.getPlayerById(msgA.getTargetId()));
                } catch (CloneNotSupportedException ex) {
                }
                break;
            case PING_MESSAGE:
                MessagePing msgP = ((MessagePing) message);

                System.out.println("Ping: " + (System.currentTimeMillis() - msgP.getStartTime()));
                break;
        }
    }

}
