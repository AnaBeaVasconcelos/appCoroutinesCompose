package com.example.appcoroutinescompose.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appcoroutinescompose.MainScreen
import com.example.appcoroutinescompose.MainViewModel
import com.example.appcoroutinescompose.ui.form.FormScreen
import com.example.appcoroutinescompose.ui.form.FormViewModel
import com.example.appcoroutinescompose.ui.todo.TodoScreen
import com.example.appcoroutinescompose.ui.todo.TodoViewModel

@Composable
fun AppNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController, startDestination = BottomNavItem.Coroutines.route) {
        composable(BottomNavItem.Coroutines.route) {
            val vm: MainViewModel = viewModel()
            MainScreen(vm)
        }
        composable(BottomNavItem.Todo.route) {
            val vm: TodoViewModel = viewModel()
            TodoScreen(vm)
        }
        composable(BottomNavItem.Form.route) {
            val vm: FormViewModel = viewModel()
            FormScreen(vm)
        }
    }
}