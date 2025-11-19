package com.example.tarot.domain.interpretation

import com.example.tarot.data.CardMetadata
import com.example.tarot.data.SpreadType
import com.example.tarot.domain.ReadingSection
import com.example.tarot.domain.TarotReading

class ReadingTemplate {
    fun buildReading(
        question: String,
        spreadType: SpreadType,
        cards: List<CardMetadata>
    ): TarotReading {
        val imageSection = ReadingSection(
            title = "The Archetypal Image",
            body = cards.joinToString(separator = "\n") { metadata ->
                "${metadata.card.name}: ${metadata.archetypalImage}"
            }
        )

        val psycheSection = ReadingSection(
            title = "Psychological Interpretation",
            body = buildPsycheNarrative(spreadType, cards)
        )

        val mirrorSection = ReadingSection(
            title = "The Mirror",
            body = "Your psychic weather reflects your question: \"$question\". " +
                when (spreadType) {
                    SpreadType.ONE_CARD -> buildSingleMirror(cards)
                    SpreadType.TWO_CARD -> buildDualMirror(cards)
                    SpreadType.THREE_CARD -> buildTripleMirror(cards)
                }
        )

        val pathSection = ReadingSection(
            title = "Path to Individuation",
            body = buildPathNarrative(cards)
        )

        return TarotReading(
            archetypalImage = imageSection,
            psychologicalInterpretation = psycheSection,
            mirror = mirrorSection,
            pathToIndividuation = pathSection,
            cardDetails = cards
        )
    }

    private fun buildPsycheNarrative(
        spreadType: SpreadType,
        cards: List<CardMetadata>
    ): String {
        return when (spreadType) {
            SpreadType.ONE_CARD -> {
                val card = cards.first()
                "${card.card.name} constellates now, inviting you to explore ${card.psychologicalMeaning}."
            }
            SpreadType.TWO_CARD -> {
                val ego = cards.getOrNull(0)
                val shadow = cards.getOrNull(1)
                if (ego == null || shadow == null) return "The psyche is presenting missing pieces."
                "${ego.card.name} describes the conscious stance while ${shadow.card.name} answers as the compensating force. ${shadow.psychologicalMeaning} balances the ego's ${ego.psychologicalMeaning}."
            }
            SpreadType.THREE_CARD -> {
                val thesis = cards.getOrNull(0)
                val antithesis = cards.getOrNull(1)
                val synthesis = cards.getOrNull(2)
                if (thesis == null || antithesis == null || synthesis == null) {
                    return "The process cannot unfold without all three stages."
                }
                "${thesis.card.name} forms the thesis of your process, ${antithesis.card.name} holds the present conflict, and ${synthesis.card.name} channels the energy forward toward ${synthesis.psychologicalMeaning}."
            }
        }
    }

    private fun buildSingleMirror(cards: List<CardMetadata>): String {
        val card = cards.first()
        return "Energy pools around ${card.card.name}, emphasizing ${card.psychologicalMeaning}."
    }

    private fun buildDualMirror(cards: List<CardMetadata>): String {
        val ego = cards.getOrNull(0)
        val shadow = cards.getOrNull(1)
        if (ego == null || shadow == null) return "The wind shifts as the spreads await completion."
        return "High pressure gathers where ${ego.card.name} stands while ${shadow.card.name} blows in as the counterwind."
    }

    private fun buildTripleMirror(cards: List<CardMetadata>): String {
        val thesis = cards.getOrNull(0)
        val antithesis = cards.getOrNull(1)
        val synthesis = cards.getOrNull(2)
        if (thesis == null || antithesis == null || synthesis == null) {
            return "Storm fronts form but the pattern is incomplete."
        }
        return "Your weather map shifts from ${thesis.card.name}'s settled air through ${antithesis.card.name}'s storm into the clearing skies promised by ${synthesis.card.name}."
    }

    private fun buildPathNarrative(cards: List<CardMetadata>): String {
        val prompts = cards.map { metadata ->
            "Consider how ${metadata.card.name.lowercase()} asks you to relate to ${metadata.psychologicalMeaning.lowercase()}"
        }
        return prompts.joinToString(separator = "\n")
    }
}
