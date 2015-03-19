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
    public void addCard(HashMap<String, Object> hashMap) {
        String allCardsKey = (String) hashMap.get("name");

        int id = getInt((String) hashMap.get("id"));
//        int power = getInt((String) hashMap.get("power"));
        int apcost = getInt((String) hashMap.get("apcost"));
//        int bloodcost = getInt((String) hashMap.get("bloodcost"));
        int range = getInt((String) hashMap.get("range"));

        BasicCard card = new BasicCard((String) hashMap.get("description"),
                id,
                (String) hashMap.get("image"),
                (String) hashMap.get("art"),
                allCardsKey,
                getEffects(hashMap.get("effects")),
                getType((String) hashMap.get("type")),
                apcost, range);

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
