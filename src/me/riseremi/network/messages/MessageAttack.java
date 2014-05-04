package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageAttack extends Message {

    private final int userId, targetId, cardId;

    public MessageAttack(int userId, int targetId, int cardId) {
        super(Message.Type.ATTACK_TEST);
        this.userId = userId;
        this.targetId = targetId;
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public int getTargetId() {
        return targetId;
    }

    public int getCardId() {
        return cardId;
    }

    @Override
    public void processServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processClient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
