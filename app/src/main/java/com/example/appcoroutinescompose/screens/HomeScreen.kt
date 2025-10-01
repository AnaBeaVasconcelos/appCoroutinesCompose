package com.example.appcoroutinescompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onOpenPlayground: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Coroutines Playground") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Card(modifier = Modifier.fillMaxWidth().clickable { onOpenPlayground() }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Coroutine Playground", style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Experimente launch, async/await, withContext, flows e channels.")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Text("Dica: toque na card para abrir o playground e ver logs em tempo real.")
            }
        }
    }
}