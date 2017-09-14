package me.riseremi.cards;

import java.util.Random;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class CardsArchive {

    static final Random rnd = new Random();

    public static BasicCard getRandomCard() throws CloneNotSupportedException {
        return get(rnd.nextInt(CardsArchivev2.length) + 1);
    }

    public static BasicCard get(int id) throws CloneNotSupportedException {
        switch (id) {
            default:
                return CardsArchivev2.getCard(id).clone();
        }
    }
}
