package me.riseremi.cards;

import java.util.Random;

/**
 *
 * @author Riseremi
 */
public class CardsArchive {

    static final Random rnd = new Random();

    @Otsylka
    public static BasicCard getRandomCard() throws CloneNotSupportedException {
//        return get(rnd.nextInt(4) + 1);
        return get(rnd.nextInt(2) + 1);
    }

    public static BasicCard get(int id) throws CloneNotSupportedException {
        switch (id) {
            default:
                return CardsArchivev2.getCard(id).clone();
        }
    }
}
