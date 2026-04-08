package com.jungtarot.app.data

class TarotRepository {

    val spanishNames: Map<String, String> = mapOf(
        "major_0_fool"        to "El Loco",
        "major_1_magician"    to "El Mago",
        "major_2_papess"      to "La Papisa",
        "major_3_empress"     to "La Emperatriz",
        "major_4_emperor"     to "El Emperador",
        "major_5_pope"        to "El Papa",
        "major_6_lovers"      to "El Enamorado",
        "major_7_chariot"     to "El Carro",
        "major_8_justice"     to "La Justicia",
        "major_9_hermit"      to "El Ermitaño",
        "major_10_wheel"      to "La Rueda de la Fortuna",
        "major_11_strength"   to "La Fuerza",
        "major_12_hanged_man" to "El Colgado",
        "major_13_death"      to "El Arcano Sin Nombre",
        "major_14_temperance" to "La Templanza",
        "major_15_devil"      to "El Diablo",
        "major_16_tower"      to "La Torre",
        "major_17_star"       to "La Estrella",
        "major_18_moon"       to "La Luna",
        "major_19_sun"        to "El Sol",
        "major_20_judgement"  to "El Juicio",
        "major_21_world"      to "El Mundo"
    )
    private val majorCards: List<CardMetadata> = listOf(
        card("major_0_fool", "0 Le Mat", 0,
            "A youthful traveler steps toward the cliff, accompanied by a loyal dog that nips at his heels.",
            "Nomadic unconscious energy that invites leaps of faith and embraces the unknown."),
        card("major_1_magician", "I Le Bateleur", 1,
            "A figure stands at a table with magical implements, one hand raised to the heavens and the other pointing earthward.",
            "The activated ego discovering its capacity to focus intention and translate imagination into form."),
        card("major_2_papess", "II La Papesse", 2,
            "A veiled priestess sits between dark and light pillars, holding a book half-concealed beneath her cloak.",
            "The receptive anima that gestates mystery and invites stillness so hidden knowledge can surface."),
        card("major_3_empress", "III L'Impératrice", 3,
            "A crowned woman rests in a lush garden, scepter in hand, a shield with an eagle at her side.",
            "Creative fecundity that reconciles opposites and nurtures emerging life into tangible reality."),
        card("major_4_emperor", "IIII L'Empereur", 4,
            "An armored ruler sits upon a stone cube, scepter and orb asserting dominion.",
            "Logos imposing order, structure, and stability; the impulse to secure and defend the known."),
        card("major_5_pope", "V Le Pape", 5,
            "A robed pontiff sits between two pillars, raising two fingers in blessing over kneeling figures.",
            "The inner authority that mediates between the personal and the transpersonal, bridging instinct and spirit."),
        card("major_6_lovers", "VI L'Amoureux", 6,
            "A young man stands between two women while a winged figure draws a bow overhead.",
            "The crisis of conscious choice that demands integration of opposites and commitment to one's own path."),
        card("major_7_chariot", "VII Le Chariot", 7,
            "A crowned warrior rides a canopied chariot drawn by two horses pulling in opposite directions.",
            "The ego harnessing opposing drives through will, directing psychic energy toward a chosen goal."),
        card("major_8_justice", "VIII La Justice", 8,
            "A crowned figure sits enthroned, sword raised in one hand and scales balanced in the other.",
            "The Self's impartial function that weighs, discerns, and restores equilibrium within the psyche."),
        card("major_9_hermit", "VIIII L'Hermite", 9,
            "A cloaked elder walks alone, lantern raised against the dark, staff in hand.",
            "The introversion that withdraws from collective noise to illuminate the inner path toward individuation."),
        card("major_10_wheel", "X La Roue de Fortune", 10,
            "A great wheel turns with figures rising and falling, crowned by a sphinx at its summit.",
            "The cyclical nature of psychic life; the invitation to find the still center within constant change."),
        card("major_11_strength", "XI La Force", 11,
            "A woman gently opens or closes the jaws of a lion with calm, unhurried hands.",
            "The soul's capacity to tame instinctual energy through love rather than force, uniting nature and spirit."),
        card("major_12_hanged_man", "XII Le Pendu", 12,
            "A figure hangs serenely by one foot from a wooden frame, the free leg crossed, face calm.",
            "Voluntary suspension that reverses ordinary perspective, allowing unconscious wisdom to rise into awareness."),
        card("major_13_death", "XIII (Sin Nombre)", 13,
            "A skeleton with a scythe moves across a field where heads and limbs of all ranks lie scattered.",
            "Radical transformation through the death of an outgrown identity, clearing ground for new psychic growth."),
        card("major_14_temperance", "XIIII Tempérance", 14,
            "A winged angel pours liquid between two cups in a continuous, unhurried flow.",
            "The transcendent function that blends opposites into a living third, mediating between conscious and unconscious."),
        card("major_15_devil", "XV Le Diable", 15,
            "A horned figure stands on a pedestal to which two smaller beings are loosely chained.",
            "The shadow's grip on unlived life; the compulsive energy that demands conscious confrontation and integration."),
        card("major_16_tower", "XVI La Maison Dieu", 16,
            "Lightning crowns a tower and casts figures earthward while flames erupt from the windows.",
            "A shock that fractures rigid constructs so psychic energy can flow again and illumination can enter."),
        card("major_17_star", "XVII L'Étoile", 17,
            "A nude figure kneels by a pool, pouring water from twin vessels beneath eight radiant stars.",
            "Active imagination guiding the psyche toward hope, authenticity, and gentle renewal."),
        card("major_18_moon", "XVIII La Lune", 18,
            "A full moon shines between two towers while a crayfish emerges from a pool and two dogs howl below.",
            "The nocturnal realm of illusion, instinct, and the unconscious depths that must be traversed with care."),
        card("major_19_sun", "XVIIII Le Soleil", 19,
            "A radiant sun shines over two children or a single child on horseback in an open garden.",
            "Consciousness illuminating the psyche; the joy of integration and the warmth of the Self made visible."),
        card("major_20_judgement", "XX Le Jugement", 20,
            "An angel blows a trumpet from the clouds while figures rise from coffins with arms outstretched.",
            "The call of the Self summoning buried aspects of the personality to rise and be consciously claimed."),
        card("major_21_world", "XXI Le Monde", 21,
            "A dancing figure wrapped in a laurel wreath is surrounded by the four evangelists at the corners.",
            "The completed cycle of individuation; the Self dancing freely at the center of a unified psyche.")
    )

    private fun card(id: String, name: String, number: Int, archetypalImage: String, psychologicalMeaning: String) =
        CardMetadata(
            card = TarotCard(id = id, arcana = ArcanaType.MAJOR, name = name, number = number),
            archetypalImage = archetypalImage,
            psychologicalMeaning = psychologicalMeaning
        )

    fun getCardMetadata(cardIds: List<String>): List<CardMetadata> {
        val catalog = majorCards.associateBy { it.card.id }
        return cardIds.mapNotNull { catalog[it] }
    }

    fun getAllCards(): List<TarotCard> = majorCards.map { it.card }
}
