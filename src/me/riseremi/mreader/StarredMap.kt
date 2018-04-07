package me.riseremi.mreader

/**
 * @author riseremi <riseremi at icloud.com>
 */
class StarredMap(fileName: String) {
    var author: String? = null
    var width: Int = 0
    var height: Int = 0
    var obstaclesLayer: Array<IntArray>? = null
    var backgroundLayer: Array<IntArray>? = null
    var decorationsLayer: Array<IntArray>? = null

    init {
        parse(FileUtils.getFileContent(fileName))
    }

    private fun parse(fileContent: String) {
        author = XMLUtils.getProperty("author", fileContent)
        width = Integer.parseInt(XMLUtils.getProperty("width", fileContent))
        height = Integer.parseInt(XMLUtils.getProperty("height", fileContent))

        val layers = XMLUtils.parse(fileContent, width, height)

        obstaclesLayer = layers[0]
        backgroundLayer = layers[1]
        decorationsLayer = layers[2]
    }
}
