package com.finapp.data

import com.finapp.util.AppLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

/**
 * In-memory store of transactions, seeded with sample data. Exposes the list as an
 * observable [StateFlow] so the UI updates automatically when a transaction is added.
 *
 * This is a singleton so every screen/ViewModel shares the same data. Swap this for a
 * Room-backed implementation later without touching the UI.
 */
object TransactionRepository {

    private val _transactions = MutableStateFlow(SeedData.transactions)
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private var nextId: Long = (SeedData.transactions.maxOfOrNull { it.id } ?: 0L) + 1

    fun addTransaction(
        title: String,
        amount: Double,
        category: Category,
        date: LocalDate,
        type: TransactionType
    ) {
        val newTransaction = Transaction(
            id = nextId++,
            title = title,
            category = category,
            amount = amount,
            date = date,
            type = type
        )
        _transactions.value = _transactions.value + newTransaction
        AppLog.event("Transaction added: ${type.name} \"$title\" $amount")
    }
}
