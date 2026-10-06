package com.example.projectguild.data.storage

import com.example.projectguild.domain.model.CoinTransaction
import com.example.projectguild.domain.model.PlayerProfile
import com.example.projectguild.domain.model.Quest
import com.example.projectguild.domain.model.QuestDifficulty
import com.example.projectguild.domain.model.QuestStatus
import com.example.projectguild.domain.model.ScreenTimeTransaction
import com.example.projectguild.domain.model.VerificationType
import com.example.projectguild.domain.model.XpTransaction
import kotlinx.serialization.Serializable

@Serializable
data class GuildSaveState(
    val playerProfile: PlayerProfile = defaultPlayerProfile(),
    val quests: List<Quest> = defaultQuests(),
    val screenTimeTransactions: List<ScreenTimeTransaction> = defaultScreenTimeTransactions(),
    val coinTransactions: List<CoinTransaction> = defaultCoinTransactions(),
    val xpTransactions: List<XpTransaction> = defaultXpTransactions()
) {
    companion object {
        fun defaultPlayerProfile(): PlayerProfile = PlayerProfile(
            playerName = "Alex",
            level = 4,
            xp = 720,
            xpForNextLevel = 1000,
            guildCoins = 35,
            screenTimeMinutes = 45,
            streakDays = 4,
            totalMathSolved = 8,
            totalMathCorrect = 8,
            completedQuestsCount = 3
        )

        fun defaultQuests(): List<Quest> = listOf(
            Quest(
                id = "quest-1",
                title = "Brush Your Teeth",
                iconEmoji = "🪥",
                description = "Brush your teeth thoroughly for 2 minutes.",
                objective = "Keep your adventurer smile bright and clean morning and night.",
                difficulty = QuestDifficulty.EASY,
                rewardCoins = 5,
                rewardXp = 5,
                verificationType = VerificationType.PARENT_CONFIRMATION,
                status = QuestStatus.AVAILABLE
            ),
            Quest(
                id = "quest-2",
                title = "Make Your Bed",
                iconEmoji = "🛏",
                description = "Make your bed and tidy your sleeping area.",
                objective = "Your sleeping area should be clean and organized.",
                difficulty = QuestDifficulty.NORMAL,
                rewardCoins = 10,
                rewardXp = 10,
                verificationType = VerificationType.PHOTO_PROOF,
                status = QuestStatus.AVAILABLE
            ),
            Quest(
                id = "quest-3",
                title = "Complete Homework",
                iconEmoji = "📚",
                description = "Finish all assigned school homework and review your daily notes.",
                objective = "Complete all assigned homework problems with care before playtime.",
                difficulty = QuestDifficulty.HARD,
                rewardCoins = 20,
                rewardXp = 20,
                verificationType = VerificationType.PHOTO_PROOF,
                status = QuestStatus.AVAILABLE
            ),
            Quest(
                id = "quest-4",
                title = "Help Clean the House",
                iconEmoji = "🧹",
                description = "Assist with sweeping, wiping the dining table, or putting toys away.",
                objective = "Help keep common living areas tidy for the whole family guild.",
                difficulty = QuestDifficulty.NORMAL,
                rewardCoins = 15,
                rewardXp = 15,
                verificationType = VerificationType.PARENT_CONFIRMATION,
                status = QuestStatus.AVAILABLE
            )
        )

        fun defaultScreenTimeTransactions(): List<ScreenTimeTransaction> = listOf(
            ScreenTimeTransaction("st-init-1", 20, "Math Challenge Morning", System.currentTimeMillis() - 7200000),
            ScreenTimeTransaction("st-init-2", 25, "Math Apprentice Bonus", System.currentTimeMillis() - 3600000)
        )

        fun defaultCoinTransactions(): List<CoinTransaction> = listOf(
            CoinTransaction("c-init-1", 10, "Room Guardian Quest", System.currentTimeMillis() - 86400000),
            CoinTransaction("c-init-2", 25, "Weekly Responsibility Bonus", System.currentTimeMillis() - 43200000)
        )

        fun defaultXpTransactions(): List<XpTransaction> = listOf(
            XpTransaction("xp-init-1", 120, "Daily Adventurer Quest Pack", System.currentTimeMillis() - 86400000),
            XpTransaction("xp-init-2", 100, "Math Training Streak", System.currentTimeMillis() - 43200000)
        )
    }
}
