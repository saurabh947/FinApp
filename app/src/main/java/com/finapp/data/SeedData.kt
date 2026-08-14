package com.finapp.data

import com.finapp.data.TransactionType.EXPENSE
import com.finapp.data.TransactionType.INCOME
import java.time.LocalDate

/**
 * Hard-coded sample data so the dashboard has something to show.
 * Replace this with a real repository (DB / network) later.
 */
object SeedData {

    private val month: LocalDate = LocalDate.of(2026, 6, 1)
    private fun day(d: Int): LocalDate = month.withDayOfMonth(d)

    val transactions: List<Transaction> = listOf(
        // ---- Income ----
        Transaction(1, "Monthly Salary", Category.SALARY, 5200.00, day(1), INCOME),
        Transaction(2, "Website Project", Category.FREELANCE, 1450.00, day(5), INCOME),
        Transaction(3, "Dividend Payout", Category.INVESTMENTS, 318.40, day(6), INCOME),
        Transaction(4, "Birthday Gift", Category.GIFTS, 200.00, day(7), INCOME),
        Transaction(5, "Tax Refund", Category.REFUNDS, 540.50, day(8), INCOME),
        Transaction(6, "Logo Design Gig", Category.FREELANCE, 375.00, day(9), INCOME),

        // ---- Expenses ----
        Transaction(20, "Apartment Rent", Category.RENT, 1800.00, day(1), EXPENSE),
        Transaction(21, "Weekly Groceries", Category.GROCERIES, 142.30, day(2), EXPENSE),
        Transaction(22, "Electricity Bill", Category.UTILITIES, 96.75, day(3), EXPENSE),
        Transaction(23, "Dinner with Friends", Category.DINING, 64.20, day(3), EXPENSE),
        Transaction(24, "Metro Card Top-up", Category.TRANSPORT, 40.00, day(4), EXPENSE),
        Transaction(25, "New Sneakers", Category.SHOPPING, 119.99, day(5), EXPENSE),
        Transaction(26, "Movie Night", Category.ENTERTAINMENT, 28.50, day(6), EXPENSE),
        Transaction(27, "Pharmacy", Category.HEALTH, 33.10, day(7), EXPENSE),
        Transaction(28, "Streaming Plan", Category.SUBSCRIPTIONS, 15.99, day(7), EXPENSE),
        Transaction(29, "Coffee Runs", Category.DINING, 22.80, day(8), EXPENSE),
        Transaction(30, "Weekly Groceries", Category.GROCERIES, 88.40, day(9), EXPENSE),
        Transaction(31, "Gym Membership", Category.HEALTH, 45.00, day(9), EXPENSE),
        Transaction(32, "Internet Bill", Category.UTILITIES, 59.99, day(10), EXPENSE),
        Transaction(33, "Music Subscription", Category.SUBSCRIPTIONS, 10.99, day(10), EXPENSE)
    )
}
