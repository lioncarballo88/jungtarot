package com.jungtarot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import com.jungtarot.app.ui.home.HomeEvent
import com.jungtarot.app.ui.home.HomeScreen
import com.jungtarot.app.ui.home.HomeViewModel
import com.jungtarot.app.ui.result.ResultScreen
import com.jungtarot.app.ui.theme.TarotTheme

private const val ROUTE_HOME = "home"
private const val ROUTE_RESULT = "result"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TarotTheme {
                val navController = rememberNavController()
                val viewModel: HomeViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                val currentReading = viewModel.currentReading.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(state.event) {
                    when (val event = state.event) {
                        is HomeEvent.Error -> snackbarHostState.showSnackbar(event.message)
                        is HomeEvent.ReadingReady -> navController.navigate(ROUTE_RESULT)
                        null -> Unit
                    }
                    if (state.event != null) viewModel.onReadingConsumed()
                }

                NavHost(navController = navController, startDestination = ROUTE_HOME) {
                    composable(ROUTE_HOME) {
                        HomeScreen(
                            state = state,
                            catalog = viewModel.catalog,
                            onSpreadSelected = viewModel::onSpreadSelected,
                            onCardSelected = viewModel::onCardSelected,
                            onCardRemoved = viewModel::onCardRemoved,
                            onRandomSelection = viewModel::onRandomSelection,
                            onRunReading = viewModel::runReading,
                            snackbarHostState = snackbarHostState
                        )
                    }
                    composable(ROUTE_RESULT) {
                        val reading = currentReading.value
                        if (reading != null) {
                            ResultScreen(
                                reading = reading,
                                onRestart = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
