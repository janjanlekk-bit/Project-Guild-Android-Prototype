package com.example.projectguild.ui.math

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectguild.data.GuildRepository
import com.example.projectguild.domain.math.MathQuestionGenerator
import com.example.projectguild.domain.model.MathDifficulty
import com.example.projectguild.domain.model.MathQuestion
import com.example.projectguild.domain.model.MathSession
import com.example.projectguild.domain.model.PlayerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MathUiState(
    val currentQuestion: MathQuestion,
    val questionNumber: Int = 1,
    val difficulty: MathDifficulty = MathDifficulty.MEDIUM,
    val selectedAnswer: Int? = null,
    val isAnswerChecked: Boolean = false,
    val isCorrect: Boolean = false,
    val previousScreenTime: Int = 45,
    val updatedScreenTime: Int = 45,
    val minutesReward: Int = 5,
    val session: MathSession = MathSession(),
    val isSessionComplete: Boolean = false
)

class MathViewModel(
    private val repository: GuildRepository,
    private val generator: MathQuestionGenerator = MathQuestionGenerator()
) : ViewModel() {

    val playerProfile: StateFlow<PlayerProfile> = repository.playerProfile

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<MathUiState> = _uiState.asStateFlow()

    private fun createInitialState(): MathUiState {
        val initialDiff = MathDifficulty.MEDIUM
        val q = generator.generateQuestion(initialDiff)
        val currentMinutes = repository.playerProfile.value.screenTimeMinutes
        return MathUiState(
            currentQuestion = q,
            questionNumber = 1,
            difficulty = initialDiff,
            previousScreenTime = currentMinutes,
            updatedScreenTime = currentMinutes
        )
    }

    fun setDifficulty(difficulty: MathDifficulty) {
        val nextQ = generator.generateQuestion(difficulty)
        _uiState.value = _uiState.value.copy(
            difficulty = difficulty,
            currentQuestion = nextQ,
            selectedAnswer = null,
            isAnswerChecked = false,
            isCorrect = false
        )
    }

    fun submitAnswer(answer: Int) {
        val state = _uiState.value
        if (state.isAnswerChecked && state.isCorrect) return

        val isRight = (answer == state.currentQuestion.correctAnswer)
        val currentMinutes = playerProfile.value.screenTimeMinutes
        val nextMinutes = if (isRight) currentMinutes + state.minutesReward else currentMinutes

        viewModelScope.launch {
            repository.recordMathAnswer(
                isCorrect = isRight,
                screenTimeRewardMinutes = state.minutesReward,
                xpReward = 5
            )
        }

        val updatedSession = if (isRight) {
            state.session.copy(
                questionsAttempted = state.session.questionsAttempted + 1,
                questionsCorrect = state.session.questionsCorrect + 1,
                screenTimeEarned = state.session.screenTimeEarned + state.minutesReward
            )
        } else {
            state.session.copy(
                questionsAttempted = state.session.questionsAttempted + 1
            )
        }

        _uiState.value = state.copy(
            selectedAnswer = answer,
            isAnswerChecked = true,
            isCorrect = isRight,
            previousScreenTime = currentMinutes,
            updatedScreenTime = nextMinutes,
            session = updatedSession
        )
    }

    fun nextQuestion() {
        val state = _uiState.value
        // Trigger session completion dialog every 5 questions
        if (state.questionNumber % 5 == 0 && state.isCorrect) {
            _uiState.value = state.copy(isSessionComplete = true)
            return
        }

        val nextQ = generator.generateQuestion(state.difficulty)
        val currentMinutes = playerProfile.value.screenTimeMinutes
        _uiState.value = state.copy(
            currentQuestion = nextQ,
            questionNumber = state.questionNumber + 1,
            selectedAnswer = null,
            isAnswerChecked = false,
            isCorrect = false,
            previousScreenTime = currentMinutes,
            updatedScreenTime = currentMinutes
        )
    }

    fun retryQuestion() {
        val state = _uiState.value
        _uiState.value = state.copy(
            selectedAnswer = null,
            isAnswerChecked = false,
            isCorrect = false
        )
    }

    fun continueSession() {
        val state = _uiState.value
        val nextQ = generator.generateQuestion(state.difficulty)
        val currentMinutes = playerProfile.value.screenTimeMinutes
        _uiState.value = state.copy(
            currentQuestion = nextQ,
            questionNumber = state.questionNumber + 1,
            selectedAnswer = null,
            isAnswerChecked = false,
            isCorrect = false,
            previousScreenTime = currentMinutes,
            updatedScreenTime = currentMinutes,
            isSessionComplete = false
        )
    }

    class Factory(
        private val repository: GuildRepository,
        private val generator: MathQuestionGenerator = MathQuestionGenerator()
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MathViewModel(repository, generator) as T
        }
    }
}
