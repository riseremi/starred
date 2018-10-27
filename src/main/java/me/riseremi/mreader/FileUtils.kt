package me.riseremi.mreader

import me.riseremi.utils.loadStream

/**
 * @author riseremi <riseremi at icloud.com>
 */
internal object FileUtils {

    fun getFileContent(fileName: String): String {
        val start = System.currentTimeMillis()

        println("\nReading file...")

        val inputStream = loadStream(fileName)
        val inputString = inputStream.bufferedReader().use { it.readText() }

//        val reader = FileUtils::class.java.getResourceAsStream(fileName)
//        val br = BufferedReader(InputStreamReader(reader))
//
//        val fileContent = StringBuilder()
//        var line = br.readLine()
//        while (line != null) {
//            line = br.readLine()
//            fileContent.append(line)
//        }
        println("Time spent: " + (System.currentTimeMillis() - start) + " ms")

        return inputString
    }
}
