package com.example.tarot.domain

import com.example.tarot.data.SpreadType
import com.example.tarot.data.TarotRepository
import com.example.tarot.domain.interpretation.ReadingTemplate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadingEngineTest {

    private val mockRepository = TarotRepository() // Replace with a mock implementation if needed
    private val mockTemplate = ReadingTemplate() // Replace with a mock implementation if needed
    private val validator = ReadingValidator()
    private val engine = ReadingEngine(mockRepository, mockTemplate, validator)

    @Test
    fun `test valid reading request`() {
        val request = ReadingRequest(
            question = "What does the future hold?",
            spreadType = SpreadType.THREE_CARD,
            selectedCards = listOf(
                Card("1", "The Fool", false, mockMetadata()),
                Card("2", "The Magician", true, mockMetadata()),
                Card("3", "The High Priestess", false, mockMetadata())
            )
        )

        val result = engine.run(request)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `test invalid spread size`() {
        val request = ReadingRequest(
            question = "What does the future hold?",
            spreadType = SpreadType.THREE_CARD,
            selectedCards = listOf(
                Card("1", "The Fool", false, mockMetadata())
            )
        )

        val result = engine.run(request)
        assertTrue(result.isFailure)
        assertEquals(result.exceptionOrNull() is ReadingError.InvalidSpreadSize, true)
    }

    private fun mockMetadata(): CardMetadata {
        return CardMetadata("1", "The Fool", "Description", "Keywords")
    }
}