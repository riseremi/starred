package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageSetFriendId extends Message {

    private final int id;

    public MessageSetFriendId(int id) {
        super(Message.Type.SET_FRIEND_ID);
        this.id = id;
    }

    public int getId() {
        return id;
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
