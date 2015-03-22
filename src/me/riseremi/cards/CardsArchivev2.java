package me.riseremi.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import me.riseremi.cards.BasicCard.EffectType;

/**
 *
 * @author Remi
 */
public class CardsArchivev2 {

    private static final HashMap<Integer, BasicCard> allCards = new HashMap<>();
    public static int length;

    public static BasicCard getCard(int id) {

        return allCards.get(id);
    }

    //create a BasicCard with recieved data and put in into archive
    public void addCard(HashMap<String, String> hashMap,
            ArrayList<HashMap<String, Object>> effectsList) {
        String name = (String) hashMap.get("name");

        int id = getInt((String) hashMap.get("id"));
        int apcost = getInt((String) hashMap.get("apcost"));
        int range = getInt((String) hashMap.get("range"));

        Effect[] effects = new Effect[effectsList.size()];

        for (int i = 0; i < effects.length; i++) {
            effects[i] = new Effect();
        }

        for (int i = 0; i < effectsList.size(); i++) {
            //first HashMap with first effect
            final HashMap<String, Object> currentHashMap = effectsList.get(i);
            final Set<String> keySet = currentHashMap.keySet();
            //key:value
            //key
            String effectKey = (String) keySet.toArray()[0];
            String valueKey = (String) keySet.toArray()[1];

            String effectStr = (String) currentHashMap.get(effectKey);
            String valueStr = (String) currentHashMap.get(valueKey);

            effects[i].setEffectType(EffectType.valueOf(effectStr));
            effects[i].setValue(valueStr);
        }

        BasicCard card = new BasicCard((String) hashMap.get("description"),
                id,
                (String) hashMap.get("image"),
                (String) hashMap.get("art"),
                name,
                effects,
                getType((String) hashMap.get("type")),
                apcost, range);

        allCards.put(id, card);
        length = allCards.size();

        //printCards(hashMap, effects);
    }

    private int getInt(String str) {
        return Integer.parseInt(str);
    }

    private BasicCard.Type getType(String str) {
        return BasicCard.Type.valueOf(str);
    }

    /**
     * Print method for debug purposes.
     *
     * @param hashMap hash map with general card info
     * @param effects array with effects
     */
    private void printCards(HashMap<String, String> hashMap, Effect[] effects) {
        System.out.println("");
        System.out.println("===");
        for (int i = 0; i < hashMap.size(); i++) {
            final String key = (String) hashMap.keySet().toArray()[i];
            System.out.println("\t" + key + " = " + hashMap.get(key));
        }

        for (Effect effect : effects) {
            System.out.println("\t" + effect.toString());
        }
        System.out.println("===");
        System.out.println("");
    }
}
