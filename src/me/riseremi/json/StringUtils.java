package me.riseremi.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import me.riseremi.cards.CardsArchivev2;
import me.riseremi.map.layer.IOManager;

/**
 *
 * @author Remi
 */
/*

 [
 {
 "name": "CardName",
 "description": "Simple description of this card.",
 "apcost": "7",
 "bloodcost": "0",
 "damage": "5",
 "range": "3",
 "target": "ENEMY",
 "class": "ClassName"
 }
 ]
 */
public class StringUtils {

    private boolean inJSON, inObject;
    private boolean inString; //for space cleaning
    private boolean nextPairAvailable; //if there is a comma
    private HashMap<String, String> hashMap = new HashMap<>();

    public void bleh() throws WrongJSONFormatException {

        try {
            InputStream reader = IOManager.class.getResourceAsStream("/res/json/test.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            String currentLine;

            StringBuilder sb = new StringBuilder();

            while ((currentLine = br.readLine()) != null) {
                sb.append(currentLine.trim()).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);

            String file = sb.toString();

            //check main json syntax
            if (!((file.startsWith("[")) && (file.endsWith("]")))) {
                throw new WrongJSONFormatException();
            } else {
                file = file.substring(1, file.length() - 1);
            }

            //split by "}," lol
            String[] objects = file.split("},");
            for (int i = 0; i < objects.length - 1; i++) {
                objects[i] += "}";
            }

            for (int i = 0; i < objects.length; i++) {
                String string = objects[i];

                //System.out.println(string);
                if (i + 1 != objects.length) {
                    //System.out.println("=======");
                }
                parseObject(objects[i]);
            }

        } catch (IOException e) {
        }

        //here we are: filled hashmap
        

    }

    //separate by commas, get key:value pairs
    private void parseObject(String obj) {
        String[] strings = obj.split("\n");

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].endsWith(",")) {
                strings[i] = strings[i].substring(0, strings[i].length() - 1);
            }
            //put single k:v right into hashMap
            if (strings[i].length() > 1) {
                getKeyValue(strings[i]);
            }
        }
        //System.out.println(hashMap.toString());
        
        CardsArchivev2 cav2 = new CardsArchivev2();
        //cav2.addCard(hashMap);
    }

    //single string key:value
    private HashMap<String, String> getKeyValue(String str) {
        //HashMap<String, String> hashMap = new HashMap<>();
        String[] kv = str.split(":");
        for (int i = 0; i < kv.length; i++) {
            kv[i] = kv[i].trim();
            kv[i] = kv[i].substring(1, kv[i].length() - 1);
        }

        hashMap.put(kv[0], kv[1]);

        //System.out.println(hashMap.toString());

        //HERE WILL BE CARDSARCHIVE2 CALL
        //System.out.println(kv[0] + ":" + hashMap.get(kv[0]));
        return hashMap;
    }
}
