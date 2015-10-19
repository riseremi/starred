package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class MessageChat extends Message {

    private final String text;
    private final int id;

    public int getId() {
        return id;
    }

    public MessageChat(String text, int id) {
        super(Message.Type.CHAT_MESSAGE);
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
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
