package com.example.projectguild.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectguild.data.GuildRepository
import com.example.projectguild.domain.model.Achievement
import com.example.projectguild.domain.model.CoinTransaction
import com.example.projectguild.domain.model.PlayerProfile
import com.example.projectguild.domain.model.Quest
import com.example.projectguild.domain.model.QuestStatus
import com.example.projectguild.domain.model.ScreenTimeTransaction
import com.example.projectguild.domain.model.XpTransaction
import com.example.projectguild.ui.components.RewardCelebration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GuildViewModel(
    private val repository: GuildRepository
) : ViewModel() {

    val playerProfile: StateFlow<PlayerProfile> = repository.playerProfile

    val quests: StateFlow<List<Quest>> = repository.quests

    val screenTimeTransactions: StateFlow<List<ScreenTimeTransaction>> = repository.screenTimeTransactions
    val coinTransactions: StateFlow<List<CoinTransaction>> = repository.coinTransactions
    val xpTransactions: StateFlow<List<XpTransaction>> = repository.xpTransactions

    val activeQuests: StateFlow<List<Quest>> = quests.map { list ->
        list.filter { it.status == QuestStatus.ACTIVE || it.status == QuestStatus.SUBMITTED || it.status == QuestStatus.REJECTED }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableQuests: StateFlow<List<Quest>> = quests.map { list ->
        list.filter { it.status == QuestStatus.AVAILABLE }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedQuests: StateFlow<List<Quest>> = quests.map { list ->
        list.filter { it.status == QuestStatus.APPROVED }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedQuestForDetail = MutableStateFlow<Quest?>(null)
    val selectedQuestForDetail: StateFlow<Quest?> = _selectedQuestForDetail.asStateFlow()

    private val _rewardCelebration = MutableStateFlow<RewardCelebration?>(null)
    val rewardCelebration: StateFlow<RewardCelebration?> = _rewardCelebration.asStateFlow()

    fun selectQuest(questId: String) {
        val quest = repository.getQuestById(questId)
        _selectedQuestForDetail.value = quest
    }

    fun dismissQuestDetail() {
        _selectedQuestForDetail.value = null
    }

    fun dismissCelebration() {
        _rewardCelebration.value = null
    }

    fun acceptQuest(questId: String) {
        viewModelScope.launch {
            repository.acceptQuest(questId)
            _selectedQuestForDetail.value = repository.getQuestById(questId)
        }
    }

    fun submitQuestProof(questId: String) {
        viewModelScope.launch {
            repository.submitQuestProof(questId)
            _selectedQuestForDetail.value = repository.getQuestById(questId)
        }
    }

    fun simulateParentApproval(questId: String) {
        viewModelScope.launch {
            val result = repository.approveQuest(questId)
            if (result != null) {
                _selectedQuestForDetail.value = result.quest
                _rewardCelebration.value = RewardCelebration(
                    title = "QUEST APPROVED! 🎉",
                    subtitle = "Great job completing '${result.quest.title}'!",
                    coinsEarned = result.coinsEarned,
                    xpEarned = result.xpEarned,
                    isLevelUp = result.levelUpResult.didLevelUp,
                    newLevel = result.levelUpResult.newLevel
                )
            }
        }
    }

    fun simulateRejection(questId: String, reason: String = "Please complete the task more carefully.") {
        viewModelScope.launch {
            repository.rejectQuest(questId, reason)
            _selectedQuestForDetail.value = repository.getQuestById(questId)
        }
    }

    fun retryQuest(questId: String) {
        viewModelScope.launch {
            repository.retryQuest(questId)
            _selectedQuestForDetail.value = repository.getQuestById(questId)
        }
    }

    fun getAchievements(): List<Achievement> = repository.getAchievements()

    class Factory(private val repository: GuildRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GuildViewModel(repository) as T
        }
    }
}
