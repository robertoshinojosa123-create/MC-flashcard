package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MinecraftColorScheme = darkColorScheme(
    primary = McGold,
    onPrimary = McStoneShadow,
    primaryContainer = McStoneDark,
    onPrimaryContainer = McGold,
    secondary = McEmerald,
    onSecondary = McStoneShadow,
    tertiary = McDiamond,
    onTertiary = McStoneShadow,
    background = McDarkBg,
    onBackground = McTextPrimary,
    surface = McStoneDark,
    onSurface = McTextPrimary,
    error = McRedTnt,
    onError = McTextPrimary
)

@Composable
fun MinecraftFlashcardsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MinecraftColorScheme,
        typography = Typography,
        content = content
    )
}
