package com.example.projectguild.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectguild.GuildApplication
import com.example.projectguild.theme.GuildBackground
import com.example.projectguild.ui.coins.CoinsHomeScreen
import com.example.projectguild.ui.components.RewardCelebrationDialog
import com.example.projectguild.ui.guild.QuestDetailDialog
import com.example.projectguild.ui.math.MathTrainingScreen
import com.example.projectguild.ui.math.MathViewModel
import com.example.projectguild.ui.navigation.GuildScreenDestination
import com.example.projectguild.ui.portal.MainPortalScreen
import com.example.projectguild.ui.profile.ProfileScreen
import com.example.projectguild.ui.screentime.AppLockerScreen
import com.example.projectguild.ui.screentime.AppLockInterceptedDialog
import com.example.projectguild.ui.screentime.ScreenTimeHomeScreen

@Composable
fun GuildMainApp(
    guildViewModel: GuildViewModel = viewModel(
        factory = GuildViewModel.Factory(GuildApplication.instance.repository)
    ),
    mathViewModel: MathViewModel = viewModel(
        factory = MathViewModel.Factory(GuildApplication.instance.repository)
    )
) {
    var currentDestination by remember { mutableStateOf(GuildScreenDestination.PORTAL) }
    var previousDestination by remember { mutableStateOf(GuildScreenDestination.PORTAL) }

    fun navigateTo(dest: GuildScreenDestination) {
        previousDestination = currentDestination
        currentDestination = dest
    }

    val selectedQuest by guildViewModel.selectedQuestForDetail.collectAsState()
    val celebration by guildViewModel.rewardCelebration.collectAsState()
    val interceptedAppEvent by guildViewModel.interceptedAppEvent.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(GuildBackground)

        when (currentDestination) {
            GuildScreenDestination.PORTAL -> {
                MainPortalScreen(
                    viewModel = guildViewModel,
                    onNavigateToScreenTime = { navigateTo(GuildScreenDestination.SCREEN_TIME) },
                    onNavigateToCoins = { navigateTo(GuildScreenDestination.COINS) },
                    onNavigateToProfile = { navigateTo(GuildScreenDestination.PROFILE) },
                    modifier = modifier
                )
            }
            GuildScreenDestination.SCREEN_TIME -> {
                ScreenTimeHomeScreen(
                    viewModel = guildViewModel,
                    onBackToPortal = { currentDestination = GuildScreenDestination.PORTAL },
                    onNavigateToMath = { navigateTo(GuildScreenDestination.MATH) },
                    onNavigateToAppLocker = { navigateTo(GuildScreenDestination.APP_LOCKER) },
                    onNavigateToProfile = { navigateTo(GuildScreenDestination.PROFILE) },
                    modifier = modifier
                )
            }
            GuildScreenDestination.COINS -> {
                CoinsHomeScreen(
                    viewModel = guildViewModel,
                    onBackToPortal = { currentDestination = GuildScreenDestination.PORTAL },
                    onOpenQuest = { questId -> guildViewModel.selectQuest(questId) },
                    onNavigateToProfile = { navigateTo(GuildScreenDestination.PROFILE) },
                    modifier = modifier
                )
            }
            GuildScreenDestination.MATH -> {
                MathTrainingScreen(
                    viewModel = mathViewModel,
                    modifier = modifier,
                    onBackClick = { currentDestination = GuildScreenDestination.SCREEN_TIME }
                )
            }
            GuildScreenDestination.PROFILE -> {
                ProfileScreen(
                    viewModel = guildViewModel,
                    modifier = modifier,
                    onBackClick = { currentDestination = previousDestination }
                )
            }
            GuildScreenDestination.APP_LOCKER -> {
                AppLockerScreen(
                    viewModel = guildViewModel,
                    onBack = { currentDestination = GuildScreenDestination.SCREEN_TIME },
                    modifier = modifier
                )
            }
        }

        // Quest Detail & Mock Approval Dialog (accessible from Coins Home)
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

        // Screen Time Depleted Intercepted Dialog (triggered by Accessibility Service)
        AppLockInterceptedDialog(
            event = interceptedAppEvent,
            onDismiss = { guildViewModel.dismissInterceptedDialog() },
            onEarnScreenTime = {
                guildViewModel.dismissInterceptedDialog()
                navigateTo(GuildScreenDestination.MATH)
            }
        )
    }
}
