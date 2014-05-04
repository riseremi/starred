package me.riseremi.network.messages;

import me.riseremi.network.NetworkMessage;

/**
 *
 * @author Riseremi
 */
public class CreateCreepMessage extends NetworkMessage {

    public CreateCreepMessage(String name, int imgId, int id, int x, int y) {
        super(NetworkMessage.CREATE_CREEP_TEST, name + NetworkMessage.SEPARATOR + imgId + NetworkMessage.SEPARATOR + id + NetworkMessage.SEPARATOR + x + NetworkMessage.SEPARATOR + y);
    }
}
