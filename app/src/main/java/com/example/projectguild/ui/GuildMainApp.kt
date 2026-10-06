package com.example.projectguild.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectguild.GuildApplication
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.theme.GuildPrimary
import com.example.projectguild.ui.components.RewardCelebrationDialog
import com.example.projectguild.ui.guild.GuildScreen
import com.example.projectguild.ui.guild.QuestDetailDialog
import com.example.projectguild.ui.home.HomeScreen
import com.example.projectguild.ui.math.MathTrainingScreen
import com.example.projectguild.ui.math.MathViewModel
import com.example.projectguild.ui.navigation.GuildScreenDestination
import com.example.projectguild.ui.profile.ProfileScreen

@Composable
fun GuildMainApp(
    guildViewModel: GuildViewModel = viewModel(
        factory = GuildViewModel.Factory(GuildApplication.instance.repository)
    ),
    mathViewModel: MathViewModel = viewModel(
        factory = MathViewModel.Factory(GuildApplication.instance.repository)
    )
) {
    var currentDestination by remember { mutableStateOf(GuildScreenDestination.HOME) }

    val selectedQuest by guildViewModel.selectedQuestForDetail.collectAsState()
    val celebration by guildViewModel.rewardCelebration.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                GuildScreenDestination.entries.forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = {
                            Text(
                                text = destination.label,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GuildPrimary,
                            selectedTextColor = GuildPrimary,
                            indicatorColor = GuildPrimary.copy(alpha = 0.12f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(GuildBackground)

        when (currentDestination) {
            GuildScreenDestination.HOME -> {
                HomeScreen(
                    viewModel = guildViewModel,
                    onNavigateToMath = { currentDestination = GuildScreenDestination.MATH },
                    onNavigateToGuild = { currentDestination = GuildScreenDestination.GUILD },
                    onOpenQuest = { questId -> guildViewModel.selectQuest(questId) },
                    modifier = modifier
                )
            }
            GuildScreenDestination.GUILD -> {
                GuildScreen(
                    viewModel = guildViewModel,
                    onOpenQuest = { questId -> guildViewModel.selectQuest(questId) },
                    modifier = modifier
                )
            }
            GuildScreenDestination.MATH -> {
                MathTrainingScreen(
                    viewModel = mathViewModel,
                    modifier = modifier
                )
            }
            GuildScreenDestination.PROFILE -> {
                ProfileScreen(
                    viewModel = guildViewModel,
                    modifier = modifier
                )
            }
        }

        // Quest Detail & Mock Approval Dialog
        if (selectedQuest != null) {
            QuestDetailDialog(
                quest = selectedQuest,
                onDismiss = { guildViewModel.dismissQuestDetail() },
                onAcceptQuest = { id -> guildViewModel.acceptQuest(id) },
                onSubmitProof = { id -> guildViewModel.submitQuestProof(id) },
                onSimulateParentApproval = { id -> guildViewModel.simulateParentApproval(id) },
                onSimulateRejection = { id -> guildViewModel.simulateRejection(id) },
                onRetryQuest = { id -> guildViewModel.retryQuest(id) }
            )
        }

        // Reward Celebration Fanfare Overlay
        RewardCelebrationDialog(
            celebration = celebration,
            onDismiss = { guildViewModel.dismissCelebration() }
        )
    }
}
