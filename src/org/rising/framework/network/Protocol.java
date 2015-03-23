package org.rising.framework.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
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

    private static final Random rnd = new Random();
    private static final ArrayList<Entity> players = new ArrayList<>();

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
            case TURN_END:
                Server.getInstance().sendToAllExcludingOne(message, id);
                break;
            case ATTACK:
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
                message.processClient(message);
                break;
            case TURN_END:
                core.startTurn();
                break;
            case ATTACK:
                message.processClient(message);
                break;
            case GAMEOVER_MESSAGE:
                message.processClient(message);
                break;
            case PING_MESSAGE:
                MessagePing msgP = ((MessagePing) message);

                if (!msgP.isSingleSide()) {
                    try {
                        Client.getInstance().send(new MessagePing(System.currentTimeMillis(), true));
                    } catch (IOException ex) {
                    }
                    System.out.println("Ping handshake recieved in "
                            + (System.currentTimeMillis() - msgP.getStartTime())
                            + "ms");
                    Main.addToChat("Ping handshake recieved in "
                            + (System.currentTimeMillis() - msgP.getStartTime())
                            + "ms" + "\r\n");
                } else {
                    System.out.println("Ping: " + (System.currentTimeMillis() - msgP.getStartTime()));
                    Main.addToChat("Ping: " + (System.currentTimeMillis() - msgP.getStartTime()) + "\r\n");
                }

                break;
        }
    }

}
