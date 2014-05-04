package me.riseremi.map.layer;

import me.riseremi.core.Core_v1;
import me.riseremi.map.world.World;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import me.riseremi.utils.RLE;

/**
 *
 * @author remi
 */
public final class IOManager {

    public static void newLoadFromFileToVersion2(String fileName, World world) throws IOException {
        File f = new File(fileName);
        //f.createNewFile();

        //BufferedReader br = new BufferedReader(new FileReader(fileName));
        //BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
        
        InputStream reader = IOManager.class.getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(reader));

        System.out.println("Loading map...");

        long start = System.currentTimeMillis();

        //String fileContent = MapHash.getFileData(fileName);
        //fileContent = MapHash.decompress(fileContent);
        //System.out.println("NAME: " + fileName);
        String fileContent = br.readLine();
        //fileContent = MapHash.decompress(fileContent);

        int width = Integer.valueOf(getProperty("width", fileContent));
        int height = Integer.valueOf(getProperty("height", fileContent));

        String map = getProperty("map", fileContent);

        String author = getProperty("author", fileContent);
        String name = getProperty("name", fileContent);

        String widthS = getProperty("width", fileContent);
        String heightS = getProperty("height", fileContent);

        String layer0 = getProperty("data-0", fileContent);
        String layer1 = getProperty("data-1", fileContent);
        String layer2 = getProperty("data-2", fileContent);

        //String teleports = getProperty("teleports", fileContent);
        //String animation = getProperty("animation", fileContent);
        //layer0 = MapHash.decode(layer0);
        //layer1 = MapHash.decode(layer1);
        //layer2 = MapHash.decode(layer2);
        layer0 = RLE.decompress(layer0);
        layer1 = RLE.decompress(layer1);
        layer2 = RLE.decompress(layer2);

        ArrayList<ArrayList<Integer>> tempLayout0 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> tempLayout1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> tempLayout2 = new ArrayList<>();

        String[] values0 = layer0.trim().split(" ");
        String[] values1 = layer1.trim().split(" ");
        String[] values2 = layer2.trim().split(" ");

        //новый цикл чтения карты из строки в двумерный массив
        //слой с препятствиями
        int obstacles = 0;
        for (int i = 0; i < height; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                int tile = Integer.parseInt(values0[width * i + j]);
                if (tile != -1) {
                    obstacles++;
                }
                row.add(tile);
            }
            tempLayout0.add(row);
        }

        System.out.println("Loaded " + obstacles + " obstacles.");

        //второй слой с тайлами
        for (int i = 0; i < height; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(Integer.parseInt(values1[width * i + j]));
            }
            tempLayout1.add(row);
        }

        //третий слой с объектами
        for (int i = 0; i < height; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(Integer.parseInt(values2[width * i + j]));
            }
            tempLayout2.add(row);
        }

        System.out.println("Loaded 2 layers.");

        //первый слой - препятствия
        //не трогать, это работает и с XML-версией
        //в tempLayout лежит полная копия слоя, теперь мы переносим её на сам слой
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                getLayer(0).setTile(x, y, tempLayout0.get(y).get(x));
            }
        }

        //второй слой - сплошные тайлы
        //не трогать, это работает и с XML-версией
        //в tempLayout лежит полная копия слоя, теперь мы переносим её на сам слой
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                getLayer(1).setTile(x, y, tempLayout1.get(y).get(x));
            }
        }

        //третий слой - объекты
        //не трогать, это работает и с XML-версией
        //в tempLayout лежит полная копия слоя, теперь мы переносим её на сам слой
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                getLayer(2).setTile(x, y, tempLayout2.get(y).get(x));
            }
        }
        System.out.println("Done!");

        long end = System.currentTimeMillis();
        System.out.println("Lapsed: " + String.valueOf(end - start) + " millis\r\n");
    }

    //загрузка карты из ТХТ ФИЛЕ    
    public static void loadFromFileTo(String fileName, int[][] map) {
        ArrayList<ArrayList<Integer>> tempLayout = new ArrayList<ArrayList<Integer>>();
        try {
            InputStream reader = IOManager.class.getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.isEmpty()) {
                    continue;
                }
                ArrayList<Integer> row = new ArrayList<Integer>();

                String[] values = currentLine.trim().split(" ");
                for (String str : values) {
                    if (!str.isEmpty()) {
                        int id = Integer.parseInt(str);
                        row.add(id);
                    }
                }
                tempLayout.add(row);
            }
        } catch (Exception ex) {
        }
        int width = tempLayout.get(0).size();
        int height = tempLayout.size();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[x][y] = tempLayout.get(y).get(x);
            }
        }
    }

    public static String putProperty(String tag, String content) {
        return openTag(tag, "") + content + closeTag(tag, "");
    }

    public static String openTag(String tag, String number) {
        String prefix = "";
        if (!number.isEmpty()) {
            prefix = "-";
        }
        return "<" + tag + prefix + number + ">";
    }

    public static String closeTag(String tag, String number) {
        String prefix = "";
        if (!number.isEmpty()) {
            prefix = "-";
        }
        return "</" + tag + prefix + number + ">";
    }

    public static String getProperty(String tag, String text) {
        String start = "<" + tag + ">";
        String end = "</" + tag + ">";

        int startIndex = text.indexOf(start);

        text = text.substring(start.length(), text.length());
        text = text.substring(startIndex);

        int endIndex = text.indexOf(end);
        String result = text.substring(0, endIndex);

        return result;
    }

    //возвращает слой с номером
    public static TiledLayer getLayer(int i) {
        switch (i) {
            case 0:
                return Core_v1.getInstance().getWorld().getNullLayer();
            case 1:
                return Core_v1.getInstance().getWorld().getWorldLayer();
            case 2:
                return Core_v1.getInstance().getWorld().getObjectsLayer();
            default:
                return null;
        }
    }
}
