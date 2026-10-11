package com.example.projectguild.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.projectguild.GuildApplication
import com.example.projectguild.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppLockAccessibilityService : AccessibilityService() {

    private var lastInterceptedPackage: String? = null
    private var lastInterceptedTime: Long = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val packageName = event.packageName?.toString() ?: return

        // Ignore our own package and critical system settings/system UI
        val myPackage = applicationContext.packageName
        if (packageName == myPackage ||
            packageName == "com.android.systemui" ||
            packageName == "com.android.settings" ||
            packageName.contains("launcher", ignoreCase = true)
        ) {
            return
        }

        val repository = runCatching { GuildApplication.instance.repository }.getOrNull() ?: return
        val isLocked = repository.isAppLocked(packageName)

        if (!isLocked) {
            return
        }

        val remainingMinutes = repository.playerProfile.value.screenTimeMinutes

        // If child still has remaining screen time (> 0), allow the app to run normally!
        if (remainingMinutes > 0) {
            Log.d(TAG, "Package $packageName is locked, but adventurer has $remainingMinutes min remaining. Allowing access.")
            return
        }

        // Child has 0 screen time! Intercept and redirect to Project Guild
        val now = System.currentTimeMillis()
        if (packageName == lastInterceptedPackage && (now - lastInterceptedTime) < DEBOUNCE_MS) {
            return
        }

        lastInterceptedPackage = packageName
        lastInterceptedTime = now
        Log.w(TAG, "Screen time is 0! Intercepting locked package: $packageName")

        _lastInterceptedAppEvent.value = InterceptedAppEvent(
            packageName = packageName,
            timestamp = now
        )

        val interceptIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(EXTRA_LOCKED_PACKAGE, packageName)
            putExtra(EXTRA_TIMESTAMP, now)
        }
        startActivity(interceptIntent)
    }

    override fun onInterrupt() {
        Log.i(TAG, "AppLockAccessibilityService interrupted")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        _isServiceActive.value = true
        Log.i(TAG, "AppLockAccessibilityService connected and active")
    }

    override fun onDestroy() {
        super.onDestroy()
        _isServiceActive.value = false
        Log.i(TAG, "AppLockAccessibilityService destroyed")
    }

    companion object {
        private const val TAG = "AppLockService"
        private const val DEBOUNCE_MS = 1500L

        const val EXTRA_LOCKED_PACKAGE = "extra_locked_package"
        const val EXTRA_TIMESTAMP = "extra_timestamp"

        private val _isServiceActive = MutableStateFlow(false)
        val isServiceActive: StateFlow<Boolean> = _isServiceActive.asStateFlow()

        private val _lastInterceptedAppEvent = MutableStateFlow<InterceptedAppEvent?>(null)
        val lastInterceptedAppEvent: StateFlow<InterceptedAppEvent?> = _lastInterceptedAppEvent.asStateFlow()

        fun clearInterceptEvent() {
            _lastInterceptedAppEvent.value = null
        }
    }
}

data class InterceptedAppEvent(
    val packageName: String,
    val timestamp: Long
)
