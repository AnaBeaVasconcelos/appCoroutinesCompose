package com.example.appcoroutinescompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow

data class PlaygroundState(
    val status: String = "idle"
)

class CoroutinesPlaygroundViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlaygroundState())
    val uiState: StateFlow<PlaygroundState> = _uiState.asStateFlow()

    // simple logs as a SharedFlow or StateFlow; we'll use MutableStateFlow with a list
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs = _logs.asStateFlow()

    private val jobList = mutableListOf<Job>()

    private fun log(text: String) {
        _logs.value = _logs.value + "${Thread.currentThread().name}: $text"
    }

    fun launchSimple() {
        val job = viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = PlaygroundState(status = "launchSimple running")
            log("launchSimple started")
            repeat(3) {
                delay(400)
                log("launchSimple step $it")
            }
            log("launchSimple finished")
            _uiState.value = PlaygroundState(status = "idle")
        }
        jobList.add(job)
    }

    fun launchWithContext() {
        val job = viewModelScope.launch {
            _uiState.value = PlaygroundState(status = "withContext running")
            log("withContext switching to IO")
            val result = withContext(Dispatchers.IO) {
                delay(600)
                "io-result"
            }
            log("withContext got: $result")
            _uiState.value = PlaygroundState(status = "idle")
        }
        jobList.add(job)
    }

    fun launchAsyncAwait() {
        val job = viewModelScope.launch {
            _uiState.value = PlaygroundState(status = "async/await running")
            log("async/await starting")
            val a = async(Dispatchers.Default) {
                delay(500); "A"
            }
            val b = async(Dispatchers.Default) {
                delay(300); "B"
            }
            val result = "${a.await()}+${b.await()}"
            log("async/await result: $result")
            _uiState.value = PlaygroundState(status = "idle")
        }
        jobList.add(job)
    }

    fun startFlowDemo() {
        val job = viewModelScope.launch {
            _uiState.value = PlaygroundState(status = "flow demo running")
            log("flow demo start")
            val flow = kotlinx.coroutines.flow.flow {
                for (i in 1..5) {
                    delay(200)
                    emit("f-$i")
                }
            }
            flow.collect { v ->
                log("flow emitted $v")
            }
            log("flow demo finished")
            _uiState.value = PlaygroundState(status = "idle")
        }
        jobList.add(job)
    }

    fun cancelAll() {
        jobList.forEach { it.cancel(CancellationException("user cancel")) }
        jobList.clear()
        _uiState.value = PlaygroundState(status = "cancelled")
        log("all cancelled")
    }

    override fun onCleared() {
        super.onCleared()
        jobList.forEach { it.cancel() }
    }
}