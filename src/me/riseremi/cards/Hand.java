package me.riseremi.cards;

import lombok.Getter;
import lombok.Setter;
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
    @Getter
    private final List<DrawableCard> cards = new ArrayList<>();
    @Getter
    @Setter
    private BasicCard justUsedCard;

    public void paint(Graphics2D g) {
        for (int i = 0; i < cards.size(); i++) {
            int x = Main.getFrames()[0].getWidth() - BasicCard.WIDTH - 12;
            int y = 32 + i * (BasicCard.HEIGHT + 4);
            final DrawableCard card = cards.get(i);

//            card.setRect(new Rectangle(x, y, BasicCard.WIDTH, BasicCard.HEIGHT));
            Rectangle paintRect = new Rectangle(x, y, BasicCard.WIDTH, BasicCard.HEIGHT);

            g.drawImage(card.getPreview(), x, y, null);
//            g.drawRect(card.getX(), card.getY(), card.getWidth(), card.getHeight());
            g.drawRect(x, y, BasicCard.WIDTH, BasicCard.HEIGHT);

            Font trb = new Font("Arial", Font.BOLD, 28);
            g.setFont(trb);

            y += BasicCard.HEIGHT / 3;

            g.setColor(Color.BLACK);
            g.drawString("" + card.getCard().getApcost(), Shift.ShiftWest(x, 1), Shift.ShiftNorth(y, 1));
            g.drawString("" + card.getCard().getApcost(), Shift.ShiftWest(x, 1), Shift.ShiftSouth(y, 1));
            g.drawString("" + card.getCard().getApcost(), Shift.ShiftEast(x, 1), Shift.ShiftNorth(y, 1));
            g.drawString("" + card.getCard().getApcost(), Shift.ShiftEast(x, 1), Shift.ShiftSouth(y, 1));

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

    public void removeCard(int slot) {
        if (cards.size() > 0) {
            cards.remove(cards.get(slot));
        }
    }

    public int size() {
        return cards.size();
    }

    public void switchPaint(Rectangle mouseRect) {
        for (BasicCard card : getCards()) {
            if (mouseRect.intersects(card.getRect())) {
                card.setPaintBig(true);
            } else {
                card.setPaintBig(false);
            }
        }
    }
}
