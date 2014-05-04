package me.riseremi.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Riseremi
 */
public class MapHash {

    public static String encodev2(String data) {
        byte[] message = data.getBytes();
        String encoded = DatatypeConverter.printBase64Binary(message);
        return encoded;
    }

    public static String decodev2(String data) {
        byte[] decoded = DatatypeConverter.parseBase64Binary(data);
        return new String(decoded);
    }

    //заменять нужно СИЕ
    //пробел. лол.
    public static String encode(String data) {
        data = data.replace("-", "m");
        return data.replace(" ", "s");
    }

    public static String decode(String data) {
        data = data.replace("m", "-");
        return data.replace("s", " ");
    }

    public static void tryToWriteGZIP() {
        String text = "a string of characters";
        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream("C:/test.txt"));

            GZIPOutputStream gzip = new GZIPOutputStream(os);
            gzip.write(text.getBytes("UTF-8"));
        } catch (Throwable t) {
        }
    }

    public static void decompressGzipFile(String gzipFile, String newFile) {
        try {
            FileInputStream fis = new FileInputStream(gzipFile);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            //close resources
            fos.close();
            gis.close();
        } catch (IOException e) {
        }
    }

    public static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        System.out.println("String length : " + str.length());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("ISO-8859-1");
        System.out.println("Output String lenght : " + outStr.length());
        return outStr;
    }

    public static String decompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        System.out.println("Input String length : " + str.length());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String line;

        StringBuilder sb = new StringBuilder();
        while ((line = bf.readLine()) != null) {
            sb.append(line);
        }
        System.out.println("Output String lenght : " + sb.length());
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String filePath = "C:/Users/Remi/Documents/test_house.m";

        String decomp = decompress(getFileData(filePath));
        System.out.println(decomp.substring(0, decomp.length() / 100));
//        String string = getFileData(filePath);
//        System.out.println("after compress:");
//        String compressed = compress(string);
//        System.out.println(compressed);
//        System.out.println("after decompress:");
//        String decomp = decompress(compressed);
//        System.out.println(decomp.substring(0, 1000));
    }

    public static String getFileData(String filepath) throws FileNotFoundException, IOException {
        BufferedReader bf = new BufferedReader(new FileReader(filepath));
        String data = "";
        String line;
        while ((line = bf.readLine()) != null) {
            data += line;
        }
        return data;
    }
}
