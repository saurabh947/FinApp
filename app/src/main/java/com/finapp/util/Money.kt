package com.finapp.util

import androidx.compose.runtime.staticCompositionLocalOf
import com.finapp.data.Currency
import java.text.NumberFormat

/** The active display currency, provided at the app root from Settings. */
val LocalCurrency = staticCompositionLocalOf { Currency.USD }

/** Formats an amount in the given currency, e.g. 1234.5 -> "$1,234.50" or "£1,234.50". */
fun formatCurrency(amount: Double, currency: Currency): String =
    NumberFormat.getCurrencyInstance(currency.locale).format(amount)
