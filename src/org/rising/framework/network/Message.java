package org.rising.framework.network;

import java.io.Serializable;

/**
 *
 * @author Remi
 */
public abstract class Message implements Serializable {

    private final Type type;

    public Message(Type type) {
        this.type = type;
    }

    public enum Type {

        SET_PLAYER_NAME, SET_FRIEND_NAME, SET_NAME, CHAT_MESSAGE, PURE_ATTACK_TEST,
        TURN_END, ATTACK, SET_PLAYER_ID, SET_FRIEND_ID, SET_POSITION, CREATE_PLAYER,
        CREATE_NOT_PLAYER, CONNECT, PING_MESSAGE, GAMEOVER_MESSAGE,
        ADD_TO_THE_LOBBY, GO, SET_ICON_ID
    }

    public Type getType() {
        return type;
    }

    public abstract void processServer(Message message);

    public abstract void processClient(Message message);

}
