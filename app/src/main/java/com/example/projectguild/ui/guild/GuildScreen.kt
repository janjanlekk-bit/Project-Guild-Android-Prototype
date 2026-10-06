package com.example.projectguild.ui.guild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectguild.domain.model.QuestStatus
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.theme.GuildGoldDark
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.ui.GuildViewModel
import com.example.projectguild.ui.components.QuestCard

@Composable
fun GuildScreen(
    viewModel: GuildViewModel,
    onOpenQuest: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val quests by viewModel.quests.collectAsState()
    val activeQuests by viewModel.activeQuests.collectAsState()
    val availableQuests by viewModel.availableQuests.collectAsState()
    val completedQuests by viewModel.completedQuests.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Active (${activeQuests.size})", "Available (${availableQuests.size})", "Completed (${completedQuests.size})")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GuildBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Guild Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "⚔️ ADVENTURER'S GUILD",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Accept missions, submit photo proof, and earn Guild Coins & XP!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = GuildPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = GuildPrimary
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Black else FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (selectedTabIndex == index) GuildPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // Tab Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTabIndex) {
                0 -> {
                    if (activeQuests.isEmpty()) {
                        EmptyQuestsNotice(
                            emoji = "🛡️",
                            title = "No Active Quests",
                            subtitle = "Check the 'Available' tab to accept new missions!"
                        )
                    } else {
                        activeQuests.forEach { quest ->
                            QuestCard(
                                quest = quest,
                                onViewClick = { onOpenQuest(quest.id) }
                            )
                        }
                    }
                }
                1 -> {
                    if (availableQuests.isEmpty()) {
                        EmptyQuestsNotice(
                            emoji = "✨",
                            title = "All Quests Accepted!",
                            subtitle = "Head over to your Active Quests to submit proof."
                        )
                    } else {
                        availableQuests.forEach { quest ->
                            QuestCard(
                                quest = quest,
                                onAcceptClick = { viewModel.acceptQuest(quest.id) },
                                onViewClick = { onOpenQuest(quest.id) }
                            )
                        }
                    }
                }
                2 -> {
                    if (completedQuests.isEmpty()) {
                        EmptyQuestsNotice(
                            emoji = "📜",
                            title = "No Completed Quests Yet",
                            subtitle = "Complete your daily quests to build your adventurer legacy!"
                        )
                    } else {
                        completedQuests.forEach { quest ->
                            QuestCard(
                                quest = quest,
                                onViewClick = { onOpenQuest(quest.id) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun EmptyQuestsNotice(
    emoji: String,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = emoji, fontSize = 42.sp)
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }
    }
}
