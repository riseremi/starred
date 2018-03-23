package me.riseremi.cards

import java.util.*
import kotlin.collections.HashMap

/**
 * by riseremi on 14.09.17
 * riseremi@icloud.com
 */
class CardsArchive {
    private var cards: HashMap<Int, Card> = HashMap()
    private val random = Random()

    private object Holder {
        val INSTANCE = CardsArchive()
    }

    companion object {
        val instance: CardsArchive by lazy { Holder.INSTANCE }
    }

    fun addCard(card: Card) {
        cards[card.id] = card
    }

    fun getRandomCard(): Card? {
        val indices = cards.keys
        val id = random.nextInt(indices.size - 1)

        // TODO: add error reporting
        return cards[indices.elementAt(id)]
                ?: throw Exception("Requested random card not found in CardsArchive (id=$id).")
    }

    fun getCard(id: Int): Card = cards[id]
            ?: throw NoSuchElementException("Requested card not found in CardsArchive (id=$id).")

    override fun toString(): String = cards.toString()
}
