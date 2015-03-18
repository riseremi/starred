package me.riseremi.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import me.riseremi.map.layer.IOManager;

/**
 *
 * @author riseremi
 */
public class StringUtilsv2 {

    private boolean inJSON, inInnerArray, inKeyValueString;
    private boolean inObject, inInnerObject;
    private final HashMap<String, Object> hashMap = new HashMap<>();

    public void process() throws WrongJSONFormatException {
        try {
            InputStream reader = IOManager.class.getResourceAsStream("/res/json/newjson.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            String currentLine, lineToProcess;

            StringBuilder sb = new StringBuilder();

            while ((currentLine = br.readLine()) != null) {
                //sb.append(currentLine.trim()).append("\n");

                lineToProcess = currentLine.trim();

                System.out.print(currentLine.trim());

                //cases:
                //[ or ]
                //{ or }
                //"xxx": "yyy",
                //"xxx": [
                if (lineToProcess.contains("[")) {
                    //innerArray start
                    if (inJSON) {
                        inInnerArray = true;
                        System.out.println("\t\tSTART OF INNER ARRAY");
                    }
                }

                char strType = lineToProcess.charAt(0);

                switch (strType) {
                    case '[':
                        //JSON start
                        inJSON = true;
                        System.out.println("\t\t\t\tSTART OF JSON ^");
                        break;
                    case ']':
                        //array end
                        if (inInnerArray && inJSON) {
                            inInnerArray = false;
                            System.out.println("\t\t\t\tEND OF INNER ARRAY ^");
                        } else if (!inInnerArray && inJSON) {
                            inJSON = false;
                            System.out.println("\t\t\t\tEND OF JSON ^");
                        }
                        break;
                    case '{':
                        //object start
                        if (inJSON) {
                            if (!inObject && !inInnerObject) {
                                inObject = true;
                                System.out.println("\t\t\t\tOBJECT START");
                            } else if (inObject && !inInnerObject) {
                                inInnerObject = true;
                                System.out.println("\t\t\t\tINNER OBJECT START");
                            }
                        }
//                        System.out.println();
                        break;
                    case '}':
                        //object end
                        if (inJSON) {
                            if (inObject && !inInnerObject) {
                                inObject = false;
                                System.out.println("\t\t\t\tOBJECT END");
                            } else if (inObject && inInnerObject) {
                                inInnerObject = false;
                                System.out.println("\t\t\t\tINNER OBJECT END");
                            }
                        }

//                        System.out.println("");
                        break;
                    case '"':
                        //key:value start
                        if (!lineToProcess.contains("[")) {
                            System.out.println("\t\tKEY:VALUE STRING ^");
                        }
                        break;
                }

                if (!inJSON) {
//                    System.out.println("......END OF JSON");
                    System.exit(0);
                    break;
                }
            }
            //sb.deleteCharAt(sb.length() - 1);

            //String file = sb.toString();
        } catch (IOException ex) {
        }
    }
}
