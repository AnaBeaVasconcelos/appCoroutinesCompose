package com.example.appcoroutinescompose.ui.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appcoroutinescompose.model.Project

@Composable
fun ProjectDetailScreen(project: Project?, onBack: () -> Unit, onToggleFav: () -> Unit, isFav: Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.title ?: "Detalhe") },
                navigationIcon = {
                    Text(
                        "⟵",
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable{ onBack() }
                    )
                },
                actions = {
                    TextButton(onClick = onToggleFav) { Text(if (isFav) "♥ Favorito" else "♡ Favoritar") }
                }
            )
        }
    ) { padding ->
        if (project == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Projeto não encontrado")
            }
            return@Scaffold
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            // imagem placeholder grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(pickColor(project.id)),
                contentAlignment = Alignment.Center
            ) {
                Text(project.title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(project.description)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Detalhes técnicos (ex.: Kotlin, Compose, Coroutines, Retrofit, Room)")
        }
    }
}