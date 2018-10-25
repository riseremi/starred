package me.riseremi.utils

import java.util.*

/**
 * @author riseremi <riseremi at icloud.com>
</riseremi> */
object NameGenerator {
    private val rnd = Random()
    private val names = arrayOf("Jack", "John", "Ken", "Dan", "Sophie", "Mary", "Kate", "Jane")
    private val surnames = arrayOf("White", "Stone", "Locke", "Wheeler", "Groves", "Brent", "Build", "Lincoln")

    fun getName() = names[rnd.nextInt(names.size)] + " " + surnames[rnd.nextInt(surnames.size)]
}
