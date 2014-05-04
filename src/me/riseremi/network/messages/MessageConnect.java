package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageConnect extends Message {

    private final String name;

    public MessageConnect(String name) {
        super(Message.Type.CONNECT);
        this.name = name;
    }

    public String getName() {
        return name;
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
