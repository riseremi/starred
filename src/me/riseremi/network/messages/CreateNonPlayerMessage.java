package me.riseremi.network.messages;

import me.riseremi.network.NetworkMessage;

/**
 *
 * @author Riseremi
 */
public class CreateNonPlayerMessage extends NetworkMessage {

    public CreateNonPlayerMessage(String name, int imgId, int id, int x, int y) {
        super(NetworkMessage.CREATE_NOT_PLAYER, name + NetworkMessage.SEPARATOR + imgId + NetworkMessage.SEPARATOR + id + NetworkMessage.SEPARATOR + x + NetworkMessage.SEPARATOR + y);
    }
}
