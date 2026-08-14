package com.finapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** Income/expense accent colors that adapt to the active (light/dark) theme. */
data class AccentColors(val income: Color, val expense: Color)

val LocalAccentColors = staticCompositionLocalOf { AccentColors(IncomeGreen, ExpenseRed) }

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Color(0xFF052E0A),
    secondary = Secondary,
    background = LightBackground,
    onBackground = Color(0xFF1A1C1A),
    surface = LightSurface,
    onSurface = Color(0xFF1A1C1A),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF49544B)
)

private val DarkColors = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Secondary,
    onPrimaryContainer = Color(0xFFD7F3D9),
    secondary = PrimaryContainer,
    background = DarkBackground,
    onBackground = Color(0xFFECECEC),
    surface = DarkSurface,
    onSurface = Color(0xFFECECEC),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC2C8C2)
)

/**
 * App theme. [darkTheme] is driven by the user's Settings choice (not the system),
 * so the user can force light or dark from within the app.
 */
@Composable
fun FinAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val accentColors =
        if (darkTheme) AccentColors(IncomeGreenDark, ExpenseRedDark)
        else AccentColors(IncomeGreen, ExpenseRed)

    // Keep the status bar icons readable against the current background.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalAccentColors provides accentColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
