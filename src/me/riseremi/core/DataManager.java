package me.riseremi.core;

import me.riseremi.map.layer.IOManager;
import me.riseremi.map.layer.TiledLayer;
import me.riseremi.map.world.World;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author remi
 */
public class DataManager {

    //TODO исправить метод, ибо некрасивый и вообще говно
    /**
     * Loads map from file to TiledLayer in the current World
     *
     * @param fileName path to the file with the map and its name
     * @param world world instance
     * @throws IOException if file not found
     * @see World
     */
//    public static void newLoadFromFileTo(String fileName, World world) throws IOException {
    public static void newLoadFromFileTo(String fileName, int[][] world) throws IOException {

        /*
         1) открыть файл
         2) прочитать первую строку с layer 0 {
         3) прочитать mapWidth строчек и записать их в в эррейлист
         4) прочитать строку с }
         5) goto 2 until end
         */

        //File f = new File(fileName);
        //System.out.println(f.toPath().toString());
        //System.out.println(f.toString());


        //f.createNewFile();


        //BufferedReader br = new BufferedReader(new FileReader(fileName));

        InputStream reader = IOManager.class.getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(reader));

        int width = Global.horizontalTiles;
        int height = Global.verticalTiles;
        String currentLine;

        //пропускаем линию с layer x {, КОСТЫЛЬ)))000
        br.readLine();

        ArrayList<ArrayList<Integer>> tempLayout = new ArrayList<>();

        //читаем mapHeight линий вниз
        int NUM_OF_LAYERS = 2;
        for (int k = 0; k < NUM_OF_LAYERS; k++) {
            tempLayout.clear();
            for (int j = 0; j < height; j++) {
                currentLine = br.readLine();
                if (currentLine.isEmpty()) {
                    continue;
                }

                ArrayList<Integer> row = new ArrayList<>();
                String[] values = currentLine.trim().split(" ");

                for (int i = 0; i < width; i++) {
                    if (!values[i].isEmpty()) {
                        int id = Integer.parseInt(values[i]);
                        row.add(id);
                    }
                }
                tempLayout.add(row);
            }

            //в tempLayout лежит полная копия слоя, теперь мы переносим её на сам слой
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
//                    getLayer(k).setTile(x, y, tempLayout.get(y).get(x));
                    world[x][y] = tempLayout.get(y).get(x);
                }
            }

            //world[1][1]=5;
            //пропускаем ещё одну строчку, в которой }
            br.readLine();
            br.readLine();
        }


    }

    public static void saveToFile(String fileName) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }

        long start = System.currentTimeMillis();

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            //запись

            for (int i = 0; i < 3; i++) {
                bw.write("layer " + String.valueOf(i) + " {");
                bw.newLine();
                for (int y = 0; y < Global.verticalTiles; y++) {
                    for (int x = 0; x < Global.horizontalTiles; x++) {
                        bw.write(String.valueOf(getLayer(i).getTile(x, y)));
                        bw.write(" ");
                        if (x + 1 == Global.horizontalTiles) {
                            bw.newLine();
                        }
                    }
                }
                bw.write("}");
                bw.newLine();
            }

            bw.flush();
            bw.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }


        long end = System.currentTimeMillis();
        System.out.println("Lapsed: " + String.valueOf(end - start) + " millis");
    }

    //возвращает слой с номером
    public static TiledLayer getLayer(int i) {
        /*switch (i) {
         case 0:
         return MapEditor.getCore().getWorld().getNullLayer();
         case 1:
         return MapEditor.getCore().getWorld().getWorldLayer();
         case 2:
         return MapEditor.getCore().getWorld().getObjectsLayer();
         default:
         return null;
         }*/
        return Core_v1.getInstance().getWorld().getWorldLayer();
    }
}
