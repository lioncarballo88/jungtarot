package com.example.tarot.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object TarotPalette {
    val background = Color(0xFF050505)
    val surface = Color(0xFF111111)
    val onSurface = Color(0xFFF5F5F5)
    val accent = Color(0xFFF6C90E)
    val muted = Color(0xFF7A7A7A)
}

private val darkScheme: ColorScheme = darkColorScheme(
    primary = TarotPalette.accent,
    onPrimary = Color.Black,
    background = TarotPalette.background,
    surface = TarotPalette.surface,
    onSurface = TarotPalette.onSurface,
    secondary = Color.White,
    onSecondary = Color.Black,
    tertiary = TarotPalette.accent
)

private val lightScheme: ColorScheme = lightColorScheme(
    primary = TarotPalette.accent,
    onPrimary = Color.Black,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),
    secondary = Color.Black,
    onSecondary = Color.White,
    tertiary = TarotPalette.accent
)

@Composable
fun TarotTheme(content: @Composable () -> Unit) {
    val useDark = isSystemInDarkTheme()
    val palette = if (useDark) darkScheme else lightScheme
    MaterialTheme(
        colorScheme = palette,
        typography = TarotTypography,
        shapes = TarotShapes,
        content = content
    )
}
