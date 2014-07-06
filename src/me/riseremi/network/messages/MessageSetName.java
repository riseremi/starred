package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageSetName extends Message {

    private final String name;
    private final int id;

    public MessageSetName(String name, int id) {
        super(Message.Type.SET_NAME);
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public void processServer() {
    }

    @Override
    public void processClient() {
    }

}
