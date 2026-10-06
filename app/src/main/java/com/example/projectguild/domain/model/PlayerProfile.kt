package com.example.projectguild.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerProfile(
    val playerName: String = "Alex",
    val level: Int = 4,
    val xp: Int = 720,
    val xpForNextLevel: Int = 1000,
    val guildCoins: Int = 35,
    val screenTimeMinutes: Int = 45,
    val streakDays: Int = 4,
    val totalMathSolved: Int = 8,
    val totalMathCorrect: Int = 8,
    val completedQuestsCount: Int = 3
) {
    val progressFraction: Float
        get() = (xp.toFloat() / xpForNextLevel.toFloat()).coerceIn(0f, 1f)
}
