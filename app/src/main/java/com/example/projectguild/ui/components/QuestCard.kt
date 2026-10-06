package com.example.projectguild.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.projectguild.domain.model.Quest
import com.example.projectguild.domain.model.QuestDifficulty
import com.example.projectguild.domain.model.QuestStatus
import com.example.projectguild.domain.model.VerificationType
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldDark
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildSuccess

@Composable
fun QuestCard(
    quest: Quest,
    onAcceptClick: (() -> Unit)? = null,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, GuildCardBorder, RoundedCornerShape(20.dp))
            .clickable { onViewClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top row: Difficulty Tag & Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyBadge(difficulty = quest.difficulty)
                QuestStatusBadge(status = quest.status)
            }

            // Main Info: Icon & Title/Description
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F3FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = quest.iconEmoji, fontSize = 24.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = quest.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = quest.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            // Rewards and Verification
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RewardTag(text = "+${quest.rewardCoins} 🪙", color = GuildGoldDark)
                    RewardTag(text = "+${quest.rewardXp} XP", color = GuildPrimary)
                }

                VerificationTag(verificationType = quest.verificationType)
            }

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (quest.status == QuestStatus.AVAILABLE && onAcceptClick != null) {
                    Button(
                        onClick = onAcceptClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GuildPrimary)
                    ) {
                        Text(
                            text = "ACCEPT QUEST",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                OutlinedButton(
                    onClick = onViewClick,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = when (quest.status) {
                            QuestStatus.AVAILABLE -> "View"
                            QuestStatus.ACTIVE -> "Open Quest"
                            QuestStatus.SUBMITTED -> "Check Status"
                            QuestStatus.REJECTED -> "Review Fix"
                            QuestStatus.APPROVED -> "Completed"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: QuestDifficulty) {
    val (label, bgColor, textColor) = when (difficulty) {
        QuestDifficulty.EASY -> Triple("EASY QUEST", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        QuestDifficulty.NORMAL -> Triple("NORMAL QUEST", Color(0xFFE3F2FD), Color(0xFF1565C0))
        QuestDifficulty.HARD -> Triple("HARD QUEST", Color(0xFFFFF3E0), Color(0xFFE65100))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun QuestStatusBadge(status: QuestStatus) {
    val (label, bgColor, textColor) = when (status) {
        QuestStatus.AVAILABLE -> Triple("AVAILABLE", Color(0xFFF1F3F5), Color(0xFF495057))
        QuestStatus.ACTIVE -> Triple("ACTIVE", Color(0xFFEDE7F6), GuildPrimary)
        QuestStatus.SUBMITTED -> Triple("WAITING APPROVAL", Color(0xFFFFF8E1), GuildGoldDark)
        QuestStatus.APPROVED -> Triple("APPROVED ✅", Color(0xFFE8F5E9), GuildSuccess)
        QuestStatus.REJECTED -> Triple("REVISION NEEDED", Color(0xFFFFEBEE), Color(0xFFC62828))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = textColor
        )
    }
}

@Composable
private fun RewardTag(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}

@Composable
private fun VerificationTag(verificationType: VerificationType) {
    val text = when (verificationType) {
        VerificationType.PARENT_CONFIRMATION -> "👨‍👩‍👧 Parent Confirmation"
        VerificationType.PHOTO_PROOF -> "📸 Photo Proof"
    }

    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
