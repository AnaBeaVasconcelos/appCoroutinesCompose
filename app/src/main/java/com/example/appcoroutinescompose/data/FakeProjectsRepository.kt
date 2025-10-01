package com.example.appcoroutinescompose.data

import com.example.appcoroutinescompose.model.Project
import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeProjectsRepository : ProjectsRepository {

    private val totalPages = 5

    override suspend fun getProjects(page: Int, pageSize: Int): List<Project> {
        // Simula latência de rede
        delay(900)

        // Simula erro aleatório (20% de chance)
        if (Random.nextInt(0, 10) < 2) {
            throw RuntimeException("Erro de rede simulado")
        }

        if (page > totalPages) return emptyList()

        val start = (page - 1) * pageSize
        return (start until start + pageSize).map { index ->
            val id = index + 1
            Project(
                id = id,
                title = "Project #$id",
                description = "Descrição curta do projeto $id — aqui você descreve stack, objetivo e resultado.",
                imageUrl = null // pode preencher com URL real para testar Coil
            )
        }
    }
}