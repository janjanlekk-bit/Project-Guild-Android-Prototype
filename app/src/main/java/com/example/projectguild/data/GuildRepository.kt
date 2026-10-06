package com.example.projectguild.data

import com.example.projectguild.data.storage.GuildSaveState
import com.example.projectguild.data.storage.GuildStorage
import com.example.projectguild.domain.LevelCalculator
import com.example.projectguild.domain.LevelUpResult
import com.example.projectguild.domain.model.Achievement
import com.example.projectguild.domain.model.CoinTransaction
import com.example.projectguild.domain.model.PlayerProfile
import com.example.projectguild.domain.model.Quest
import com.example.projectguild.domain.model.QuestStatus
import com.example.projectguild.domain.model.ScreenTimeTransaction
import com.example.projectguild.domain.model.XpTransaction
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class QuestApprovalResult(
    val quest: Quest,
    val coinsEarned: Int,
    val xpEarned: Int,
    val levelUpResult: LevelUpResult
)

interface GuildRepository {
    val playerProfile: StateFlow<PlayerProfile>
    val quests: StateFlow<List<Quest>>
    val screenTimeTransactions: StateFlow<List<ScreenTimeTransaction>>
    val coinTransactions: StateFlow<List<CoinTransaction>>
    val xpTransactions: StateFlow<List<XpTransaction>>

    suspend fun acceptQuest(questId: String): Boolean
    suspend fun submitQuestProof(questId: String, proofNote: String = "📸 Photo verification attached"): Boolean
    suspend fun approveQuest(questId: String): QuestApprovalResult?
    suspend fun rejectQuest(questId: String, reason: String = "Please complete the task more carefully."): Boolean
    suspend fun retryQuest(questId: String): Boolean

    suspend fun recordMathAnswer(isCorrect: Boolean, screenTimeRewardMinutes: Int = 5, xpReward: Int = 5): LevelUpResult
    fun getQuestById(questId: String): Quest?
    fun getAchievements(): List<Achievement>
    suspend fun resetToDefaultSampleData()
}

class DefaultGuildRepository(
    private val storage: GuildStorage
) : GuildRepository {

    private val mutex = Mutex()

    private val _playerProfile = MutableStateFlow(PlayerProfile())
    override val playerProfile: StateFlow<PlayerProfile> = _playerProfile.asStateFlow()

    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    override val quests: StateFlow<List<Quest>> = _quests.asStateFlow()

    private val _screenTimeTransactions = MutableStateFlow<List<ScreenTimeTransaction>>(emptyList())
    override val screenTimeTransactions: StateFlow<List<ScreenTimeTransaction>> = _screenTimeTransactions.asStateFlow()

    private val _coinTransactions = MutableStateFlow<List<CoinTransaction>>(emptyList())
    override val coinTransactions: StateFlow<List<CoinTransaction>> = _coinTransactions.asStateFlow()

    private val _xpTransactions = MutableStateFlow<List<XpTransaction>>(emptyList())
    override val xpTransactions: StateFlow<List<XpTransaction>> = _xpTransactions.asStateFlow()

    init {
        loadFromStorage()
    }

    private fun loadFromStorage() {
        val state = storage.loadState()
        _playerProfile.value = state.playerProfile
        _quests.value = state.quests
        _screenTimeTransactions.value = state.screenTimeTransactions
        _coinTransactions.value = state.coinTransactions
        _xpTransactions.value = state.xpTransactions
    }

    private fun persistCurrentState() {
        storage.saveState(
            GuildSaveState(
                playerProfile = _playerProfile.value,
                quests = _quests.value,
                screenTimeTransactions = _screenTimeTransactions.value,
                coinTransactions = _coinTransactions.value,
                xpTransactions = _xpTransactions.value
            )
        )
    }

    override suspend fun acceptQuest(questId: String): Boolean = mutex.withLock {
        val currentList = _quests.value
        val quest = currentList.find { it.id == questId } ?: return false
        if (quest.status != QuestStatus.AVAILABLE) return false

        val updated = currentList.map {
            if (it.id == questId) it.copy(status = QuestStatus.ACTIVE) else it
        }
        _quests.value = updated
        persistCurrentState()
        true
    }

    override suspend fun submitQuestProof(questId: String, proofNote: String): Boolean = mutex.withLock {
        val currentList = _quests.value
        val quest = currentList.find { it.id == questId } ?: return false
        if (quest.status != QuestStatus.ACTIVE && quest.status != QuestStatus.REJECTED) return false

        val updated = currentList.map {
            if (it.id == questId) {
                it.copy(
                    status = QuestStatus.SUBMITTED,
                    rejectionReason = null,
                    mockProofImageNote = proofNote
                )
            } else it
        }
        _quests.value = updated
        persistCurrentState()
        true
    }

    override suspend fun approveQuest(questId: String): QuestApprovalResult? = mutex.withLock {
        val currentList = _quests.value
        val quest = currentList.find { it.id == questId } ?: return null
        if (quest.status != QuestStatus.SUBMITTED && quest.status != QuestStatus.ACTIVE) return null

        val updatedQuests = currentList.map {
            if (it.id == questId) it.copy(status = QuestStatus.APPROVED, rejectionReason = null) else it
        }
        _quests.value = updatedQuests

        // Calculate XP and level progression
        val currentProfile = _playerProfile.value
        val levelResult = LevelCalculator.calculateNewProgress(
            currentLevel = currentProfile.level,
            currentXp = currentProfile.xp,
            gainedXp = quest.rewardXp,
            xpForLevel = currentProfile.xpForNextLevel
        )

        // Ledger transactions
        val coinTx = CoinTransaction(
            id = UUID.randomUUID().toString(),
            amount = quest.rewardCoins,
            reason = "Completed Quest: ${quest.title}"
        )
        val xpTx = XpTransaction(
            id = UUID.randomUUID().toString(),
            amount = quest.rewardXp,
            reason = "Completed Quest: ${quest.title}"
        )

        _coinTransactions.value = listOf(coinTx) + _coinTransactions.value
        _xpTransactions.value = listOf(xpTx) + _xpTransactions.value

        _playerProfile.value = currentProfile.copy(
            level = levelResult.newLevel,
            xp = levelResult.newXp,
            xpForNextLevel = levelResult.xpForNextLevel,
            guildCoins = currentProfile.guildCoins + quest.rewardCoins,
            completedQuestsCount = currentProfile.completedQuestsCount + 1
        )

        persistCurrentState()

        return QuestApprovalResult(
            quest = quest.copy(status = QuestStatus.APPROVED),
            coinsEarned = quest.rewardCoins,
            xpEarned = quest.rewardXp,
            levelUpResult = levelResult
        )
    }

    override suspend fun rejectQuest(questId: String, reason: String): Boolean = mutex.withLock {
        val currentList = _quests.value
        val quest = currentList.find { it.id == questId } ?: return false
        if (quest.status != QuestStatus.SUBMITTED && quest.status != QuestStatus.ACTIVE) return false

        val updated = currentList.map {
            if (it.id == questId) {
                it.copy(
                    status = QuestStatus.REJECTED,
                    rejectionReason = reason
                )
            } else it
        }
        _quests.value = updated
        persistCurrentState()
        true
    }

    override suspend fun retryQuest(questId: String): Boolean = mutex.withLock {
        val currentList = _quests.value
        val quest = currentList.find { it.id == questId } ?: return false
        if (quest.status != QuestStatus.REJECTED) return false

        val updated = currentList.map {
            if (it.id == questId) {
                it.copy(
                    status = QuestStatus.ACTIVE,
                    rejectionReason = null
                )
            } else it
        }
        _quests.value = updated
        persistCurrentState()
        true
    }

    override suspend fun recordMathAnswer(
        isCorrect: Boolean,
        screenTimeRewardMinutes: Int,
        xpReward: Int
    ): LevelUpResult = mutex.withLock {
        val current = _playerProfile.value

        if (isCorrect) {
            val levelResult = LevelCalculator.calculateNewProgress(
                currentLevel = current.level,
                currentXp = current.xp,
                gainedXp = xpReward,
                xpForLevel = current.xpForNextLevel
            )

            val stTx = ScreenTimeTransaction(
                id = UUID.randomUUID().toString(),
                amount = screenTimeRewardMinutes,
                reason = "Math Training Correct Answer"
            )
            val xpTx = XpTransaction(
                id = UUID.randomUUID().toString(),
                amount = xpReward,
                reason = "Math Training Problem Solved"
            )

            _screenTimeTransactions.value = listOf(stTx) + _screenTimeTransactions.value
            _xpTransactions.value = listOf(xpTx) + _xpTransactions.value

            _playerProfile.value = current.copy(
                level = levelResult.newLevel,
                xp = levelResult.newXp,
                xpForNextLevel = levelResult.xpForNextLevel,
                screenTimeMinutes = current.screenTimeMinutes + screenTimeRewardMinutes,
                totalMathSolved = current.totalMathSolved + 1,
                totalMathCorrect = current.totalMathCorrect + 1
            )
            persistCurrentState()
            levelResult
        } else {
            // No screen time lost for incorrect answers!
            _playerProfile.value = current.copy(
                totalMathSolved = current.totalMathSolved + 1
            )
            persistCurrentState()
            LevelUpResult(
                newLevel = current.level,
                newXp = current.xp,
                xpForNextLevel = current.xpForNextLevel,
                didLevelUp = false,
                levelsGained = 0
            )
        }
    }

    override fun getQuestById(questId: String): Quest? {
        return _quests.value.find { it.id == questId }
    }

    override fun getAchievements(): List<Achievement> {
        val profile = _playerProfile.value
        return listOf(
            Achievement(
                id = "ach-1",
                title = "First Quest",
                description = "Completed first real-world mission",
                iconEmoji = "🏆",
                currentProgress = profile.completedQuestsCount.coerceAtMost(1),
                targetProgress = 1,
                isUnlocked = profile.completedQuestsCount >= 1
            ),
            Achievement(
                id = "ach-2",
                title = "Math Apprentice",
                description = "Solve 25 math challenges",
                iconEmoji = "🧠",
                currentProgress = profile.totalMathSolved.coerceAtMost(25),
                targetProgress = 25,
                isUnlocked = profile.totalMathSolved >= 25
            ),
            Achievement(
                id = "ach-3",
                title = "Helpful Adventurer",
                description = "Complete 10 guild quests",
                iconEmoji = "⚔️",
                currentProgress = profile.completedQuestsCount.coerceAtMost(10),
                targetProgress = 10,
                isUnlocked = profile.completedQuestsCount >= 10
            ),
            Achievement(
                id = "ach-4",
                title = "Streak Champion",
                description = "Maintain a 7-day learning streak",
                iconEmoji = "🔥",
                currentProgress = profile.streakDays.coerceAtMost(7),
                targetProgress = 7,
                isUnlocked = profile.streakDays >= 7
            )
        )
    }

    override suspend fun resetToDefaultSampleData(): Unit = mutex.withLock {
        val fresh = GuildSaveState()
        _playerProfile.value = fresh.playerProfile
        _quests.value = fresh.quests
        _screenTimeTransactions.value = fresh.screenTimeTransactions
        _coinTransactions.value = fresh.coinTransactions
        _xpTransactions.value = fresh.xpTransactions
        storage.saveState(fresh)
    }
}
