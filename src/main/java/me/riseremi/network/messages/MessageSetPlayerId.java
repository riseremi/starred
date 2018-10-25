package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class MessageSetPlayerId extends Message {

    private final int id;

    public MessageSetPlayerId(int id) {
        super(Message.Type.SET_PLAYER_ID);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void processServer(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processClient(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
