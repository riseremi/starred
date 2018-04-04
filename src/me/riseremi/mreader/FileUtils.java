package me.riseremi.mreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
class FileUtils {

    protected static String getFileContent(String fileName) throws IOException {
        long start = System.currentTimeMillis();

        System.out.println("\nReading file...");

        InputStream reader = FileUtils.class.getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(reader));

        String fileContent = "", line;
        while ((line = br.readLine()) != null) {
            fileContent += line;
        }
        System.out.println("Lapsed: " + (System.currentTimeMillis() - start) + " ms");

        return fileContent;
    }
}
