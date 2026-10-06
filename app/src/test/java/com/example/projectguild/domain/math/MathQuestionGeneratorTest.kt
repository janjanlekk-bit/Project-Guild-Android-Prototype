package com.example.projectguild.domain.math

import com.example.projectguild.domain.model.MathDifficulty
import com.example.projectguild.domain.model.MathOperation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class MathQuestionGeneratorTest {

    @Test
    fun generateQuestion_optionsContainCorrectAnswer_andHasFourChoices() {
        val generator = MathQuestionGenerator(Random(42))

        for (difficulty in MathDifficulty.entries) {
            repeat(20) {
                val question = generator.generateQuestion(difficulty)
                assertEquals(4, question.options.size)
                assertTrue("Options must contain the correct answer", question.options.contains(question.correctAnswer))
                assertEquals("Options should be unique", 4, question.options.toSet().size)
            }
        }
    }

    @Test
    fun generateQuestion_subtractionNeverProducesNegativeResults() {
        val generator = MathQuestionGenerator(Random(12345))

        repeat(200) {
            val easyQ = generator.generateQuestion(MathDifficulty.EASY)
            if (easyQ.operation == MathOperation.SUBTRACTION) {
                assertTrue("Easy subtraction answer must be non-negative: ${easyQ.equationText}", easyQ.correctAnswer >= 0)
                assertTrue("Easy minuend must be >= subtrahend: ${easyQ.equationText}", easyQ.num1 >= easyQ.num2)
            }

            val medQ = generator.generateQuestion(MathDifficulty.MEDIUM)
            if (medQ.operation == MathOperation.SUBTRACTION) {
                assertTrue("Medium subtraction answer must be non-negative: ${medQ.equationText}", medQ.correctAnswer >= 0)
                assertTrue("Medium minuend must be >= subtrahend: ${medQ.equationText}", medQ.num1 >= medQ.num2)
            }

            val hardQ = generator.generateQuestion(MathDifficulty.HARD)
            if (hardQ.operation == MathOperation.SUBTRACTION) {
                assertTrue("Hard subtraction answer must be non-negative: ${hardQ.equationText}", hardQ.correctAnswer >= 0)
                assertTrue("Hard minuend must be >= subtrahend: ${hardQ.equationText}", hardQ.num1 >= hardQ.num2)
            }
        }
    }

    @Test
    fun generateQuestion_divisionHasNoFractionsOrRemainders() {
        val generator = MathQuestionGenerator(Random(9999))

        repeat(100) {
            val q = generator.generateQuestion(MathDifficulty.HARD)
            if (q.operation == MathOperation.DIVISION) {
                assertTrue("Divisor must be > 0: ${q.equationText}", q.num2 > 0)
                assertEquals("Division must have zero remainder: ${q.equationText}", 0, q.num1 % q.num2)
                assertEquals("Quotient must match answer: ${q.equationText}", q.num1 / q.num2, q.correctAnswer)
            }
        }
    }

    @Test
    fun generateQuestion_difficultyConstraints_easyAreOneDigit() {
        val generator = MathQuestionGenerator(Random(777))

        repeat(50) {
            val q = generator.generateQuestion(MathDifficulty.EASY)
            assertTrue("Easy numbers should be single digit or small", q.num1 in 1..10)
            assertTrue("Easy numbers should be single digit or small", q.num2 in 1..10)
        }
    }

    @Test
    fun generateQuestion_deterministicWithSameSeed() {
        val gen1 = MathQuestionGenerator(Random(42))
        val gen2 = MathQuestionGenerator(Random(42))

        val q1 = gen1.generateQuestion(MathDifficulty.MEDIUM)
        val q2 = gen2.generateQuestion(MathDifficulty.MEDIUM)

        assertEquals(q1.num1, q2.num1)
        assertEquals(q1.num2, q2.num2)
        assertEquals(q1.operation, q2.operation)
        assertEquals(q1.correctAnswer, q2.correctAnswer)
    }
}
