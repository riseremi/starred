package me.riseremi.network.messages;

import me.riseremi.core.Core_v1;
import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageSetIconId extends Message {

    private final int id;

    public MessageSetIconId(int id) {
        super(Message.Type.SET_ICON_ID);
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
        Core_v1.getInstance().getFriend().setImage(((MessageSetIconId)message).getId());
    }

}
