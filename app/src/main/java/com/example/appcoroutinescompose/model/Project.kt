package com.example.appcoroutinescompose.model

data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String? = null // placeholder – pode ser usado por Coil depois
)