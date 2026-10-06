package com.example.projectguild.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectguild.theme.GuildCardBorder
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldContainer
import com.example.projectguild.theme.GuildOnGoldContainer
import com.example.projectguild.theme.GuildOnScreenTimeContainer
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildScreenTime
import com.example.projectguild.theme.GuildScreenTimeContainer

@Composable
fun ScreenTimeCard(
    minutes: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GuildScreenTimeContainer)
            .border(1.5.dp, GuildScreenTime.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp)
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
                    text = "SCREEN TIME",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = GuildOnScreenTimeContainer.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$minutes",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = GuildOnScreenTimeContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "min",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GuildOnScreenTimeContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GuildCoinsCard(
    coins: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GuildGoldContainer)
            .border(1.5.dp, GuildGold.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(GuildGold.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🪙", fontSize = 22.sp)
            }
            Column {
                Text(
                    text = "GUILD COINS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = GuildOnGoldContainer.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$coins",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = GuildOnGoldContainer
                )
            }
        }
    }
}

@Composable
fun XpProgressBar(
    currentXp: Int,
    maxXp: Int,
    level: Int,
    modifier: Modifier = Modifier
) {
    val progress = (currentXp.toFloat() / maxXp.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "xpAnim")

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LEVEL $level ADVENTURER",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = GuildPrimary,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "$currentXp / $maxXp XP",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFE2E6F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(GuildPrimary, Color(0xFF8E24AA), GuildGold)
                        )
                    )
            )
        }
    }
}
