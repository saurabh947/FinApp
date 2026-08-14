package com.finapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.data.Transaction
import com.finapp.data.TransactionRepository
import com.finapp.data.TransactionType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Everything the dashboard needs to render, derived from the transaction list. */
data class DashboardUiState(
    val income: List<Transaction>,
    val expenses: List<Transaction>,
    val totalIncome: Double,
    val totalExpenses: Double
) {
    val balance: Double get() = totalIncome - totalExpenses
}

class DashboardViewModel : ViewModel() {

    val uiState: StateFlow<DashboardUiState> =
        TransactionRepository.transactions
            .map { buildState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = buildState(TransactionRepository.transactions.value)
            )

    private fun buildState(all: List<Transaction>): DashboardUiState {
        val income = all
            .filter { it.type == TransactionType.INCOME }
            .sortedByDescending { it.date }
        val expenses = all
            .filter { it.type == TransactionType.EXPENSE }
            .sortedByDescending { it.date }

        return DashboardUiState(
            income = income,
            expenses = expenses,
            totalIncome = income.sumOf { it.amount },
            totalExpenses = expenses.sumOf { it.amount }
        )
    }
}
