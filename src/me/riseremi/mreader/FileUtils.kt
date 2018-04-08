package me.riseremi.mreader

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author riseremi <riseremi at icloud.com>
 */
internal object FileUtils {

    fun getFileContent(fileName: String): String {
        val start = System.currentTimeMillis()

        println("\nReading file...")

        val reader = FileUtils::class.java.getResourceAsStream(fileName)
        val br = BufferedReader(InputStreamReader(reader))

        val fileContent = StringBuilder()
        var line = br.readLine()
        while (line != null) {
            line = br.readLine()
            fileContent.append(line)
        }
        println("Time spent: " + (System.currentTimeMillis() - start) + " ms")

        return fileContent.toString()
    }
}
