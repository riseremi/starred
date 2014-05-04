package me.riseremi.network.messages;

import me.riseremi.network.NetworkMessage;

/**
 *
 * @author Riseremi
 */
public class PureDamageMessage extends NetworkMessage {
    public PureDamageMessage(int id, int power) {
        super(NetworkMessage.PURE_ATTACK_TEST, id + NetworkMessage.SEPARATOR + power);
    }
}
