package me.riseremi.json

import me.riseremi.cards.Card
import me.riseremi.cards.CardsArchive
import me.riseremi.cards.Effect
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

/**
 * by riseremi on 14.09.17
 * riseremi@icloud.com
 */
class CardsLoader {
    fun loadCards(filePath: String) {
        val tokener = JSONTokener(CardsLoader::class.java.getResourceAsStream(filePath))
        val cards = JSONArray(tokener)

        repeat(cards.length()) { i ->
            val jsonCard = cards.getJSONObject(i)
            val card = Card.build {
                id = jsonCard.getInt("id")
                name = jsonCard.getString("name")
                description = jsonCard.getString("description")
                type = jsonCard.getString("type")
                framePath = jsonCard.getString("image")
                artPath = jsonCard.getString("art")
                apcost = jsonCard.getInt("apcost")
                // TODO: replace this by normal JSON array
                range = parseRange(jsonCard.getString("range"))
                effects = parseEffects(jsonCard.getJSONArray("effects"))
            }
            CardsArchive.instance.addCard(card)
        }

        println(CardsArchive.instance)
    }

    private fun parseRange(range: String): IntArray {
        return range
                .split("-")
                .map { p -> Integer.parseInt(p) }
                .toIntArray()
    }

    private fun parseEffects(effectsSrc: JSONArray): List<Effect> {
        return effectsSrc.map { effect ->
            val jsonEffect = JSONObject(effect.toString())
            Effect(
                    Card.Companion.EffectType.valueOf(jsonEffect.getString(("name"))),
                    jsonEffect.getInt("value")
            )
        }
    }
}
