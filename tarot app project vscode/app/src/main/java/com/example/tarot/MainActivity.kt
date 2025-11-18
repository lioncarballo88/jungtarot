package com.example.tarot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tarot.domain.TarotReading
import com.example.tarot.ui.home.HomeEvent
import com.example.tarot.ui.home.HomeScreen
import com.example.tarot.ui.home.HomeViewModel
import com.example.tarot.ui.result.ResultScreen
import com.example.tarot.ui.theme.TarotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TarotTheme {
                val viewModel: HomeViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                var currentReading by remember { mutableStateOf<TarotReading?>(null) }

                LaunchedEffect(state.event) {
                    when (val event = state.event) {
                        is HomeEvent.Error -> snackbarHostState.showSnackbar(event.message)
                        is HomeEvent.ReadingReady -> currentReading = event.reading
                        null -> Unit
                    }
                    if (state.event != null) viewModel.onReadingConsumed()
                }

                if (currentReading == null) {
                    HomeScreen(
                        state = state,
                        catalog = viewModel.getCatalog(),
                        onQuestionChange = viewModel::onQuestionChange,
                        onSpreadSelected = viewModel::onSpreadSelected,
                        onCardSelected = viewModel::onCardSelected,
                        onCardRemoved = viewModel::onCardRemoved,
                        onRunReading = viewModel::runReading,
                        onLoadSample = viewModel::loadSampleReading,
                        snackbarHostState = snackbarHostState
                    )
                } else {
                    ResultScreen(reading = currentReading!!, onRestart = { currentReading = null })
                }
            }
        }
    }
}
