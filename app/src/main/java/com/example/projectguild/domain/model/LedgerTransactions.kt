package com.example.projectguild.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScreenTimeTransaction(
    val id: String,
    val amount: Int,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class CoinTransaction(
    val id: String,
    val amount: Int,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class XpTransaction(
    val id: String,
    val amount: Int,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)
