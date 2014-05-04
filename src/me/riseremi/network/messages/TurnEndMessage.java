package me.riseremi.network.messages;

import me.riseremi.network.NetworkMessage;

/**
 *
 * @author Riseremi
 */
public class TurnEndMessage extends NetworkMessage {
    public TurnEndMessage(int nextPlayerId) {
        super(NetworkMessage.TURN_ENDED, "" + nextPlayerId);
    }
}
