package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect

fun <T> sendToChannel(
    channel: ReceiveChannel<T>,
    flow: MutableStateFlow<T>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    GlobalScope.async {
        while (true) {
            val result = channel.receive()
            CoroutineScope(dispatcher).launch {
                flow.emit(result)
            }
        }
    }
}

fun <T> sendToChannel(
    channel: ReceiveChannel<T>,
    flow: MutableSharedFlow<T>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    GlobalScope.async {
        while (true) {
            val result = channel.receive()
            CoroutineScope(dispatcher).launch {
                flow.emit(result)
            }
        }
    }
}

fun <T> sharedFlowToChannel(flow: SharedFlow<T>, channel: SendChannel<T>) {
    GlobalScope.async {
        flow.collect {
            channel.send(it)
        }
    }
}