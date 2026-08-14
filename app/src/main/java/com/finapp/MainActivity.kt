package com.finapp

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.finapp.data.SettingsRepository
import com.finapp.data.ThemeMode
import com.finapp.ui.FinAppNavHost
import com.finapp.ui.theme.FinAppTheme
import com.finapp.util.LocalCurrency

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Launch (pre-Compose) window background, matched to the saved theme, set from code.
        val startDark = SettingsRepository.settings.value.theme == ThemeMode.DARK
        window.setBackgroundDrawable(
            ColorDrawable(if (startDark) 0xFF303030.toInt() else 0xFFFFFFFF.toInt())
        )
        enableEdgeToEdge()
        setContent {
            val settings by SettingsRepository.settings.collectAsState()
            FinAppTheme(darkTheme = settings.theme == ThemeMode.DARK) {
                CompositionLocalProvider(LocalCurrency provides settings.currency) {
                    FinAppNavHost()
                }
            }
        }
    }
}
