package com.example.projectguild.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LevelCalculatorTest {

    @Test
    fun calculateNewProgress_xpBelowThreshold_doesNotLevelUp() {
        val result = LevelCalculator.calculateNewProgress(
            currentLevel = 4,
            currentXp = 720,
            gainedXp = 100,
            xpForLevel = 1000
        )

        assertEquals(4, result.newLevel)
        assertEquals(820, result.newXp)
        assertFalse(result.didLevelUp)
        assertEquals(0, result.levelsGained)
    }

    @Test
    fun calculateNewProgress_xpReachesThreshold_levelsUpCorrectly() {
        val result = LevelCalculator.calculateNewProgress(
            currentLevel = 4,
            currentXp = 720,
            gainedXp = 280,
            xpForLevel = 1000
        )

        assertEquals(5, result.newLevel)
        assertEquals(0, result.newXp)
        assertTrue(result.didLevelUp)
        assertEquals(1, result.levelsGained)
    }

    @Test
    fun calculateNewProgress_xpExceedsThreshold_levelsUpAndCarriesOverExcess() {
        val result = LevelCalculator.calculateNewProgress(
            currentLevel = 4,
            currentXp = 720,
            gainedXp = 350,
            xpForLevel = 1000
        )

        // 720 + 350 = 1070 -> Level 5 with 70 XP
        assertEquals(5, result.newLevel)
        assertEquals(70, result.newXp)
        assertTrue(result.didLevelUp)
        assertEquals(1, result.levelsGained)
    }

    @Test
    fun calculateNewProgress_largeXpGain_levelsUpMultipleTimes() {
        val result = LevelCalculator.calculateNewProgress(
            currentLevel = 4,
            currentXp = 720,
            gainedXp = 2500,
            xpForLevel = 1000
        )

        // 720 + 2500 = 3220 -> Level 4 + 3 = 7 with 220 XP
        assertEquals(7, result.newLevel)
        assertEquals(220, result.newXp)
        assertTrue(result.didLevelUp)
        assertEquals(3, result.levelsGained)
    }
}
