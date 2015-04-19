package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageConnect extends Message {

    private final String name;
    private final int iconId;

    public MessageConnect(String name, int iconId) {
        super(Message.Type.CONNECT);
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
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
