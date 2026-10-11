package com.example.projectguild.ui.portal

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldDark
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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Greeting Banner & Adventurer Avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "WELCOME BACK,",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = GuildPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${player.playerName.uppercase()} ⚔️",
                    style = MaterialTheme.typography.headlineSmall,
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
                    size = 64.dp
                )
            }
        }

        // XP Progress Card
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

        // Section Title: The 2 Choices
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "CHOOSE YOUR ACTIVITY",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Text(
                text = "What would you like to earn right now?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // CHOICE 1: EARN SCREEN TIME
        PortalChoiceCard(
            emoji = "⏱️",
            title = "EARN SCREEN TIME",
            subtitle = "Solve learning puzzles & math challenges to earn device time for games and videos!",
            statBadge = "⏱️ ${player.screenTimeMinutes} MIN AVAILABLE",
            tags = listOf("🧠 Math Challenges", "⚡ Quick Practice", "🔥 Daily Streak"),
            buttonText = "OPEN SCREEN TIME",
            gradientColors = listOf(Color(0xFF0984E3), Color(0xFF5A45FF)),
            badgeBgColor = Color.White.copy(alpha = 0.22f),
            badgeTextColor = Color.White,
            onClick = onNavigateToScreenTime
        )

        // CHOICE 2: EARN DIGITAL COINS
        PortalChoiceCard(
            emoji = "🪙",
            title = "EARN DIGITAL COINS",
            subtitle = "Complete real-world quests & chores at home, submit photo proof, and earn guild gold!",
            statBadge = "🪙 ${player.guildCoins} GUILD COINS",
            tags = listOf("⚔️ Real Quests", "📸 Photo Proof", "⭐ Parent Approved"),
            buttonText = "OPEN GUILD COINS",
            gradientColors = listOf(Color(0xFFFF9F1A), Color(0xFFE65100)),
            badgeBgColor = Color.White.copy(alpha = 0.22f),
            badgeTextColor = Color.White,
            onClick = onNavigateToCoins
        )

        // Footer Card: Adventurer Profile & Achievements Shortcut
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GuildCardBorder, RoundedCornerShape(20.dp))
                .clickable { onNavigateToProfile() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F0FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🧙", fontSize = 24.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Adventurer Profile & Badges",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${player.completedQuestsCount} quests done • ${player.streakDays} day streak",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "View Profile",
                    tint = GuildPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
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
    badgeBgColor: Color,
    badgeTextColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradientColors))
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Top Header Row: Emoji & Current Stat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 28.sp)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(badgeBgColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = statBadge,
                            color = badgeTextColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Title & Subtitle
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.92f),
                        lineHeight = 18.sp
                    )
                }

                // Tag Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.16f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Action Button / Strip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .clickable { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = buttonText,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = gradientColors.first()
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = gradientColors.first(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
