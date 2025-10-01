package com.example.appcoroutinescompose.ui.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.appcoroutinescompose.model.Project
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    onOpenDetail: (Int) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Projects · Portfólio") },
                navigationIcon = {
                    Text(
                        "⟵",
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(12.dp)
                    )
                },
                actions = {
                    TextButton(onClick = { viewModel.refresh() }) {
                        Text("Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.projects.isEmpty() && state.isLoading) {
                // primeiro carregamento
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Carregando projetos...")
                }
                return@Box
            }

            val listState = rememberLazyListState()

            LaunchedEffect(listState) {
                // detecta quando chegar perto do final para carregar próxima página
                snapshotFlow {
                    val layout = listState.layoutInfo
                    val total = layout.totalItemsCount
                    val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: 0
                    total to lastVisible
                }
                    .distinctUntilChanged()
                    .filter { (_, lastVisible) ->
                        // se falta <3 itens para o fim -> carregar próximo
                        val total = listState.layoutInfo.totalItemsCount
                        total > 0 && lastVisible >= total - 3
                    }
                    .collect {
                        viewModel.loadNextPage()
                    }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                if (state.error != null) {
                    // Barra de erro simples
                    Snackbar(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Erro: ${state.error}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                            TextButton(onClick = { viewModel.retryAfterError() }) {
                                Text("Tentar de novo")
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(state.projects, key = { it.id }) { project ->
                        ProjectCard(
                            item = project,
                            isFav = state.favorites.contains(project.id),
                            onToggleFav = { viewModel.toggleFavorite(project.id) },
                            onClick = { onOpenDetail(project.id) }
                        )
                    }

                    item {
                        when {
                            state.isLoading -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                                }
                            }
                            state.endReached -> {
                                Text(
                                    "Fim da lista",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(
    item: Project,
    isFav: Boolean,
    onToggleFav: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Placeholder de imagem (substituir por Coil AsyncImage se quiser)
            val bg = remember(item.id) { pickColor(item.id) }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Text(item.title.firstOrNull()?.toString() ?: "?", color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.description, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                TextButton(onClick = { onToggleFav() }) {
                    Text(if (isFav) "♥" else "♡")
                }
            }
        }
    }
}

fun pickColor(id: Int) : Color {
    // escolha de cor baseada no id (simples hashing)
    val colors = listOf(
        Color(0xFFB39DDB),
        Color(0xFF90CAF9),
        Color(0xFFFFCC80),
        Color(0xFFA5D6A7),
        Color(0xFFFFAB91)
    )
    return colors[id % colors.size]
}