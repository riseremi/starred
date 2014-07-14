package me.riseremi.cards;

import java.util.HashMap;

/**
 *
 * @author Remi
 */
public class CardsArchivev2 {

    private HashMap<String, BasicCard> allCards;

    public CardsArchivev2(HashMap<String, String> hashMap) {
        String allCardsKey = hashMap.get("name");

        int id = getInt(hashMap.get("id"));
        int power = getInt(hashMap.get("power"));
        int apcost = getInt(hashMap.get("apcost"));
        int bloodcost = getInt(hashMap.get("bloodcost"));
        int range = getInt(hashMap.get("range"));

        BasicCard card = new BasicCard(id, hashMap.get("image"), allCardsKey,
                BasicCard.Effect.HEAL, BasicCard.Type.PHYS, power, apcost, bloodcost, range);
    }

    private int getInt(String str) {
        return Integer.parseInt(str);
    }

    private BasicCard.Effect getEffect(String str) {
        assert (BasicCard.Effect.valueOf(str) == null);
        return BasicCard.Effect.valueOf(str);
    }

}
