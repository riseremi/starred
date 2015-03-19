package me.riseremi.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import me.riseremi.cards.CardsArchivev2;
import me.riseremi.map.layer.IOManager;

/**
 *
 * @author riseremi
 */
public class StringUtilsv2 {

    private boolean inJSON, inInnerArray, inKeyValueString;
    private boolean inObject, inInnerObject;
    private final HashMap<String, Object> hashMap = new HashMap<>();
    private String effects = "effects";
    private final String[] keys = {"name", "id", "image", "art", "description", "apcost", "range", "type"};
    private ArrayList<HashMap<String, Object>> effectsList = new ArrayList<>();
    private HashMap<String, Object> tempEffect = new HashMap<>();

    public void process() throws WrongJSONFormatException {
        try {
            InputStream reader = IOManager.class.getResourceAsStream("/res/json/newjson.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            String currentLine, lineToProcess;

            StringBuilder sb = new StringBuilder();

            while ((currentLine = br.readLine()) != null) {
                //sb.append(currentLine.trim()).append("\n");

                lineToProcess = currentLine.trim();

//                System.out.print(currentLine.trim());
                //cases:
                //[ or ]
                //{ or }
                //"xxx": "yyy",
                //"xxx": [
                if (lineToProcess.contains("[")) {
                    //innerArray start
                    if (inJSON) {
                        inInnerArray = true;
//                        System.out.println("\t\tSTART OF INNER ARRAY");

                        lineToProcess = cleanData(lineToProcess);
                        String[] pair = lineToProcess.split(":");

                        if (effects.equals(pair[0])) {
//                            System.out.println("EFFECTS ARRAY WAS FOUND");
                        }
                    }
                }

                char strStart = lineToProcess.charAt(0);

                switch (strStart) {
                    case '[':
                        //JSON start
                        inJSON = true;
//                        System.out.println("\t\t\t\tSTART OF JSON ^");
                        break;
                    case ']':
                        //array end
                        if (inInnerArray && inJSON) {
                            inInnerArray = false;
//                            System.out.println("\t\t\t\tEND OF INNER ARRAY ^");
                        } else if (!inInnerArray && inJSON) {
                            inJSON = false;
//                            System.out.println("\t\t\t\tEND OF JSON ^");
                        }
                        break;
                    case '{':
                        //object start
                        if (inJSON) {
                            if (!inObject && !inInnerObject) {
                                inObject = true;
//                                System.out.println("\t\t\t\tOBJECT START");

                            } else if (inObject && !inInnerObject) {
                                inInnerObject = true;
//                                System.out.println("\t\t\t\tINNER OBJECT START");
                            }
                        }
                        break;
                    case '}':
                        //object end
                        if (inJSON) {
                            if (inObject && !inInnerObject) {
                                inObject = false;
//                                System.out.println("\t\t\t\tOBJECT END");
                            } else if (inObject && inInnerObject) {
                                inInnerObject = false;
//                                System.out.println("\t\t\t\tINNER OBJECT END");

                                effectsList.add(tempEffect);
                                tempEffect = new HashMap<>();
                            }
                        }
                        break;
                    case '"':
                        //key:value start
                        if (!lineToProcess.contains("[")) {
//                            System.out.println("\t\tKEY:VALUE STRING ^");

                            lineToProcess = cleanData(lineToProcess);
                            String[] pair = lineToProcess.split(":");

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
                            //System.out.println("before " + currentLine);
                            String[] pair = lineToProcess.split(":");
                            tempEffect.put(pair[0], pair[1]);

//                            System.out.println("tempEffect: " + tempEffect.toString());
                        }
                    }
                }

                if (!inJSON) {
//                    System.out.println("......END OF JSON");
//                    System.out.println("hashMap: " + hashMap.toString());

                    System.out.println("Data:");
                    for (int i = 0; i < hashMap.size(); i++) {
                        final String key = (String) hashMap.keySet().toArray()[i];
                        System.out.println(key + " = " + hashMap.get(key));
                    }

                    //System.out.println("list: " + effectsList.toString());
                    System.out.println();

                    System.out.println("Effects:");
                    for (HashMap<String, Object> map : effectsList) {
                        for (int i = 0; i < map.size(); i++) {
                            final String key = (String) map.keySet().toArray()[i];
                            System.out.print(map.get(key) + (i == map.size() - 1 ? "" : " = "));
                        }
                        System.out.println();
                    }
                    
                    CardsArchivev2 cav2 = new CardsArchivev2();
                    cav2.addCard(hashMap);

                    System.exit(0);
                    break;
                }
            }
            //sb.deleteCharAt(sb.length() - 1);

            //String file = sb.toString();
        } catch (IOException ex) {
        }
    }

    public String cleanData(String str) {
        str = str.replace(": ", ":");
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
