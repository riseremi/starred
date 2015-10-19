package me.riseremi.network.messages;

import org.rising.framework.network.Message;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class MessagePing extends Message {

    private final long startTime;
    private final boolean singleSide;

    public MessagePing(long startTime, boolean singleSide) {
        super(Message.Type.PING_MESSAGE);
        this.startTime = startTime;
        this.singleSide = singleSide;
    }

    public long getStartTime() {
        return startTime;
    }
    
    public boolean isSingleSide() {
        return singleSide;
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
