package com.example.projectguild.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GuildColorScheme = lightColorScheme(
    primary = GuildPrimary,
    onPrimary = Color.White,
    primaryContainer = GuildPrimaryContainer,
    onPrimaryContainer = GuildOnPrimaryContainer,
    secondary = GuildGold,
    onSecondary = Color.Black,
    secondaryContainer = GuildGoldContainer,
    onSecondaryContainer = GuildOnGoldContainer,
    tertiary = GuildSuccess,
    onTertiary = Color.White,
    tertiaryContainer = GuildSuccessContainer,
    onTertiaryContainer = GuildOnSuccessContainer,
    background = GuildBackground,
    onBackground = GuildOnSurface,
    surface = GuildSurface,
    onSurface = GuildOnSurface,
    surfaceVariant = GuildSurfaceVariant,
    onSurfaceVariant = GuildOnSurfaceVariant,
    error = GuildDanger,
    onError = Color.White,
    errorContainer = GuildDangerContainer,
    onErrorContainer = GuildDanger
)

@Composable
fun ProjectGuildTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GuildColorScheme,
        typography = Typography,
        content = content
    )
}
