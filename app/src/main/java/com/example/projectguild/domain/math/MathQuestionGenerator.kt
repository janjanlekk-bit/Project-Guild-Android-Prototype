package com.example.projectguild.domain.math

import com.example.projectguild.domain.model.MathDifficulty
import com.example.projectguild.domain.model.MathOperation
import com.example.projectguild.domain.model.MathQuestion
import java.util.UUID
import kotlin.random.Random

class MathQuestionGenerator(
    private val random: Random = Random.Default
) {

    fun generateQuestion(difficulty: MathDifficulty): MathQuestion {
        val operation = chooseOperation(difficulty)
        val (num1, num2, answer) = generateOperands(operation, difficulty)
        val options = generateDistractors(answer)
        val explanation = generateExplanation(operation, num1, num2, answer)

        return MathQuestion(
            id = UUID.randomUUID().toString(),
            num1 = num1,
            num2 = num2,
            operation = operation,
            correctAnswer = answer,
            options = options,
            difficulty = difficulty,
            explanation = explanation
        )
    }

    private fun chooseOperation(difficulty: MathDifficulty): MathOperation {
        return when (difficulty) {
            MathDifficulty.EASY -> {
                // Prioritize addition and subtraction for elementary school learners
                if (random.nextBoolean()) MathOperation.ADDITION else MathOperation.SUBTRACTION
            }
            MathDifficulty.MEDIUM -> {
                when (random.nextInt(100)) {
                    in 0..44 -> MathOperation.ADDITION
                    in 45..84 -> MathOperation.SUBTRACTION
                    else -> MathOperation.MULTIPLICATION
                }
            }
            MathDifficulty.HARD -> {
                when (random.nextInt(100)) {
                    in 0..29 -> MathOperation.ADDITION
                    in 30..59 -> MathOperation.SUBTRACTION
                    in 60..79 -> MathOperation.MULTIPLICATION
                    else -> MathOperation.DIVISION
                }
            }
        }
    }

    private fun generateOperands(
        operation: MathOperation,
        difficulty: MathDifficulty
    ): Triple<Int, Int, Int> {
        return when (operation) {
            MathOperation.ADDITION -> {
                when (difficulty) {
                    MathDifficulty.EASY -> {
                        val n1 = random.nextInt(1, 10)
                        val n2 = random.nextInt(1, 10)
                        Triple(n1, n2, n1 + n2)
                    }
                    MathDifficulty.MEDIUM -> {
                        val n1 = random.nextInt(10, 50)
                        val n2 = random.nextInt(5, 30)
                        Triple(n1, n2, n1 + n2)
                    }
                    MathDifficulty.HARD -> {
                        val n1 = random.nextInt(25, 99)
                        val n2 = random.nextInt(25, 99)
                        Triple(n1, n2, n1 + n2)
                    }
                }
            }
            MathOperation.SUBTRACTION -> {
                when (difficulty) {
                    MathDifficulty.EASY -> {
                        val n1 = random.nextInt(2, 11)
                        val n2 = random.nextInt(1, n1) // Guarantees n1 >= n2, strictly non-negative
                        Triple(n1, n2, n1 - n2)
                    }
                    MathDifficulty.MEDIUM -> {
                        val n1 = random.nextInt(15, 60)
                        val n2 = random.nextInt(3, n1) // Guarantees non-negative result
                        Triple(n1, n2, n1 - n2)
                    }
                    MathDifficulty.HARD -> {
                        val n1 = random.nextInt(40, 100)
                        val n2 = random.nextInt(10, n1) // Guarantees non-negative result
                        Triple(n1, n2, n1 - n2)
                    }
                }
            }
            MathOperation.MULTIPLICATION -> {
                when (difficulty) {
                    MathDifficulty.EASY -> {
                        val n1 = random.nextInt(1, 6)
                        val n2 = random.nextInt(1, 6)
                        Triple(n1, n2, n1 * n2)
                    }
                    MathDifficulty.MEDIUM -> {
                        val n1 = random.nextInt(2, 10)
                        val n2 = random.nextInt(2, 6)
                        Triple(n1, n2, n1 * n2)
                    }
                    MathDifficulty.HARD -> {
                        val n1 = random.nextInt(4, 12)
                        val n2 = random.nextInt(4, 12)
                        Triple(n1, n2, n1 * n2)
                    }
                }
            }
            MathOperation.DIVISION -> {
                // Ensure whole-number results, no fractions/remainders, and no division by zero
                val divisor = when (difficulty) {
                    MathDifficulty.EASY -> random.nextInt(2, 5)
                    MathDifficulty.MEDIUM -> random.nextInt(2, 7)
                    MathDifficulty.HARD -> random.nextInt(3, 10)
                }
                val quotient = when (difficulty) {
                    MathDifficulty.EASY -> random.nextInt(1, 6)
                    MathDifficulty.MEDIUM -> random.nextInt(2, 8)
                    MathDifficulty.HARD -> random.nextInt(3, 12)
                }
                val dividend = divisor * quotient
                Triple(dividend, divisor, quotient)
            }
        }
    }

    private fun generateDistractors(answer: Int): List<Int> {
        val options = mutableSetOf(answer)
        val deltas = listOf(-2, -1, 1, 2, 3, -3, 4, -4, 5, -5).shuffled(random)

        for (delta in deltas) {
            val candidate = answer + delta
            if (candidate >= 0 && candidate != answer) {
                options.add(candidate)
            }
            if (options.size == 4) break
        }

        // Fallback in case options still under 4
        var offset = 10
        while (options.size < 4) {
            val candidate = (answer + offset).coerceAtLeast(0)
            options.add(candidate)
            offset += 5
        }

        return options.toList().shuffled(random)
    }

    private fun generateExplanation(
        operation: MathOperation,
        num1: Int,
        num2: Int,
        answer: Int
    ): String {
        return when (operation) {
            MathOperation.ADDITION ->
                "$num1 + $num2 = $answer because combining $num1 and $num2 gives $answer."
            MathOperation.SUBTRACTION ->
                "$num1 - $num2 = $answer because taking $num2 away from $num1 leaves $answer."
            MathOperation.MULTIPLICATION ->
                "$num1 × $num2 = $answer because $num1 groups of $num2 equals $answer."
            MathOperation.DIVISION ->
                "$num1 ÷ $num2 = $answer because $num2 fits into $num1 exactly $answer times ($answer × $num2 = $num1)."
        }
    }
}
