package com.example.tarot.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarot.data.SpreadType
import com.example.tarot.data.TarotCard
import com.example.tarot.data.TarotRepository
import com.example.tarot.domain.ReadingEngine
import com.example.tarot.domain.ReadingRequest
import com.example.tarot.domain.TarotReading
import com.example.tarot.domain.interpretation.ReadingTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeEvent {
    data class ReadingReady(val reading: TarotReading) : HomeEvent()
    data class Error(val message: String) : HomeEvent()
}

data class HomeUiState(
    val question: String = "",
    val spreadType: SpreadType = SpreadType.THREE_CARD,
    val selectedCards: List<TarotCard> = emptyList(),
    val isLoading: Boolean = false,
    val event: HomeEvent? = null
) {
    val canRunReading: Boolean = question.isNotBlank() && selectedCards.size == spreadType.cardCount
}

class HomeViewModel(
    private val repository: TarotRepository = TarotRepository(),
    private val readingEngine: ReadingEngine = ReadingEngine(
        repository = repository,
        template = ReadingTemplate()
    )
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    private val catalog: List<TarotCard> by lazy { repository.getAllCards() }
    fun getCatalog(): List<TarotCard> = catalog

    fun onQuestionChange(value: String) {
        _state.value = _state.value.copy(question = value, event = null)
    }

    fun onSpreadSelected(spread: SpreadType) {
        val trimmedCards = _state.value.selectedCards.take(spread.cardCount)
        _state.value = _state.value.copy(
            spreadType = spread,
            selectedCards = trimmedCards,
            event = null
        )
    }

    fun onCardSelected(cardId: String) {
        val target = catalog.firstOrNull { it.id == cardId } ?: return
        val current = _state.value
        if (current.selectedCards.any { it.id == cardId }) return
        if (current.selectedCards.size >= current.spreadType.cardCount) return

        _state.value = current.copy(
            selectedCards = current.selectedCards + target,
            event = null
        )
    }

    fun onCardRemoved(cardId: String) {
        val current = _state.value
        _state.value = current.copy(
            selectedCards = current.selectedCards.filterNot { it.id == cardId },
            event = null
        )
    }

    fun onReadingConsumed() {
        _state.value = _state.value.copy(event = null)
    }

    fun runReading() {
        val snapshot = _state.value
        if (!snapshot.canRunReading || snapshot.isLoading) return

        _state.value = snapshot.copy(isLoading = true, event = null)
        viewModelScope.launch {
            val result = readingEngine.run(
                ReadingRequest(
                    question = snapshot.question,
                    spreadType = snapshot.spreadType,
                    cardIds = snapshot.selectedCards.map { it.id }
                )
            )
            val error = result.exceptionOrNull()
            val newState = if (error != null) {
                snapshot.copy(
                    isLoading = false,
                    event = HomeEvent.Error(error.message ?: "Se produjo un error")
                )
            } else {
                snapshot.copy(
                    isLoading = false,
                    event = HomeEvent.ReadingReady(result.getOrThrow())
                )
            }
            _state.value = newState
        }
    }

    fun loadSampleReading() {
        val sampleCards = listOf("major_4_emperor", "major_16_tower", "major_17_star")
        val selected = sampleCards.mapNotNull { id -> catalog.firstOrNull { it.id == id } }
        _state.value = _state.value.copy(
            question = "Siento que mi carrera profesional está estancada...",
            spreadType = SpreadType.THREE_CARD,
            selectedCards = selected,
            event = null
        )
    }
}
