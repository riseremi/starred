package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
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
    public void processServer(Message message) {
    }

    @Override
    public void processClient(Message message) {
    }

}
