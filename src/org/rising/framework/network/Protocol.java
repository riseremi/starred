package org.rising.framework.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.network.messages.MessageAddToTheLobby;
import me.riseremi.network.messages.MessageChat;
import me.riseremi.network.messages.MessageConnect;
import me.riseremi.network.messages.MessageSetFriendId;
import me.riseremi.network.messages.MessageSetIconId;
import me.riseremi.network.messages.MessageSetName;
import me.riseremi.network.messages.MessageSetPlayerId;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class Protocol {

    private static final Random rnd = new Random();
    private static final ArrayList<Entity> players = new ArrayList<>();

    public static void processMessageOnServerSide(final Message message, int id) throws IOException {
        Message.Type type = message.getType();

        switch (type) {
            case CONNECT:
                System.out.println("[SERVER] Incoming connection: " + id);
                final MessageConnect connectMessage = (MessageConnect) message;
                final String name = connectMessage.getName();
                final int iconId = connectMessage.getIconId();

                Player p = new Player(name, iconId, id, Entity.Type.PLAYER);

                players.add(p);

                if (players.size() == 2) {
                    for (Entity e : players) {
                        Server.getInstance().sendToOne(
                                new MessageSetPlayerId(e.getId()), e.getId());

                        Server.getInstance().sendToAllExcludingOne(
                                new MessageSetFriendId(e.getId()), e.getId());

                        Server.getInstance().sendToAll(
                                new MessageSetName(e.getName(), e.getId()));

                        Server.getInstance().sendToAllExcludingOne(
                                new MessageSetIconId(e.getImgId()), e.getId());

                        Server.getInstance().sendToAllExcludingOne(
                                new MessageAddToTheLobby(e.getName()), e.getId());
                    }
                    Main.getLobbyScreen().setCanGo();
                }
                break;
            case CHAT_MESSAGE:
            case SET_POSITION:
            case ATTACK:
                Server.getInstance().sendToAll(message);
                break;
            case TURN_END:
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
            case TURN_END:
                core.startTurn();
                break;
            case ATTACK:
            case GAMEOVER_MESSAGE:
            case ADD_TO_THE_LOBBY:
            case SET_POSITION:
            case GO:
            case SET_ICON_ID:
                message.processClient(message);
                break;
        }
    }

}
