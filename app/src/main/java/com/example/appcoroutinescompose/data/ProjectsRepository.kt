package com.example.appcoroutinescompose.data

import com.example.appcoroutinescompose.model.Project

interface ProjectsRepository {
    /**
     * Obtém uma página de projetos.
     * @param page 1-based
     * @param pageSize quantidade por página
     */
    suspend fun getProjects(page: Int, pageSize: Int): List<Project>
}