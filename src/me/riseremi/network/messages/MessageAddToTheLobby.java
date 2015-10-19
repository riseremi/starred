package me.riseremi.network.messages;

import me.riseremi.main.Main;
import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class MessageAddToTheLobby extends Message {

    private final String name;

    public MessageAddToTheLobby(String name) {
        super(Message.Type.ADD_TO_THE_LOBBY);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void processServer(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processClient(Message message) {
        final MessageAddToTheLobby message1 = (MessageAddToTheLobby) message;
        Main.getLobbyScreen().getPlayersListModel().addElement(message1.getName());
        System.out.println("Added: " + message1.getName());
    }

}
