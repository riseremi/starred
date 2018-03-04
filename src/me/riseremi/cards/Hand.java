package me.riseremi.cards;

import me.riseremi.main.Main;
import me.riseremi.utils.Shift;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Колода карт, которые находятся на руках у героя.
 *
 * @author riseremi <riseremi at icloud.com>
 */
public final class Hand {

    public static final int SIZE = 8;
    private final List<DrawableCard> cards = new ArrayList<>();

    public void paint(Graphics2D g) {
        for (int i = 0; i < cards.size(); i++) {
            int x = Main.getFrames()[0].getWidth() - DrawableCard.PREVIEW_WIDTH - 12;
            int y = 32 + i * (DrawableCard.PREVIEW_HEIGHT + 4);
            final DrawableCard card = cards.get(i);

            Rectangle paintRect = new Rectangle(x, y, DrawableCard.PREVIEW_WIDTH, DrawableCard.PREVIEW_HEIGHT);
            card.setCollisionRectangle(paintRect);

            g.drawImage(card.getPreview(), x, y, null);
            g.drawRect(paintRect.x, paintRect.y, paintRect.width, paintRect.height);
            g.drawRect(x, y, DrawableCard.PREVIEW_WIDTH, DrawableCard.PREVIEW_HEIGHT);

            Font trb = new Font("Arial", Font.BOLD, 28);
            g.setFont(trb);

            y += DrawableCard.PREVIEW_HEIGHT / 3;

            g.setColor(Color.BLACK);
            g.drawString("" + card.getCard().getApcost(), Shift.Companion.ShiftWest(x, 1), Shift.Companion.ShiftNorth(y, 1));
            g.drawString("" + card.getCard().getApcost(), Shift.Companion.ShiftWest(x, 1), Shift.Companion.ShiftSouth(y, 1));
            g.drawString("" + card.getCard().getApcost(), Shift.Companion.ShiftEast(x, 1), Shift.Companion.ShiftNorth(y, 1));
            g.drawString("" + card.getCard().getApcost(), Shift.Companion.ShiftEast(x, 1), Shift.Companion.ShiftSouth(y, 1));

            //red color for cards with blood cost
            g.setColor(Color.WHITE);
            g.drawString("" + card.getCard().getApcost(), x, y);

            g.setColor(Color.WHITE);

            //draw a big card cover
            if (card.getHover()) {
                g.drawImage(card.getCover(), 16, 24, null);
            }
        }
    }

    public DrawableCard getActiveCard() {
        for (DrawableCard card : cards) {
            if (card.getHover()) {
                return card;
            }
        }
        return null;
    }

    public DrawableCard getCard(int slot) {
        return cards.get(slot);
    }

    public List<DrawableCard> getCards() {
        return cards;
    }

    public void addCard(DrawableCard card) {
        if (cards.size() < SIZE) {
            cards.add(card);
        }
    }

    public void removeCard(DrawableCard card) {
        cards.remove(card);
    }

    public void removeLastCard() {
        if (cards.size() > 0) {
            cards.remove(cards.size() - 1);
        }
    }
//
//    public void removeCard(int slot) {
//        if (cards.size() > 0) {
//            cards.remove(cards.get(slot));
//        }
//    }

    public int size() {
        return cards.size();
    }

    public void switchPaint(Rectangle mouseRect) {
        for (DrawableCard card : getCards()) {
            if (mouseRect.intersects(card.getCollisionRectangle())) {
                card.setHover(true);
            } else {
                card.setHover(false);
            }
        }
    }
}
