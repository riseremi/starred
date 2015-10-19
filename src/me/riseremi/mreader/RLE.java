package me.riseremi.mreader;

import java.util.ArrayList;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
class RLE {

    // alias for that one below
    public static String decompress(String data) throws WrongFormatException {
        return decompress(data, " ");
    }

    //decompress input array
    public static String decompress(String data, String separator) throws WrongFormatException {
        if (!isStringValid(data)) {
            throw new WrongFormatException();
        }

        ArrayList<Integer> tempLayout = new ArrayList<>();

        String[] tiles = data.split(" ");

        for (String tile : tiles) {
            //if needs expansion
            if (tile.contains(":")) {
                String[] temp = tile.split(":");
                //creating a temp array
                for (int j = 0; j < Integer.parseInt(temp[0]); j++) {
                    tempLayout.add(Integer.parseInt(temp[1]));
                }
            } else {
                //just add the tile to the tempLayout
                tempLayout.add(Integer.parseInt(tile));
            }
        }
        //copy tempLayout to the decodedData
        Object[] objects = tempLayout.toArray();
        int[] decoded = new int[tempLayout.size()];

        for (int i = 0; i < objects.length; i++) {
            decoded[i] = (int) objects[i];
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < decoded.length; i++) {
            sb.append(String.valueOf(decoded[i])).append(separator);
        }

        return sb.toString().trim();
    }

    // check if given string is valid, try to catch most common error cases
    public static boolean isStringValid(String str) {
        boolean noIllegalChars = str.matches("^[0-9\\s:-]+$");
        boolean noSeuqenceOfColons = !str.matches("^.*\\:{2,}.*$");
        boolean containsSpaces = str.contains(" ");

        return noIllegalChars && noSeuqenceOfColons && containsSpaces;
    }
}
