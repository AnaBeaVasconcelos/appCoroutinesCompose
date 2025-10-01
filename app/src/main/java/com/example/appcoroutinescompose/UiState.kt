package com.example.appcoroutinescompose

data class UiState(
    val status: String = "",
    val quickResult: String = "",
    val concurrentResult: String = "",
    val flowLast: String = "",
    val logs: List<String> = emptyList()
)