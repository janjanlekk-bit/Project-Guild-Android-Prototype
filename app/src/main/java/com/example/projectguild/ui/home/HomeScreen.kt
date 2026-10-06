package com.example.projectguild.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.projectguild.domain.model.Quest
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldDark
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildSuccess
import com.example.projectguild.ui.GuildViewModel
import com.example.projectguild.ui.components.AdventurerAvatar
import com.example.projectguild.ui.components.GuildCoinsCard
import com.example.projectguild.ui.components.QuestCard
import com.example.projectguild.ui.components.ScreenTimeCard
import com.example.projectguild.ui.components.XpProgressBar

@Composable
fun HomeScreen(
    viewModel: GuildViewModel,
    onNavigateToMath: () -> Unit,
    onNavigateToGuild: () -> Unit,
    onOpenQuest: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val player by viewModel.playerProfile.collectAsState()
    val quests by viewModel.quests.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GuildBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Greeting Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GOOD MORNING,",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = GuildPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${player.playerName.uppercase()}, ADVENTURER! ⚔️",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            AdventurerAvatar(
                level = player.level,
                size = 64.dp
            )
        }

        // XP Progress Bar
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GuildCardBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                XpProgressBar(
                    currentXp = player.xp,
                    maxXp = player.xpForNextLevel,
                    level = player.level
                )
            }
        }

        // Balance Cards: Screen Time & Guild Coins
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScreenTimeCard(
                minutes = player.screenTimeMinutes,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToMath
            )
            GuildCoinsCard(
                coins = player.guildCoins,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToGuild
            )
        }

        // Training Grounds Callout Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GuildCardBorder, RoundedCornerShape(24.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6C5CE7), Color(0xFF0984E3))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🧠", fontSize = 18.sp)
                        }
                        Text(
                            text = "TRAINING GROUNDS",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "Math Challenge",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = "Solve math problems to earn free screen time minutes! Each correct problem rewards +5 minutes.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onNavigateToMath,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GuildGold,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "START TRAINING ⚡",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Today's Quests Section
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S QUESTS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "View All Guild ⚔️",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = GuildPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }

            quests.take(3).forEach { quest ->
                QuestCard(
                    quest = quest,
                    onAcceptClick = { viewModel.acceptQuest(quest.id) },
                    onViewClick = { onOpenQuest(quest.id) }
                )
            }
        }

        // Today's Progress Section
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GuildCardBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "TODAY'S PROGRESS",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )

                ProgressRowItem(
                    emoji = "⭐",
                    label = "${player.completedQuestsCount} quests completed"
                )
                ProgressRowItem(
                    emoji = "🧠",
                    label = "${player.totalMathSolved} math problems solved"
                )
                ProgressRowItem(
                    emoji = "🔥",
                    label = "${player.streakDays} day adventurer streak"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProgressRowItem(emoji: String, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFF4F6FC)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
