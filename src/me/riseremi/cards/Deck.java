package me.riseremi.cards;

import me.riseremi.main.Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 * Колода карт, которые находятся на руках у героя.
 * 
 * @author Riseremi
 */
public final class Deck {
    public static final int SIZE = 8;
    @Getter private final ArrayList<BasicCard> cards = new ArrayList<>();
    @Getter @Setter private BasicCard justUsedCard;

    public void paint(Graphics2D g) {
        for (int i = 0; i < cards.size(); i++) {
            int x = Main.getFrames()[0].getWidth() - BasicCard.WIDTH - 12;
            int y = 32 + i * (BasicCard.HEIGHT + 4);
            final BasicCard card = cards.get(i);

            card.setRect(new Rectangle(x, y, BasicCard.WIDTH, BasicCard.HEIGHT));

            g.drawImage(card.getSmallCard(), x, y, null);
            g.drawRect(card.getX(), card.getY(), card.getWidth(), card.getHeight());

            Font trb = new Font("Arial", Font.BOLD, 28);
            g.setFont(trb);

            y += BasicCard.HEIGHT / 3;

            g.setColor(Color.BLACK);
            g.drawString("" + card.getCost(), ShiftWest(x, 1), ShiftNorth(y, 1));
            g.drawString("" + card.getCost(), ShiftWest(x, 1), ShiftSouth(y, 1));
            g.drawString("" + card.getCost(), ShiftEast(x, 1), ShiftNorth(y, 1));
            g.drawString("" + card.getCost(), ShiftEast(x, 1), ShiftSouth(y, 1));
            g.setColor(Color.WHITE);
            g.drawString("" + card.getCost(), x, y);

            if (card.isPaintBig()) {
                g.drawImage(card.getBigCard(), 16, 24, null);
            }
        }
    }

    public BasicCard getActiveCard() {
        for (BasicCard card : cards) {
            if (card.isPaintBig()) {
                return card;
            }
        }
        return null;
    }

    public BasicCard getCard(int slot) {
        return cards.get(slot);
    }

    public void addCard(BasicCard card) {
        if (cards.size() < SIZE) {
            cards.add(card);
        }
    }

    public void removeCard(BasicCard card) {
        cards.remove(card);
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

    int ShiftNorth(int p, int distance) {
        return (p - distance);
    }

    int ShiftSouth(int p, int distance) {
        return (p + distance);
    }

    int ShiftEast(int p, int distance) {
        return (p + distance);
    }

    int ShiftWest(int p, int distance) {
        return (p - distance);
    }
}
