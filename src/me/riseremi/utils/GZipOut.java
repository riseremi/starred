package me.riseremi.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: remi
 * Date/time: 05.06.13, 11:19
 */
public class GZipOut {
    public GZIPOutputStream out;

    public static void main(String[] args) {
        try {
            new GZipOut().gZipFile("/home/remi/8.m", "/8.m.gz");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Чтение файла
     *
     * @param fileName - путь к файлу, для чтения
     * @return - данные из файла
     */
    public byte[] readFile(String fileName) throws Exception {
        InputStream in = new FileInputStream(fileName);
        byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        return data;
    }

    /**
     * Сжатие массива байт data и сохранение в toFileName
     *
     * @param data       - массив байт, предназначенный для сжатия
     * @param toFileName - путь к файлу, в который сохраняем
     */
    public void gZipArray(byte[] data, String toFileName) throws Exception {
        OutputStream out = new GZIPOutputStream(new FileOutputStream(toFileName));
        out.write(data);
        out.flush();
        out.close();
    }

    /**
     * Применение операций чтения, а затем сжатия
     *
     * @param fromFileName - путь к файлу, которой сжимаем
     * @param toFileName   - путь к файлу, в который сохраняем
     */
    public void gZipFile(String fromFileName, String toFileName) throws Exception {
        byte[] array = readFile(fromFileName);
        gZipArray(array, toFileName);
    }
}