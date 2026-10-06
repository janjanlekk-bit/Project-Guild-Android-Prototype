package com.example.projectguild.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class QuestDifficulty {
    EASY,
    NORMAL,
    HARD
}

@Serializable
enum class QuestStatus {
    AVAILABLE,
    ACTIVE,
    SUBMITTED,
    APPROVED,
    REJECTED
}

@Serializable
enum class VerificationType {
    PARENT_CONFIRMATION,
    PHOTO_PROOF
}

@Serializable
data class Quest(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val description: String,
    val objective: String,
    val difficulty: QuestDifficulty,
    val rewardCoins: Int,
    val rewardXp: Int,
    val verificationType: VerificationType,
    val status: QuestStatus = QuestStatus.AVAILABLE,
    val rejectionReason: String? = null,
    val mockProofImageNote: String? = null
)
