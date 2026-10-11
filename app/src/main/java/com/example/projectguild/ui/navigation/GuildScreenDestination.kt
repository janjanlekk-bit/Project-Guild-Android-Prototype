package com.example.projectguild.ui.navigation

enum class GuildScreenDestination(
    val route: String,
    val label: String
) {
    PORTAL("portal", "Main Portal"),
    SCREEN_TIME("screen_time", "Screen Time HQ"),
    COINS("coins", "Guild Coins HQ"),
    MATH("math", "Math Arena"),
    PROFILE("profile", "Adventurer Profile"),
    APP_LOCKER("app_locker", "App Locker")
}

