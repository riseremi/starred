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

    protected static String getFileContent(String fileName, boolean debug) throws IOException {
        long start = System.currentTimeMillis();

        Logger.debug("\nReading file...", debug);

        InputStream reader = FileUtils.class.getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(reader));

        String fileContent = "", line;
        while ((line = br.readLine()) != null) {
            fileContent += line;
        }
        Logger.debug("Lapsed: " + (System.currentTimeMillis() - start) + " ms", debug);

        return fileContent;
    }
}
