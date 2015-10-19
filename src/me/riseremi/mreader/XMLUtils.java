package me.riseremi.mreader;

import java.util.ArrayList;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class XMLUtils {

    protected static ArrayList<int[][]> parse(String fileContent, int width, int height, boolean debug) throws WrongFormatException {
        ArrayList<int[][]> layers = new ArrayList<>();
        int[][] obstaclesLayerArr = new int[width][height],
                backgroundLayerArr = new int[width][height],
                decorationsLayerArr = new int[width][height];

        String obstaclesLayerString = RLE.decompress(getProperty("data-0", fileContent));
        String backgroundLayerString = RLE.decompress(getProperty("data-1", fileContent));
        String decorationsLayerString = RLE.decompress(getProperty("data-2", fileContent));

        ArrayList<ArrayList<Integer>> obstaclesLayer = new ArrayList<>();
        ArrayList<ArrayList<Integer>> backgroundLayer = new ArrayList<>();
        ArrayList<ArrayList<Integer>> decorationsLayer = new ArrayList<>();

        String[] values0 = obstaclesLayerString.trim().split(" ");
        String[] values1 = backgroundLayerString.trim().split(" ");
        String[] values2 = decorationsLayerString.trim().split(" ");

        loadLayer(width, height, values0, obstaclesLayer);
        loadLayer(width, height, values1, backgroundLayer);
        loadLayer(width, height, values2, decorationsLayer);

        // convert layers to int[][]
        moveCells(width, height, obstaclesLayer, obstaclesLayerArr);
        moveCells(width, height, backgroundLayer, backgroundLayerArr);
        moveCells(width, height, decorationsLayer, decorationsLayerArr);

        layers.add(obstaclesLayerArr);
        layers.add(backgroundLayerArr);
        layers.add(decorationsLayerArr);

        return layers;

    }

    private static void moveCells(int width, int height, ArrayList<ArrayList<Integer>> source, int[][] destination) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                destination[x][y] = source.get(y).get(x);
            }
        }
    }

    private static void loadLayer(int width, int height, String[] values, ArrayList<ArrayList<Integer>> layer) {
        for (int i = 0; i < height; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(Integer.parseInt(values[width * i + j]));
            }
            layer.add(row);
        }
    }

    protected static String getProperty(String tag, String text) {
        String openTag = "<" + tag + ">";
        String closeTag = "</" + tag + ">";

        int startIndex = text.indexOf(openTag);

        text = text.substring(openTag.length(), text.length());
        text = text.substring(startIndex);

        int endIndex = text.indexOf(closeTag);
        String result = text.substring(0, endIndex);

        return result;
    }
}
