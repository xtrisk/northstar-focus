package com.ink.northstarfocus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Cedar,
    onPrimary = Color.White,
    primaryContainer = Seafoam,
    onPrimaryContainer = DeepInk,
    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = Apricot.copy(alpha = 0.36f),
    onSecondaryContainer = DeepInk,
    tertiary = Apricot,
    onTertiary = DeepInk,
    background = Cream,
    onBackground = DeepInk,
    surface = Porcelain,
    onSurface = DeepInk,
    outline = SandOutline
)

private val DarkColors = darkColorScheme(
    primary = Seafoam,
    onPrimary = DeepInk,
    primaryContainer = Cedar,
    onPrimaryContainer = Color.White,
    secondary = Apricot,
    onSecondary = DeepInk,
    secondaryContainer = NightCard,
    onSecondaryContainer = Color.White,
    tertiary = Coral,
    onTertiary = Color.White,
    background = NightTeal,
    onBackground = Color.White,
    surface = NightCard,
    onSurface = Color.White,
    outline = Mist
)

@Composable
fun NorthstarFocusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = NorthstarTypography,
        content = content
    )
}
