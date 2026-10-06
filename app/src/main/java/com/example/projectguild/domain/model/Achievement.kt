package com.example.projectguild.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val isUnlocked: Boolean
)
