package com.example.projectguild.domain

data class LevelUpResult(
    val newLevel: Int,
    val newXp: Int,
    val xpForNextLevel: Int,
    val didLevelUp: Boolean,
    val levelsGained: Int
)

object LevelCalculator {
    const val DEFAULT_XP_PER_LEVEL = 1000

    fun calculateNewProgress(
        currentLevel: Int,
        currentXp: Int,
        gainedXp: Int,
        xpForLevel: Int = DEFAULT_XP_PER_LEVEL
    ): LevelUpResult {
        require(gainedXp >= 0) { "Gained XP must be non-negative" }
        val totalXp = currentXp + gainedXp
        val levelsGained = totalXp / xpForLevel
        val remainingXp = totalXp % xpForLevel
        val newLevel = currentLevel + levelsGained

        return LevelUpResult(
            newLevel = newLevel,
            newXp = remainingXp,
            xpForNextLevel = xpForLevel,
            didLevelUp = levelsGained > 0,
            levelsGained = levelsGained
        )
    }
}
