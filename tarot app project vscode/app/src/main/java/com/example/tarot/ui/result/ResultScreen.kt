package com.example.tarot.ui.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tarot.data.CardMetadata
import com.example.tarot.domain.ReadingSection
import com.example.tarot.domain.TarotReading
import com.example.tarot.ui.theme.TarotPalette

@Composable
fun ResultScreen(reading: TarotReading, onRestart: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TarotPalette.background,
        floatingActionButton = {
            FloatingActionButton(onClick = onRestart, containerColor = TarotPalette.accent) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Nueva lectura", tint = TarotPalette.onSurface)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ReadingSectionCard(reading.archetypalImage)
            ReadingSectionCard(reading.psychologicalInterpretation)
            ReadingSectionCard(reading.mirror)
            ReadingSectionCard(reading.pathToIndividuation)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Cartas incluidas", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TarotPalette.accent)
            CardDetailsList(details = reading.cardDetails)
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ReadingSectionCard(section: ReadingSection) {
    Surface(
        tonalElevation = 6.dp,
        shape = MaterialTheme.shapes.large,
        color = TarotPalette.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(section.title, style = MaterialTheme.typography.titleMedium, color = TarotPalette.accent, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(section.body, style = MaterialTheme.typography.bodyLarge, color = TarotPalette.onSurface)
        }
    }
}

@Composable
private fun CardDetailsList(details: List<CardMetadata>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        details.forEach { metadata ->
            Surface(
                tonalElevation = 4.dp,
                color = TarotPalette.surface,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(metadata.card.name, style = MaterialTheme.typography.bodyLarge, color = TarotPalette.accent)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(metadata.archetypalImage, style = MaterialTheme.typography.bodyMedium, color = TarotPalette.onSurface)
                }
            }
        }
    }
}
