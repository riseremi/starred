package me.riseremi.network.messages;

import me.riseremi.cards.Card;
import me.riseremi.cards.CardsArchive;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import org.rising.framework.network.Message;

/**
 * @author riseremi <riseremi at icloud.com>
 */
public class MessageAttack extends Message {

    private final int userId, targetId, cardId;

    public MessageAttack(int userId, int targetId, int cardId) {
        super(Message.Type.ATTACK);
        this.userId = userId;
        this.targetId = targetId;
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public int getTargetId() {
        return targetId;
    }

    public int getCardId() {
        return cardId;
    }

    @Override
    public void processServer(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void processClient(Message message) {
        MessageAttack msgA = ((MessageAttack) message);
        Core_v1 core = Core_v1.getInstance();

        Card card;
        card = CardsArchive.Companion.getInstance().getCard(msgA.getCardId());

        final Entity user = core.getPlayerById(msgA.getUserId());
        final Entity target = core.getPlayerById(msgA.getTargetId());

//        card.applyEffectFromTo(user, target);
//        card.use(user, target, true);
        user.applyEffects(card, target);
    }
}
