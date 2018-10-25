package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
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
    public void processServer(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processClient(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
