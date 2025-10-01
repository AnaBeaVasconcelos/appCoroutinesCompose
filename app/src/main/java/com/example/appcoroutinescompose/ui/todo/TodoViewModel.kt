package com.example.appcoroutinescompose.ui.todo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TodoItem(val id: Int, val text: String)

class TodoViewModel : ViewModel() {
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos = _todos.asStateFlow()

    private var counter = 0

    fun addTodo(text: String) {
        if (text.isNotBlank()) {
            counter++
            _todos.value = _todos.value + TodoItem(counter, text)
        }
    }

    fun removeTodo(id: Int) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }
}