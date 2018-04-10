package me.riseremi.mreader

import java.io.File

/**
 * @author riseremi <riseremi at icloud.com>
 */
internal object FileUtils {

    fun getFileContent(fileName: String): String {
        val start = System.currentTimeMillis()

        println("\nReading file...")

        val path = javaClass.getResource(fileName)
        val inputStream = File(path.path).inputStream()
        val inputString = inputStream.bufferedReader().use({ it.readText() })

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
