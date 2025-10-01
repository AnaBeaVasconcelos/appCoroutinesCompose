package com.example.appcoroutinescompose.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appcoroutinescompose.screens.CoroutinesPlaygroundScreen
import com.example.appcoroutinescompose.screens.HomeScreen
import com.example.appcoroutinescompose.viewmodel.CoroutinesPlaygroundViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onOpenPlayground = { navController.navigate("playground") })
        }
        composable("playground") {
            val vm: CoroutinesPlaygroundViewModel = viewModel()
            CoroutinesPlaygroundScreen(vm = vm, onBack = { navController.popBackStack() })
        }
    }
}