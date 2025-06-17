package com.cloudflare.anthropic_sample.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CloudflareDarkColorScheme =
  darkColorScheme(
    primary = Orange,
    onPrimary = Black,
    secondary = DarkGray,
    onSecondary = White,
    tertiary = LightGray,
    onTertiary = Black,
    background = Black,
    onBackground = White,
    surface = DarkBlue,
    onSurface = White,
    surfaceVariant = DarkGray,
    onSurfaceVariant = LightGray,
  )

@Composable
fun AnthropicSampleTheme(content: @Composable () -> Unit) {
  MaterialTheme(colorScheme = CloudflareDarkColorScheme, typography = Typography, content = content)
}
