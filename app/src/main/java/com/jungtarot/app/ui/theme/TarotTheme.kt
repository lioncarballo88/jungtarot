package com.jungtarot.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object TarotPalette {
    val accent = Color(0xFFF6C90E)
    val darkBackground = Color(0xFF050505)
    val darkSurface = Color(0xFF111111)
    val darkOnSurface = Color(0xFFF5F5F5)
    val muted = Color(0xFF7A7A7A)
}

private val darkScheme: ColorScheme = darkColorScheme(
    primary = TarotPalette.accent,
    onPrimary = Color.Black,
    background = TarotPalette.darkBackground,
    surface = TarotPalette.darkSurface,
    onSurface = TarotPalette.darkOnSurface,
    secondary = Color.White,
    onSecondary = Color.Black,
    tertiary = TarotPalette.accent,
    outline = TarotPalette.muted
)

private val lightScheme: ColorScheme = lightColorScheme(
    primary = TarotPalette.accent,
    onPrimary = Color.Black,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),
    secondary = Color.Black,
    onSecondary = Color.White,
    tertiary = TarotPalette.accent,
    outline = TarotPalette.muted
)

@Composable
fun TarotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkScheme else lightScheme,
        typography = TarotTypography,
        shapes = TarotShapes,
        content = content
    )
}
