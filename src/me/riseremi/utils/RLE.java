package me.riseremi.utils;

import java.util.ArrayList;

/**
 *
 * @author Remi
 */
public class RLE {

    //compressing input array
    public static String compress(int[] data) {
        int[] newData = new int[data.length + 1];
        System.arraycopy(data, 0, newData, 0, data.length);

        int sequenceLength = 1;

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < newData.length; i++) {
            if ((newData[i] == newData[i - 1]) && !(i == newData.length - 1)) {
                sequenceLength++;
            } else if (!(i == newData.length - 1)) {
                if (sequenceLength >= 2) {
                    sb.append(sequenceLength).append(":").append(newData[i - 1]).append(" ");
                } else {
                    sb.append(newData[i - 1]).append(" ");
                }
                sequenceLength = 1;
            } else if ((i + 1 == newData.length)) {
                if (sequenceLength >= 2) {
                    sb.append(sequenceLength).append(":").append(newData[i - 1]).append(" ");
                } else if (sequenceLength < 2) {
                    sb.append(newData[i - 1]).append(" ");
                }
            }
        }
        System.out.println("Uncompressed length: " + data.length * 2);
        System.out.println("Compressed length: " + sb.toString().length());

        return sb.toString();
    }

    //decompressing input array
    public static String decompress(String data) {
        ArrayList<Integer> tempLayout = new ArrayList<>();

        String[] tiles = data.split(" ");

        for (int i = 0; i < tiles.length; i++) {
            //if needs expansion
            if (tiles[i].contains(":")) {
                String[] temp = tiles[i].split(":");

                //creating a temp array
                for (int j = 0; j < Utils.getInt(temp[0]); j++) {
                    tempLayout.add(Utils.getInt(temp[1]));
                }
            } else {
                //just add a tile to the tempLayout
                tempLayout.add(Utils.getInt(tiles[i]));
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
            sb.append(String.valueOf(decoded[i])).append(" ");
        }

        return sb.toString();
    }
}
