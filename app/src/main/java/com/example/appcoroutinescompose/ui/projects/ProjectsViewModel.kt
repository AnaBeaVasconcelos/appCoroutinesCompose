package com.example.appcoroutinescompose.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcoroutinescompose.data.FakeProjectsRepository
import com.example.appcoroutinescompose.data.ProjectsRepository
import com.example.appcoroutinescompose.model.Project
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProjectsUiState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val endReached: Boolean = false,
    val favorites: Set<Int> = emptySet()
)

class ProjectsViewModel(
    private val repo: ProjectsRepository = FakeProjectsRepository(), // injetar prod repo depois
    private val pageSize: Int = 10
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadNextPage() // carrega primeira página
    }

    fun refresh() {
        if (_uiState.value.isRefreshing) return
        _uiState.value = _uiState.value.copy(isRefreshing = true, error = null, page = 1, endReached = false)
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try {
                val items = repo.getProjects(page = 1, pageSize = pageSize)
                _uiState.value = _uiState.value.copy(
                    projects = items,
                    page = 1,
                    endReached = items.isEmpty(),
                    error = null
                )
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message ?: "Erro desconhecido")
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.endReached) return

        val nextPage = state.page + if (state.projects.isEmpty()) 0 else 1

        _uiState.value = state.copy(isLoading = true, error = null)

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try {
                val items = repo.getProjects(page = nextPage, pageSize = pageSize)
                val newList = if (nextPage == 1) items else state.projects + items
                _uiState.value = _uiState.value.copy(
                    projects = newList,
                    page = nextPage,
                    endReached = items.isEmpty()
                )
            } catch (t: Throwable) {
                // mantém lista atual e mostra erro
                _uiState.value = _uiState.value.copy(error = t.message ?: "Erro desconhecido")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun toggleFavorite(projectId: Int) {
        val cur = _uiState.value.favorites.toMutableSet()
        if (cur.contains(projectId)) cur.remove(projectId) else cur.add(projectId)
        _uiState.value = _uiState.value.copy(favorites = cur)
        // Aqui você poderia salvar em DataStore/Room
    }

    fun retryAfterError() {
        // Simplesmente tenta carregar próxima página de novo
        if (_uiState.value.projects.isEmpty()) {
            // se lista vazia, tenta refresh
            refresh()
        } else {
            loadNextPage()
        }
    }
}