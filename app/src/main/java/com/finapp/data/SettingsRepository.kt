package com.finapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/** Display currency. [locale] drives the symbol + number formatting via NumberFormat. */
enum class Currency(val label: String, val symbol: String, val locale: Locale) {
    USD("USD", "\$", Locale.US),
    GBP("GBP", "£", Locale.UK)
}

/** Light or dark appearance. */
enum class ThemeMode(val label: String) {
    LIGHT("Light"),
    DARK("Dark")
}

data class AppSettings(
    val currency: Currency = Currency.USD,
    val theme: ThemeMode = ThemeMode.LIGHT
)

/**
 * App-wide settings shared across every screen (singleton). Exposed as a [StateFlow]
 * so the UI re-renders immediately when currency or theme changes.
 */
object SettingsRepository {
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    fun setCurrency(currency: Currency) {
        _settings.value = _settings.value.copy(currency = currency)
    }

    fun setTheme(theme: ThemeMode) {
        _settings.value = _settings.value.copy(theme = theme)
    }
}
