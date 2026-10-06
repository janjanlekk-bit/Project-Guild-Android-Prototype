package com.example.projectguild.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.ui.graphics.vector.ImageVector

enum class GuildScreenDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME("home", "Home", Icons.Rounded.Home),
    GUILD("guild", "Guild", Icons.Rounded.Shield),
    MATH("math", "Training", Icons.Rounded.Calculate),
    PROFILE("profile", "Adventurer", Icons.Rounded.Person)
}
