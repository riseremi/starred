package me.riseremi.network.messages;

import me.riseremi.core.Core_v1;
import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class MessageGameOver extends Message {

    private final int winnerId;

    public MessageGameOver(int winnerId) {
        super(Message.Type.GAMEOVER_MESSAGE);
        this.winnerId = winnerId;
    }
    
    public int getWinnerId() {
        return winnerId;
    }

    @Override
    public void processServer(Message message) {
    }

    @Override
    public void processClient(Message message) {
        MessageGameOver msg = (MessageGameOver) message;
        
        Core_v1.getInstance().setWinnerId(msg.getWinnerId());
        Core_v1.getInstance().setGameOver(true);
    }

}
