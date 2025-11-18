package com.example.tarot.data

class TarotRepository {
    private val majorCards: List<CardMetadata> = listOf(
        card(
            id = "major_0_fool",
            name = "0 The Fool",
            number = 0,
            archetypalImage = "A youthful traveler steps toward the cliff, accompanied by a loyal dog that nips at his heels.",
            psychologicalMeaning = "Nomadic unconscious energy that invites leaps of faith and embraces the unknown."
        ),
        card(
            id = "major_1_magician",
            name = "I The Magician",
            number = 1,
            archetypalImage = "A figure stands at a table with magical implements, one hand raised to the heavens and the other pointing earthward.",
            psychologicalMeaning = "The activated ego discovering its capacity to focus intention and translate imagination into form."
        ),
        card(
            id = "major_2_papess",
            name = "II The Papess",
            number = 2,
            archetypalImage = "A veiled priestess sits between dark and light pillars, holding a book half-concealed beneath her cloak.",
            psychologicalMeaning = "The receptive anima that gestates mystery and invites stillness so hidden knowledge can surface."
        ),
        card(
            id = "major_3_empress",
            name = "III The Empress",
            number = 3,
            archetypalImage = "A crowned woman rests in a lush garden, scepter in hand, a shield with an eagle at her side.",
            psychologicalMeaning = "Creative fecundity that reconciles opposites and nurtures emerging life into tangible reality."
        ),
        card(
            id = "major_4_emperor",
            name = "IV The Emperor",
            number = 4,
            archetypalImage = "An armored ruler sits upon a stone cube, scepter and orb asserting dominion.",
            psychologicalMeaning = "Logos imposing order, structure, and stability; the impulse to secure and defend the known."
        ),
        card(
            id = "major_16_tower",
            name = "XVI The Tower",
            number = 16,
            archetypalImage = "Lightning crowns a tower and casts figures earthward while flames erupt from the windows.",
            psychologicalMeaning = "A shock that fractures rigid constructs so psychic energy can flow again and illumination can enter."
        ),
        card(
            id = "major_17_star",
            name = "XVII The Star",
            number = 17,
            archetypalImage = "A nude figure kneels by a pool, pouring water from twin vessels beneath eight radiant stars.",
            psychologicalMeaning = "Active imagination guiding the psyche toward hope, authenticity, and gentle renewal."
        )
    )

    private fun card(
        id: String,
        name: String,
        number: Int,
        archetypalImage: String,
        psychologicalMeaning: String
    ): CardMetadata {
        return CardMetadata(
            card = TarotCard(
                id = id,
                arcana = ArcanaType.MAJOR,
                name = name,
                number = number
            ),
            archetypalImage = archetypalImage,
            psychologicalMeaning = psychologicalMeaning
        )
    }

    fun getCardMetadata(cardIds: List<String>): List<CardMetadata> {
        val catalog = (majorCards).associateBy { it.card.id }
        return cardIds.mapNotNull { catalog[it] }
    }

    fun getAllCards(): List<TarotCard> {
        return (majorCards).map { it.card }
    }
}
