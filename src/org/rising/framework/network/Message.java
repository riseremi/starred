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
        TURN_ENDED, ATTACK_TEST, SET_PLAYER_ID, SET_FRIEND_ID, SET_POSITION, CREATE_PLAYER,
        CREATE_NOT_PLAYER, CONNECT, PING_MESSAGE
    }

    public Type getType() {
        return type;
    }

    public abstract void processServer();

    public abstract void processClient();

}
