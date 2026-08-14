package com.finapp.data

import com.finapp.data.TransactionType.EXPENSE
import com.finapp.data.TransactionType.INCOME
import java.time.LocalDate

/** Whether a transaction adds to or subtracts from the user's balance. */
enum class TransactionType { INCOME, EXPENSE }

/**
 * A spending/earning category. [label] is what we show in the UI and [type]
 * determines whether it belongs to income or expenses.
 */
enum class Category(val label: String, val type: TransactionType) {
    // Income
    SALARY("Salary", INCOME),
    FREELANCE("Freelance", INCOME),
    INVESTMENTS("Investments", INCOME),
    GIFTS("Gifts", INCOME),
    REFUNDS("Refunds", INCOME),

    // Expenses
    RENT("Rent", EXPENSE),
    GROCERIES("Groceries", EXPENSE),
    UTILITIES("Utilities", EXPENSE),
    DINING("Dining", EXPENSE),
    TRANSPORT("Transport", EXPENSE),
    SHOPPING("Shopping", EXPENSE),
    ENTERTAINMENT("Entertainment", EXPENSE),
    HEALTH("Health", EXPENSE),
    SUBSCRIPTIONS("Subscriptions", EXPENSE);

    companion object {
        /** Categories valid for a given transaction type. */
        fun forType(type: TransactionType): List<Category> = entries.filter { it.type == type }

        /** First category for a type — a sensible default selection. */
        fun firstForType(type: TransactionType): Category = entries.first { it.type == type }
    }
}

/**
 * A single money movement. [amount] is always stored as a positive value;
 * [type] determines whether it counts as income or an expense.
 */
data class Transaction(
    val id: Long,
    val title: String,
    val category: Category,
    val amount: Double,
    val date: LocalDate,
    val type: TransactionType
)
