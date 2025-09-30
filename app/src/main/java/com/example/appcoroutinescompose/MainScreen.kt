package com.example.appcoroutinescompose

import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: MainViewModel) {
    val state by vm.uiState.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Coroutines Playground") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Status: ${state.status}")
            Text("Resultado rápido: ${state.quickResult}")
            Text("Resultado concorrente: ${state.concurrentResult}")
            Text("Flow último: ${state.flowLast}")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.launchSimpleCoroutine() }) { Text("Launch simples") }
                Button(onClick = { vm.launchWithContextAndSuspend() }) { Text("withContext (IO)") }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.launchConcurrent() }) { Text("async / await") }
                Button(onClick = { vm.cancelAll() }) { Text("Cancelar jobs") }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.startFlow() }) { Text("Start Flow") }
                Button(onClick = { vm.collectFlowOnce() }) { Text("Collect once") }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.startChannelProducer() }) { Text("Iniciar Channel") }
                Button(onClick = { vm.consumeChannel() }) { Text("Consumir Channel") }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            Text("Logs:")
            for (line in state.logs.takeLast(6)) {
                Text(line)
            }

        }
    }
}