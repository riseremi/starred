package me.riseremi.entities;

import me.riseremi.cards.Card;
import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;

/**
 * @author riseremi <riseremi at icloud.com>
 */
public class Player extends Entity {
    // Card picked from the hand.
    // Range selection will be based on the range of this card.
    private Card raisedCard;

    public Player(String name, int imgId, int id, Entity.Type type) {
        super(name, imgId, id, 0, 0, Global.WINDOW_WIDTH, type);
    }

    public void setRaisedCard(Card raisedCard) {
        if (this.getActionPoints() >= raisedCard.getApcost()) {
            this.raisedCard = raisedCard;
            Core_v1.getInstance().setTileSelectionMode(true);
        }
    }

    public Card getRaisedCard() {
        return raisedCard;
    }

    public void resetRaisedCard() {
        this.raisedCard = null;
    }
}
