package com.finapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.data.Category
import com.finapp.data.Transaction
import com.finapp.data.TransactionType
import com.finapp.ui.theme.ExpenseContainer
import com.finapp.ui.theme.ExpenseRed
import com.finapp.ui.theme.IncomeContainer
import com.finapp.ui.theme.IncomeGreen
import com.finapp.ui.theme.LocalAccentColors
import com.finapp.util.AppLog
import com.finapp.util.LocalCurrency
import com.finapp.util.formatCurrency
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddTransaction: (TransactionType) -> Unit,
    onOpenSettings: () -> Unit,
    onTransactionClick: (Long) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val isIncome = selectedTab == 0

    LaunchedEffect(Unit) { AppLog.screen("Dashboard") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FinApp", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onAddTransaction(
                        if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE
                    )
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(if (isIncome) "Add Income" else "Add Expense") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            SummarySection(
                totalIncome = state.totalIncome,
                totalExpenses = state.totalExpenses,
                balance = state.balance
            )
            TransactionTabs(
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
                income = state.income,
                expenses = state.expenses,
                onTransactionClick = onTransactionClick
            )
        }
    }
}

@Composable
private fun SummarySection(
    totalIncome: Double,
    totalExpenses: Double,
    balance: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                label = "Total Income",
                amount = totalIncome,
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                container = IncomeContainer,
                accent = IncomeGreen
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                label = "Total Expenses",
                amount = totalExpenses,
                icon = Icons.AutoMirrored.Filled.TrendingDown,
                container = ExpenseContainer,
                accent = ExpenseRed
            )
        }

        Spacer(Modifier.height(12.dp))
        BalanceBar(balance = balance)
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    label: String,
    amount: Double,
    icon: ImageVector,
    container: Color,
    accent: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = accent
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = formatCurrency(amount, LocalCurrency.current),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BalanceBar(balance: Double) {
    val accentColors = LocalAccentColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Net Balance",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = formatCurrency(balance, LocalCurrency.current),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (balance >= 0) accentColors.income else accentColors.expense
            )
        }
    }
}

@Composable
private fun TransactionTabs(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    income: List<Transaction>,
    expenses: List<Transaction>,
    onTransactionClick: (Long) -> Unit
) {
    val tabs = listOf("Income", "Expenses")

    Column(Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onSelectTab(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        val isIncome = selectedTab == 0
        val items = if (isIncome) income else expenses
        TransactionList(items = items, isIncome = isIncome, onTransactionClick = onTransactionClick)
    }
}

@Composable
private fun TransactionList(items: List<Transaction>, isIncome: Boolean, onTransactionClick: (Long) -> Unit) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No transactions yet",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // extra bottom padding so the last row clears the floating action button
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items, key = { it.id }) { transaction ->
            TransactionRow(
                transaction = transaction,
                isIncome = isIncome,
                onClick = { onTransactionClick(transaction.id) }
            )
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction, isIncome: Boolean, onClick: () -> Unit) {
    val accentColors = LocalAccentColors.current
    val accent = if (isIncome) accentColors.income else accentColors.expense
    val sign = if (isIncome) "+" else "-"

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon(transaction.category),
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${transaction.category.label}  ·  ${transaction.date.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "$sign${formatCurrency(transaction.amount, LocalCurrency.current)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accent
            )
        }
    }
}

fun categoryIcon(category: Category): ImageVector = when (category) {
    Category.SALARY -> Icons.Filled.Payments
    Category.FREELANCE -> Icons.Filled.Work
    Category.INVESTMENTS -> Icons.AutoMirrored.Filled.ShowChart
    Category.GIFTS -> Icons.Filled.CardGiftcard
    Category.REFUNDS -> Icons.Filled.Replay
    Category.RENT -> Icons.Filled.Home
    Category.GROCERIES -> Icons.Filled.ShoppingCart
    Category.UTILITIES -> Icons.Filled.Bolt
    Category.DINING -> Icons.Filled.Restaurant
    Category.TRANSPORT -> Icons.Filled.DirectionsCar
    Category.SHOPPING -> Icons.Filled.ShoppingBag
    Category.ENTERTAINMENT -> Icons.Filled.Movie
    Category.HEALTH -> Icons.Filled.FavoriteBorder
    Category.SUBSCRIPTIONS -> Icons.Filled.Subscriptions
}
