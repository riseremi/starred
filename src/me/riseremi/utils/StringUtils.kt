package me.riseremi.utils

/**
 * by riseremi on 26/03/2018
 * riseremi@icloud.com
 */

fun splitInChunks(chunkLength: Int, string: String): ArrayList<String> {
    val strings = ArrayList<String>()

    var index = 0
    while (index < string.length) {
        strings.add(string.substring(index, Math.min(index + chunkLength, string.length)))
        index += chunkLength
    }

    return strings
}