package com.example.appcoroutinescompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val channel = Channel<String>()

    private val coldFlow = flow {
        for (i in 1..5) {
            delay(300)
            emit("item-$i")
        }
    }

    private val _toast = MutableSharedFlow<String>(replay = 1)
    val toast = _toast.asSharedFlow()

    private val _toastState = MutableStateFlow("")
    val toastState = _toastState.asStateFlow()

    private val jobList = mutableListOf<Job>()

    private fun log(msg: String) {
        val prev = _uiState.value
        _uiState.value = prev.copy(logs = prev.logs + "[${Thread.currentThread().name}] $msg")
    }

    /**
     * 1) Launch simples: mostra uso de viewModelScope.launch, Dispatchers e cancelamento
     */
    fun launchSimpleCoroutine() {
        _uiState.update { it.copy(status = "launching simples") }

        val job = viewModelScope.launch(Dispatchers.Default + CoroutineName("simple-job")) {
            log("Started simple coroutine")
            // Delay é cooperativo: permite cancelamento
            repeat(5) { i ->
                delay(400)
                log("step $i")
            }
            _uiState.update { it.copy(quickResult = "done simples", status = "done simples") }
            log("Finished simple coroutine")
        }

        jobList.add(job)
    }

    /**
     * 2) withContext: mudar Dispatcher para IO e chamar código suspenso
     */
    fun launchWithContextAndSuspend() {
        _uiState.update { it.copy(status = "withContext") }

        val job = viewModelScope.launch {
            log("About to run IO work")
            val result = withContext(Dispatchers.IO) { // troca segura de contexto
                heavyFakeIoWork() // suspending
            }
            _uiState.update { it.copy(quickResult = result, status = "withContext done") }
            log("withContext result: $result")
        }

        jobList.add(job)
    }

    private suspend fun heavyFakeIoWork(): String {
        delay(800)
        return "IO OK"
    }

    /**
     * 3) Concurrent computing with async/await and structured concurrency
     */
    fun launchConcurrent() {
        _uiState.update { it.copy(status = "concurrent starting") }

        val job = viewModelScope.launch {
            // async started concurrently and structured: if this scope cancels, children cancel
            val a = async(Dispatchers.Default) { computeA() }
            val b = async(Dispatchers.Default) { computeB() }

            try {
                val combined = "${a.await()} + ${b.await()}"
                _uiState.update { it.copy(concurrentResult = combined, status = "concurrent done") }
                log("Concurrent result: $combined")
            } catch (e: CancellationException) {
                log("concurrent cancelled")
                throw e
            } catch (e: Exception) {
                log("concurrent failed: ${e.message}")
            }
        }

        jobList.add(job)
    }

    private suspend fun computeA(): String {
        delay(600)
        return "A"
    }

    private suspend fun computeB(): String {
        delay(400)
        return "B"
    }

    /**
     * 4) Demonstrate cancellation
     */
    fun cancelAll() {
        jobList.forEach { it.cancel(CancellationException("User requested cancel")) }
        jobList.clear()
        _uiState.update { it.copy(status = "cancelled") }
        log("All jobs cancelled")
    }

    /**
     * 5) Cold Flow collection: collect on viewModelScope or lifecycleScope
     */
    fun startFlow() {
        _uiState.update { it.copy(status = "flow started") }

        val job = viewModelScope.launch {
            log("Collecting cold flow")
            coldFlow.collect { item ->
                log("Flow emitted: $item")
                _uiState.update { it.copy(flowLast = item) }
            }
            log("Flow collection complete")
        }

        jobList.add(job)
    }

    fun collectFlowOnce() {
        // use first() to collect only first emission
        val job = viewModelScope.launch {
            val first = coldFlow.first()
            log("Flow first: $first")
            _uiState.update { it.copy(flowLast = "first: $first") }
        }
        jobList.add(job)
    }

    /**
     * 6) Channels: producer/consumer model
     */
    fun startChannelProducer() {
        val job = viewModelScope.launch {
            repeat(5) {
                delay(300)
                val value = "channel-$it"
                channel.send(value)
                log("Sent to channel: $value")
            }
            channel.close()
            log("Channel closed")
        }
        jobList.add(job)
    }

    fun consumeChannel() {
        val job = viewModelScope.launch {
            for (v in channel) {
                log("Received from channel: $v")
                _uiState.update { it.copy(flowLast = v) }
            }
            log("Finished consuming channel")
        }
        jobList.add(job)
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModelCancelled -> cancels viewModelScope
        log("ViewModel cleared")
    }

}
