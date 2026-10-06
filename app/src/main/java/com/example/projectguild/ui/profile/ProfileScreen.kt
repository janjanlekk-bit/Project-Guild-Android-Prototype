package com.example.projectguild.ui.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectguild.domain.model.Achievement
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldDark
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildScreenTime
import com.example.projectguild.theme.GuildStreakFire
import com.example.projectguild.theme.GuildSuccess
import com.example.projectguild.ui.GuildViewModel
import com.example.projectguild.ui.components.AdventurerAvatar
import com.example.projectguild.ui.components.XpProgressBar

@Composable
fun ProfileScreen(
    viewModel: GuildViewModel,
    modifier: Modifier = Modifier
) {
    val player by viewModel.playerProfile.collectAsState()
    val achievements = viewModel.getAchievements()
    val coinTxs by viewModel.coinTransactions.collectAsState()
    val stTxs by viewModel.screenTimeTransactions.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GuildBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Title
        Text(
            text = "🧙 MY ADVENTURER",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Character Hero Profile Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GuildCardBorder, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AdventurerAvatar(
                    level = player.level,
                    size = 84.dp
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = player.playerName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Level ${player.level} Guild Apprentice",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GuildPrimary
                    )
                }

                XpProgressBar(
                    currentXp = player.xp,
                    maxXp = player.xpForNextLevel,
                    level = player.level
                )

                // 3 Quick Stat Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatItem(
                        icon = "🪙",
                        value = "${player.guildCoins}",
                        label = "Coins",
                        valueColor = GuildGoldDark
                    )
                    ProfileStatItem(
                        icon = "⏱",
                        value = "${player.screenTimeMinutes}m",
                        label = "Screen Time",
                        valueColor = GuildScreenTime
                    )
                    ProfileStatItem(
                        icon = "🔥",
                        value = "${player.streakDays}d",
                        label = "Streak",
                        valueColor = GuildStreakFire
                    )
                }
            }
        }

        // Achievements Section
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "ACHIEVEMENTS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )

            achievements.forEach { achievement ->
                AchievementCard(achievement = achievement)
            }
        }

        // Recent Activity Ledger Preview
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "RECENT ACTIVITY LEDGER",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )

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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    coinTxs.take(2).forEach { tx ->
                        ActivityRowItem(
                            icon = "🪙",
                            title = tx.reason,
                            change = "+${tx.amount} coins",
                            color = GuildGoldDark
                        )
                    }

                    stTxs.take(2).forEach { tx ->
                        ActivityRowItem(
                            icon = "⏱",
                            title = tx.reason,
                            change = "+${tx.amount} min",
                            color = GuildScreenTime
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun ProfileStatItem(
    icon: String,
    value: String,
    label: String,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = valueColor
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) Color(0xFFF9FFF9) else Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                if (achievement.isUnlocked) Color(0xFFA5D6A7) else GuildCardBorder,
                RoundedCornerShape(18.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isUnlocked) Color(0xFFE8F5E9) else Color(0xFFF1F3F8)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = achievement.iconEmoji, fontSize = 22.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = achievement.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (achievement.isUnlocked) {
                        Text(
                            text = "UNLOCKED 🏆",
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp,
                            color = GuildSuccess
                        )
                    }
                }

                Text(
                    text = achievement.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!achievement.isUnlocked) {
                    Spacer(modifier = Modifier.height(6.dp))
                    val progress = (achievement.currentProgress.toFloat() / achievement.targetProgress.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GuildPrimary,
                        trackColor = Color(0xFFE2E6F0)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${achievement.currentProgress} / ${achievement.targetProgress}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityRowItem(
    icon: String,
    title: String,
    change: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = icon, fontSize = 16.sp)
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = change,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
