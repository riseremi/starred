package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageChat extends Message {

    private final String text;

    public MessageChat(String text) {
        super(Message.Type.CHAT_MESSAGE);
        this.text = text;
    }

    public String getText() {
        return text;
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
