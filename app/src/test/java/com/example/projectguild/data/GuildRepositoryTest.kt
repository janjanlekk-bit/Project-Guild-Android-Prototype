package com.example.projectguild.data

import com.example.projectguild.data.storage.GuildSaveState
import com.example.projectguild.data.storage.InMemoryGuildStorage
import com.example.projectguild.domain.model.QuestStatus
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GuildRepositoryTest {

    private lateinit var storage: InMemoryGuildStorage
    private lateinit var repository: DefaultGuildRepository

    @Before
    fun setup() {
        storage = InMemoryGuildStorage(GuildSaveState())
        repository = DefaultGuildRepository(storage)
    }

    @Test
    fun correctMathAnswer_rewardsScreenTimeAndXp() = runTest {
        val initialProfile = repository.playerProfile.value
        val initialScreenTime = initialProfile.screenTimeMinutes
        val initialXp = initialProfile.xp
        val initialSolved = initialProfile.totalMathSolved
        val initialCorrect = initialProfile.totalMathCorrect

        val levelResult = repository.recordMathAnswer(
            isCorrect = true,
            screenTimeRewardMinutes = 5,
            xpReward = 5
        )

        val updatedProfile = repository.playerProfile.value
        assertEquals("Screen time should increase by 5", initialScreenTime + 5, updatedProfile.screenTimeMinutes)
        assertEquals("XP should increase by 5", initialXp + 5, updatedProfile.xp)
        assertEquals("Solved count should increase by 1", initialSolved + 1, updatedProfile.totalMathSolved)
        assertEquals("Correct count should increase by 1", initialCorrect + 1, updatedProfile.totalMathCorrect)

        // Verify ledger transactions
        assertEquals(1, repository.screenTimeTransactions.value.size - GuildSaveState.defaultScreenTimeTransactions().size)
        assertEquals(5, repository.screenTimeTransactions.value.first().amount)
        assertEquals(1, repository.xpTransactions.value.size - GuildSaveState.defaultXpTransactions().size)
        assertEquals(5, repository.xpTransactions.value.first().amount)
    }

    @Test
    fun incorrectMathAnswer_doesNotRewardScreenTimeOrXp() = runTest {
        val initialProfile = repository.playerProfile.value
        val initialScreenTime = initialProfile.screenTimeMinutes
        val initialXp = initialProfile.xp
        val initialSolved = initialProfile.totalMathSolved
        val initialCorrect = initialProfile.totalMathCorrect

        val levelResult = repository.recordMathAnswer(
            isCorrect = false,
            screenTimeRewardMinutes = 5,
            xpReward = 5
        )

        val updatedProfile = repository.playerProfile.value
        assertEquals("Screen time must not change on wrong answer", initialScreenTime, updatedProfile.screenTimeMinutes)
        assertEquals("XP must not change on wrong answer", initialXp, updatedProfile.xp)
        assertEquals("Solved count increases by 1", initialSolved + 1, updatedProfile.totalMathSolved)
        assertEquals("Correct count remains the same", initialCorrect, updatedProfile.totalMathCorrect)
        assertFalse("Did not level up", levelResult.didLevelUp)
    }

    @Test
    fun missionApproval_awardsCoinsAndXp() = runTest {
        val quest = repository.quests.value.first()
        val initialCoins = repository.playerProfile.value.guildCoins
        val initialXp = repository.playerProfile.value.xp
        val initialCompletedCount = repository.playerProfile.value.completedQuestsCount

        // Flow: Available -> Active -> Submitted -> Approved
        repository.acceptQuest(quest.id)
        assertEquals(QuestStatus.ACTIVE, repository.getQuestById(quest.id)?.status)

        repository.submitQuestProof(quest.id, "Mock Photo")
        assertEquals(QuestStatus.SUBMITTED, repository.getQuestById(quest.id)?.status)

        val result = repository.approveQuest(quest.id)
        assertNotNull(result)
        assertEquals(QuestStatus.APPROVED, repository.getQuestById(quest.id)?.status)

        val updatedProfile = repository.playerProfile.value
        assertEquals("Coins increase by quest reward", initialCoins + quest.rewardCoins, updatedProfile.guildCoins)
        assertEquals("XP increases by quest reward", initialXp + quest.rewardXp, updatedProfile.xp)
        assertEquals("Completed quest count increases by 1", initialCompletedCount + 1, updatedProfile.completedQuestsCount)

        // Verify ledger transactions
        assertEquals(quest.rewardCoins, repository.coinTransactions.value.first().amount)
        assertEquals(quest.rewardXp, repository.xpTransactions.value.first().amount)
    }

    @Test
    fun missionRejection_doesNotAwardCoinsOrXp() = runTest {
        val quest = repository.quests.value.first()
        val initialCoins = repository.playerProfile.value.guildCoins
        val initialXp = repository.playerProfile.value.xp
        val initialCompletedCount = repository.playerProfile.value.completedQuestsCount

        repository.acceptQuest(quest.id)
        repository.submitQuestProof(quest.id)

        val rejected = repository.rejectQuest(quest.id, "Please complete the task more carefully.")
        assertTrue(rejected)

        val updatedQuest = repository.getQuestById(quest.id)
        assertEquals(QuestStatus.REJECTED, updatedQuest?.status)
        assertEquals("Please complete the task more carefully.", updatedQuest?.rejectionReason)

        val updatedProfile = repository.playerProfile.value
        assertEquals("Coins must remain unchanged", initialCoins, updatedProfile.guildCoins)
        assertEquals("XP must remain unchanged", initialXp, updatedProfile.xp)
        assertEquals("Completed count unchanged", initialCompletedCount, updatedProfile.completedQuestsCount)
    }

    @Test
    fun missionRetry_resetsFromRejectedToActive() = runTest {
        val quest = repository.quests.value.first()
        repository.acceptQuest(quest.id)
        repository.submitQuestProof(quest.id)
        repository.rejectQuest(quest.id, "Try again")

        val retried = repository.retryQuest(quest.id)
        assertTrue(retried)

        val updatedQuest = repository.getQuestById(quest.id)
        assertEquals(QuestStatus.ACTIVE, updatedQuest?.status)
        assertEquals(null, updatedQuest?.rejectionReason)
    }

    @Test
    fun statePersistence_survivesAppRestart() = runTest {
        // Perform mutations on repository
        repository.recordMathAnswer(isCorrect = true, screenTimeRewardMinutes = 15, xpReward = 10)
        val quest = repository.quests.value[1]
        repository.acceptQuest(quest.id)
        repository.submitQuestProof(quest.id)
        repository.approveQuest(quest.id)

        val savedProfile = repository.playerProfile.value

        // Simulate app restart by creating a new repository with the same storage
        val newRepository = DefaultGuildRepository(storage)

        assertEquals(savedProfile.screenTimeMinutes, newRepository.playerProfile.value.screenTimeMinutes)
        assertEquals(savedProfile.guildCoins, newRepository.playerProfile.value.guildCoins)
        assertEquals(savedProfile.xp, newRepository.playerProfile.value.xp)
        assertEquals(savedProfile.completedQuestsCount, newRepository.playerProfile.value.completedQuestsCount)
        assertEquals(QuestStatus.APPROVED, newRepository.getQuestById(quest.id)?.status)
    }
}
