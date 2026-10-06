package com.example.projectguild.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectguild.theme.GuildGold
import com.example.projectguild.theme.GuildGoldDark
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.theme.GuildPrimaryDark

@Composable
fun AdventurerAvatar(
    level: Int,
    size: Dp = 72.dp,
    avatarEmoji: String = "🧙",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer Glowing Avatar Ring
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(GuildGold.copy(alpha = 0.35f), Color.Transparent)
                    )
                )
                .border(
                    width = 3.dp,
                    brush = Brush.linearGradient(listOf(GuildGold, GuildPrimary)),
                    shape = CircleShape
                )
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = avatarEmoji,
                fontSize = (size.value * 0.45f).sp
            )
        }

        // Level Badge at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 6.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(GuildPrimary, GuildPrimaryDark)
                    )
                )
                .border(1.dp, GuildGold, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "LVL $level",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
