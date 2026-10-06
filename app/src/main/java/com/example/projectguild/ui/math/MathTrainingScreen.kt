package com.example.projectguild.ui.math

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.projectguild.domain.model.MathDifficulty
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildDanger
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldDark
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildScreenTime
import com.example.projectguild.theme.GuildSuccess

@Composable
fun MathTrainingScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val player by viewModel.playerProfile.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GuildBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "🧠 TRAINING GROUNDS",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Solve problems to earn screen time. Each correct answer gives +5 minutes!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Current Screen Time Banner
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, Color(0xFFBBDEFB), RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GuildScreenTime.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⏱", fontSize = 22.sp)
                    }
                    Column {
                        Text(
                            text = "CURRENT SCREEN TIME",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${player.screenTimeMinutes} MINUTES",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = GuildScreenTime
                        )
                    }
                }

                // Difficulty selector row
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "REWARD: +5 MIN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        // Difficulty Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MathDifficulty.entries.forEach { diff ->
                FilterChip(
                    selected = uiState.difficulty == diff,
                    onClick = { viewModel.setDifficulty(diff) },
                    label = {
                        Text(
                            text = diff.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GuildPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Question Arena Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GuildCardBorder, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Question Counter Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF1F3FA))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "QUESTION #${uiState.questionNumber}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = GuildPrimary,
                        letterSpacing = 1.sp
                    )
                }

                // Main Math Equation Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFF8F9FE), Color(0xFFECEFF8))
                            )
                        )
                        .padding(vertical = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.currentQuestion.equationText,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E232A),
                        letterSpacing = 2.sp
                    )
                }

                // 4 Multiple Choice Answer Options
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val options = uiState.currentQuestion.options
                    // Display in 2x2 grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        options.take(2).forEach { option ->
                            MathOptionButton(
                                option = option,
                                isSelected = uiState.selectedAnswer == option,
                                isChecked = uiState.isAnswerChecked,
                                isCorrect = uiState.currentQuestion.correctAnswer == option,
                                onClick = { viewModel.submitAnswer(option) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        options.drop(2).take(2).forEach { option ->
                            MathOptionButton(
                                option = option,
                                isSelected = uiState.selectedAnswer == option,
                                isChecked = uiState.isAnswerChecked,
                                isCorrect = uiState.currentQuestion.correctAnswer == option,
                                onClick = { viewModel.submitAnswer(option) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Feedback section
                if (uiState.isAnswerChecked) {
                    if (uiState.isCorrect) {
                        // Correct banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE8F5E9))
                                .border(1.5.dp, GuildSuccess, RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🎉 CORRECT!",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "+5 MINUTES EARNED",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF388E3C)
                                )
                                Text(
                                    text = "Screen Time: ${uiState.previousScreenTime} → ${uiState.updatedScreenTime} min",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.nextQuestion() },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GuildSuccess),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text = "NEXT QUESTION ➔",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        // Incorrect banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFF3E0))
                                .border(1.5.dp, Color(0xFFFFB74D), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Not quite!",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFE65100)
                                )
                                Text(
                                    text = uiState.currentQuestion.explanation,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFBF360C)
                                )
                                Text(
                                    text = "No screen time lost. You can try again!",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF795548)
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.retryQuestion() },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "TRY AGAIN 🔄",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Session Summary Dialog
    if (uiState.isSessionComplete) {
        Dialog(onDismissRequest = { viewModel.continueSession() }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "🏆", fontSize = 44.sp)
                    Text(
                        text = "TRAINING COMPLETE!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = GuildPrimary
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF7F8FC))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SessionStatLine("Questions Solved:", "${uiState.session.questionsAttempted}")
                        SessionStatLine("Correct Answers:", "${uiState.session.questionsCorrect}")
                        SessionStatLine("Accuracy:", "${uiState.session.accuracy}%")
                        SessionStatLine("Screen Time Earned:", "+${uiState.session.screenTimeEarned} min", highlight = true)
                    }

                    Button(
                        onClick = { viewModel.continueSession() },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GuildPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = "CONTINUE TRAINING ⚡",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MathOptionButton(
    option: Int,
    isSelected: Boolean,
    isChecked: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isChecked && isCorrect -> Color(0xFFE8F5E9)
        isChecked && isSelected && !isCorrect -> Color(0xFFFFEBEE)
        isSelected -> GuildPrimary.copy(alpha = 0.15f)
        else -> Color.White
    }

    val borderColor = when {
        isChecked && isCorrect -> GuildSuccess
        isChecked && isSelected && !isCorrect -> GuildDanger
        isSelected -> GuildPrimary
        else -> GuildCardBorder
    }

    val textColor = when {
        isChecked && isCorrect -> Color(0xFF2E7D32)
        isChecked && isSelected && !isCorrect -> GuildDanger
        isSelected -> GuildPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = !isChecked || !isCorrect) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$option",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = textColor
        )
    }
}

@Composable
private fun SessionStatLine(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = if (highlight) 16.sp else 14.sp,
            fontWeight = FontWeight.Black,
            color = if (highlight) GuildSuccess else MaterialTheme.colorScheme.onSurface
        )
    }
}
