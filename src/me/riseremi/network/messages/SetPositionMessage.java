package me.riseremi.network.messages;

import me.riseremi.network.NetworkMessage;

/**
 *
 * @author Remi
 */
public class SetPositionMessage extends NetworkMessage {

    public SetPositionMessage(int id, int x, int y) {
        super(NetworkMessage.SET_POSITION, id + NetworkMessage.SEPARATOR + x + NetworkMessage.SEPARATOR + y);
    }

}
