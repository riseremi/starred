package me.riseremi.map.layer

/**
 * @author LPzhelud
 */
abstract class Layer(private val width: Int, private val height: Int) {
    var blocksX: Int = 0
    var blocksY: Int = 0
    var x: Int = 0
    var y: Int = 0
}
