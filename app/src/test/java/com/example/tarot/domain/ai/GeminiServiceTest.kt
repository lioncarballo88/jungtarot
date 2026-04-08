package com.example.tarot.domain.ai

import com.example.tarot.data.SpreadType
import com.example.tarot.data.AppLanguage
import com.example.tarot.domain.Card
import com.example.tarot.domain.ReadingSection
import com.example.tarot.domain.TarotReading
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class GeminiServiceTest {

    private val service = GeminiService(apiKey = "test-api-key")

    @Test
    fun `test generate reading`() = runBlocking {
        val reading = service.generateReading(
            question = "What is my purpose?",
            spreadType = SpreadType.THREE_CARD,
            selectedCards = listOf(
                Card("1", "The Fool", false, mockMetadata()),
                Card("2", "The Magician", true, mockMetadata()),
                Card("3", "The High Priestess", false, mockMetadata())
            ),
            language = AppLanguage.ENGLISH
        )

        assertNotNull(reading)
        assertNotNull(reading.psychologicalInterpretation)
        assertNotNull(reading.mirror)
        assertNotNull(reading.pathToIndividuation)
    }

    private fun mockMetadata(): CardMetadata {
        return CardMetadata("1", "The Fool", "Description", "Keywords")
    }
}