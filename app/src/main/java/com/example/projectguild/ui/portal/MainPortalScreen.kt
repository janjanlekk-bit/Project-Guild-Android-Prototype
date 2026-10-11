package com.example.projectguild.ui.portal

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildScreenTime
import com.example.projectguild.ui.GuildViewModel
import com.example.projectguild.ui.components.AdventurerAvatar
import com.example.projectguild.ui.components.XpProgressBar

@Composable
fun MainPortalScreen(
    viewModel: GuildViewModel,
    onNavigateToScreenTime: () -> Unit,
    onNavigateToCoins: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val player by viewModel.playerProfile.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GuildBackground)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Compact Top Greeting & Avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WELCOME BACK,",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = GuildPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${player.playerName.uppercase()} ⚔️",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onNavigateToProfile() }
            ) {
                AdventurerAvatar(
                    level = player.level,
                    size = 52.dp
                )
            }
        }

        // Compact Level & XP Bar Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GuildCardBorder, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                XpProgressBar(
                    currentXp = player.xp,
                    maxXp = player.xpForNextLevel,
                    level = player.level
                )
            }
        }

        // Section Prompt Label
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CHOOSE YOUR PATH TODAY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Text(
                text = "🧙 Tap avatar for stats",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GuildPrimary,
                modifier = Modifier.clickable { onNavigateToProfile() }
            )
        }

        // CHOICE 1: EARN SCREEN TIME (Weighted to fit perfectly)
        PortalChoiceCard(
            emoji = "⏱️",
            title = "EARN SCREEN TIME",
            subtitle = "Solve learning puzzles & math challenges to earn device time for games!",
            statBadge = "⏱️ ${player.screenTimeMinutes} MIN AVAILABLE",
            tags = listOf("🧠 Math Arena", "⚡ +5m / Problem", "🔥 ${player.streakDays}d Streak"),
            buttonText = "OPEN SCREEN TIME",
            gradientColors = listOf(Color(0xFF0984E3), Color(0xFF5A45FF)),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            onClick = onNavigateToScreenTime
        )

        // CHOICE 2: EARN DIGITAL COINS (Weighted to fit perfectly)
        PortalChoiceCard(
            emoji = "🪙",
            title = "EARN DIGITAL COINS",
            subtitle = "Complete real-world quests at home, submit photo proof, and earn gold!",
            statBadge = "🪙 ${player.guildCoins} COINS SAVED",
            tags = listOf("⚔️ Real Quests", "📸 Photo Proof", "⭐ Parent Approved"),
            buttonText = "OPEN GUILD COINS",
            gradientColors = listOf(Color(0xFFFF9F1A), Color(0xFFE65100)),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            onClick = onNavigateToCoins
        )
    }
}

@Composable
private fun PortalChoiceCard(
    emoji: String,
    title: String,
    subtitle: String,
    statBadge: String,
    tags: List<String>,
    buttonText: String,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradientColors))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Emoji & Stat Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 22.sp)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.22f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = statBadge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Middle: Title & Subtitle
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.92f),
                        lineHeight = 16.sp
                    )
                }

                // Tag Chips Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.18f))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Bottom Action Button Strip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .clickable { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = buttonText,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = gradientColors.first()
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = gradientColors.first(),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}
