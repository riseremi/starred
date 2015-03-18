package me.riseremi.cards;

import java.util.HashMap;

/**
 *
 * @author Remi
 */
public class CardsArchivev2 {

    private static final HashMap<Integer, BasicCard> allCards = new HashMap<>();

    public static BasicCard getCard(int id) {

        return allCards.get(id);
    }

    //create a BasicCard with recieved data and put in into archive
    public void addCard(HashMap<String, String> hashMap) {
        String allCardsKey = hashMap.get("name");

        int id = getInt(hashMap.get("id"));
        int power = getInt(hashMap.get("power"));
        int apcost = getInt(hashMap.get("apcost"));
        int bloodcost = getInt(hashMap.get("bloodcost"));
        int range = getInt(hashMap.get("range"));

        BasicCard card = new BasicCard(hashMap.get("description"),
                id,
                hashMap.get("image"),
                hashMap.get("art"),
                allCardsKey,
                getEffects(hashMap.get("effects")),
                getType(hashMap.get("type")),
                power, apcost, bloodcost, range);

        allCards.put(id, card);

        System.out.println(allCards.toString());
    }

    private int getInt(String str) {
        return Integer.parseInt(str);
    }

    private BasicCard.Effect[] getEffects(Object str) {
        BasicCard.Effect[] effects = (BasicCard.Effect[]) str;
        return effects;
    }

    private BasicCard.Type getType(String str) {
        return BasicCard.Type.valueOf(str);
    }

}
