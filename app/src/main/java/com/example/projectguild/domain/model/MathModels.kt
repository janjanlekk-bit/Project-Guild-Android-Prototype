package com.example.projectguild.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MathDifficulty {
    EASY,
    MEDIUM,
    HARD
}

@Serializable
enum class MathOperation(val symbol: String) {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("×"),
    DIVISION("÷")
}

@Serializable
data class MathQuestion(
    val id: String,
    val num1: Int,
    val num2: Int,
    val operation: MathOperation,
    val correctAnswer: Int,
    val options: List<Int>,
    val difficulty: MathDifficulty,
    val explanation: String
) {
    val equationText: String
        get() = "$num1 ${operation.symbol} $num2 = ?"
}

@Serializable
data class MathSession(
    val questionsAttempted: Int = 0,
    val questionsCorrect: Int = 0,
    val screenTimeEarned: Int = 0
) {
    val accuracy: Int
        get() = if (questionsAttempted > 0) (questionsCorrect * 100) / questionsAttempted else 0
}
