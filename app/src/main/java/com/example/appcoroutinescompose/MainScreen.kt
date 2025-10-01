package com.example.appcoroutinescompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Coroutines Playground") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status e resultado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Status: ${uiState.status}", style = MaterialTheme.typography.bodyLarge)
                    if (uiState.quickResult.isNotEmpty()) {
                        Text("Quick Result: ${uiState.quickResult}")
                    }
                    if (uiState.concurrentResult.isNotEmpty()) {
                        Text("Concurrent Result: ${uiState.concurrentResult}")
                    }
                    if (uiState.flowLast.isNotEmpty()) {
                        Text("Flow Last: ${uiState.flowLast}")
                    }
                }
            }

            // Botões de ações
            val actions = listOf(
                "Launch simples" to { viewModel.launchSimpleCoroutine() },
                "WithContext" to { viewModel.launchWithContextAndSuspend() },
                "Async Concurrent" to { viewModel.launchConcurrent() },
                "Start Flow" to { viewModel.startFlow() },
                "Collect Flow Once" to { viewModel.collectFlowOnce() },
                "Start Channel Producer" to { viewModel.startChannelProducer() },
                "Consume Channel" to { viewModel.consumeChannel() },
                "Cancel All" to { viewModel.cancelAll() }
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { (label, action) ->
                    Button(
                        onClick = action,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(label)
                    }
                }
            }

            // Logs
            Text("Logs:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(uiState.logs) { log ->
                    Text(log, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}