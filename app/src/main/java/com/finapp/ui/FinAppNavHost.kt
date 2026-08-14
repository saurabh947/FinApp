package com.finapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.finapp.data.TransactionType

private object Routes {
    const val DASHBOARD = "dashboard"
    const val ADD = "add"
    const val SETTINGS = "settings"
    const val ARG_TYPE = "type"

    fun addRoute(type: TransactionType) = "$ADD?$ARG_TYPE=${type.name}"
    const val ADD_PATTERN = "$ADD?$ARG_TYPE={$ARG_TYPE}"
}

@Composable
fun FinAppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onAddTransaction = { type -> navController.navigate(Routes.addRoute(type)) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(
            route = Routes.ADD_PATTERN,
            arguments = listOf(
                navArgument(Routes.ARG_TYPE) {
                    type = NavType.StringType
                    defaultValue = TransactionType.EXPENSE.name
                }
            )
        ) { backStackEntry ->
            val typeName = backStackEntry.arguments?.getString(Routes.ARG_TYPE)
                ?: TransactionType.EXPENSE.name
            val initialType = runCatching { TransactionType.valueOf(typeName) }
                .getOrDefault(TransactionType.EXPENSE)

            AddTransactionScreen(
                initialType = initialType,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
