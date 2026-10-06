package com.example.projectguild.ui.guild

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.projectguild.domain.model.Quest
import com.example.projectguild.domain.model.QuestStatus
import com.example.projectguild.domain.model.VerificationType
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildDanger
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldDark
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildSuccess
import com.example.projectguild.ui.components.DifficultyBadge
import com.example.projectguild.ui.components.QuestStatusBadge

@Composable
fun QuestDetailDialog(
    quest: Quest?,
    onDismiss: () -> Unit,
    onAcceptQuest: (String) -> Unit,
    onSubmitProof: (String) -> Unit,
    onSimulateParentApproval: (String) -> Unit,
    onSimulateRejection: (String) -> Unit,
    onRetryQuest: (String) -> Unit
) {
    if (quest == null) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with status & close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuestStatusBadge(status = quest.status)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close")
                    }
                }

                // Quest Title & Emoji
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(GuildPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = quest.iconEmoji, fontSize = 28.sp)
                    }
                    Column {
                        Text(
                            text = quest.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black
                        )
                        DifficultyBadge(difficulty = quest.difficulty)
                    }
                }

                // Objective Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF7F8FC))
                        .border(1.dp, GuildCardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "OBJECTIVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = quest.objective,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Reward & Verification Summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "REWARD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "+${quest.rewardCoins} 🪙",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = GuildGoldDark
                            )
                            Text(
                                text = "+${quest.rewardXp} XP",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = GuildPrimary
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "VERIFICATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (quest.verificationType == VerificationType.PHOTO_PROOF) "📸 Photo Proof" else "👨‍👩‍👧 Parent Confirm",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Status-dependent Section
                when (quest.status) {
                    QuestStatus.AVAILABLE -> {
                        Button(
                            onClick = { onAcceptQuest(quest.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GuildPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "ACCEPT QUEST ⚔️", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }

                    QuestStatus.ACTIVE -> {
                        // Mock proof section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFEBF5FF))
                                .border(1.dp, Color(0xFF90CAF9), RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "📸", fontSize = 32.sp)
                                Text(
                                    text = "Ready to complete your quest?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1565C0)
                                )
                                Text(
                                    text = "Tap below to submit mock proof for your parent.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1976D2)
                                )
                            }
                        }

                        Button(
                            onClick = { onSubmitProof(quest.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GuildPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "SUBMIT PROOF 📸", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }

                    QuestStatus.SUBMITTED -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFF9C4))
                                .border(1.dp, Color(0xFFFFD54F), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "⏳", fontSize = 30.sp)
                                Text(
                                    text = "PROOF SUBMITTED",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = Color(0xFF795548)
                                )
                                Text(
                                    text = "Waiting for Guild Master / Parent approval...",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF8D6E63)
                                )
                            }
                        }

                        // Prototype-only controls
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF5F5F5))
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🛠 PROTOTYPE CONTROLS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF757575)
                                )

                                Button(
                                    onClick = { onSimulateParentApproval(quest.id) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GuildSuccess),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "👑 SIMULATE PARENT APPROVAL", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }

                                OutlinedButton(
                                    onClick = { onSimulateRejection(quest.id) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "❌ SIMULATE REJECTION", color = GuildDanger, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    QuestStatus.REJECTED -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFEBEE))
                                .border(1.dp, Color(0xFFEF9A9A), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "❌ QUEST REJECTED",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = GuildDanger
                                )
                                Text(
                                    text = "Reason: ${quest.rejectionReason ?: "Please complete the task more carefully."}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFB71C1C)
                                )
                            }
                        }

                        Button(
                            onClick = { onRetryQuest(quest.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GuildPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "TRY AGAIN 🔄", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }

                    QuestStatus.APPROVED -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE8F5E9))
                                .border(1.dp, Color(0xFFA5D6A7), RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "✅", fontSize = 32.sp)
                                Text(
                                    text = "QUEST APPROVED!",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "+${quest.rewardCoins} Guild Coins & +${quest.rewardXp} XP awarded!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF388E3C)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
