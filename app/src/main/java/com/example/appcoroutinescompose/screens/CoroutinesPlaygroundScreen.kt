package com.example.appcoroutinescompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appcoroutinescompose.viewmodel.CoroutinesPlaygroundViewModel

@Composable
fun CoroutinesPlaygroundScreen(vm: CoroutinesPlaygroundViewModel, onBack: () -> Unit) {
    val uiState by vm.uiState.collectAsState()
    val logs by vm.logs.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Playground") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Status: ${uiState.status}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.launchSimple() }) { Text("launch") }
                Button(onClick = { vm.launchWithContext() }) { Text("withContext") }
                Button(onClick = { vm.launchAsyncAwait() }) { Text("async/await") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.startFlowDemo() }) { Text("start Flow") }
                Button(onClick = { vm.cancelAll() }) { Text("cancel") }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Logs:", style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)) {
                for (line in logs.reversed()) {
                    Text(line)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }


}