package me.riseremi.json;

import me.riseremi.cards.CardsArchivev2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class JSONSLoader {

    private boolean inJSON, inInnerArray;
    private boolean inObject, inInnerObject;
    private HashMap<String, String> hashMap = new HashMap<>();
    private final String[] keys = {"name", "id", "cover", "art", "description", "apcost", "range", "type"};
    private ArrayList<HashMap<String, Object>> effectsList = new ArrayList<>();
    private HashMap<String, Object> tempEffect = new HashMap<>();
    private final String SEPARATOR = Character.toString((char) 31);

    public void process() throws WrongJSONFormatException {
        try {
            InputStream reader = JSONSLoader.class.getResourceAsStream("/res/json/newjson.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            String currentLine, lineToProcess;

            while ((currentLine = br.readLine()) != null) {

                lineToProcess = currentLine.trim();
                if (lineToProcess.contains("[")) {
                    //innerArray start
                    if (inJSON) {
                        inInnerArray = true;

                        lineToProcess = cleanData(lineToProcess);
                    }
                }

                char strStart = lineToProcess.charAt(0);

                switch (strStart) {
                    case '[':
                        if (!inJSON) {
                            inJSON = true;
                        }
                        break;
                    case ']':
                        //array end
                        if (inInnerArray && inJSON) {
                            inInnerArray = false;
                        } else if (!inInnerArray && inJSON) {
                            inJSON = false;
                        }
                        break;
                    case '{':
                        //object start
                        if (inJSON) {
                            if (!inObject && !inInnerObject) {
                                inObject = true;

                            } else if (inObject && !inInnerObject) {
                                inInnerObject = true;
                            }
                        }
                        break;
                    case '}':
                        //object end
                        if (inJSON) {
                            if (inObject && !inInnerObject) {
                                inObject = false;
                                CardsArchivev2 cav2 = new CardsArchivev2();
                                cav2.addCard(hashMap, effectsList);
                                hashMap = new HashMap<>();
                                effectsList = new ArrayList<>();
                            } else if (inObject && inInnerObject) {
                                inInnerObject = false;
                                effectsList.add(tempEffect);
                                tempEffect = new HashMap<>();
                            }
                        }
                        break;
                    case '"':
                        //key:value start
                        if (!lineToProcess.contains("[") && !inInnerObject) {
                            lineToProcess = cleanData(lineToProcess);
                            String[] pair = lineToProcess.split(SEPARATOR);

                            if (checkKey(pair[0])) {
                                hashMap.put(pair[0], pair[1]);
                            }
                        }
                        break;
                }

                if (inJSON) {
                    if (inInnerArray && inInnerObject) {
                        if (strStart == '"') {
                            //effect key:value found
                            lineToProcess = cleanData(lineToProcess);
                            String[] pair = lineToProcess.split(SEPARATOR);
                            tempEffect.put(pair[0], pair[1]);
                        }
                    }
                }

                if (!inJSON) {
                    break;
                }
            }
        } catch (IOException ex) {
        }
    }

    public String cleanData(String str) {
        boolean withinQuotes = false;
        String newStr = "";
        for (int i = 0; i < str.length() - 1; i++) {
            String character = str.substring(i, i + 1);

            if (character.equals("\"")) {
                withinQuotes = !withinQuotes;
            }

            if (character.equals(" ") && !withinQuotes) {
                character = "";
            }

            //replace all : outside the quotes with a separator character
            if (character.equals(":") && !withinQuotes) {
                character = SEPARATOR;
            }

            newStr += character;
        }
        str = newStr;

        str = str.replace("\"", "");
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private boolean checkKey(String key) {
        for (String key1 : keys) {
            if (key1.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
