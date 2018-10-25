package me.riseremi.network.messages;

import me.riseremi.main.Main;
import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class MessageGo extends Message {

    public MessageGo() {
        super(Message.Type.GO);
    }

    @Override
    public void processServer(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processClient(Message message) {
        Main.go();
    }

}
