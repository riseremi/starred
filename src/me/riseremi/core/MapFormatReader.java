package me.riseremi.core;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.riseremi.mreader.StarredMap;
import me.riseremi.mreader.WrongFormatException;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class MapFormatReader {

    public static void main(String[] args) {
        String fileName = "/res/new_map2.m";

        try {
            StarredMap map = new StarredMap(fileName);

            System.out.println(map.getAuthor());

        } catch (WrongFormatException | IOException ex) {
            Logger.getLogger(MapFormatReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
