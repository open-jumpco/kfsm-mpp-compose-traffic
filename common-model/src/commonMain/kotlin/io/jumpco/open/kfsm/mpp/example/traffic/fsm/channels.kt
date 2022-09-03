package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
fun <T> channelToStateFlow(
    name: String,
    channel: ReceiveChannel<T>,
    flow: MutableStateFlow<T>,
    uiCoroutineScope: CoroutineScope,
    coroutineScope: CoroutineScope
) {
    coroutineScope.async {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            uiCoroutineScope.launch {
                flow.emit(result)
                logger.info { "emitted:$name:$result" }
            }
        }
    }
}

fun <T> channelToStateFlow(
    name: String,
    channel: ReceiveChannel<T>,
    flow: MutableStateFlow<T>,
    uiCoroutineScope: CoroutineScope,
    coroutineScope: CoroutineScope,
    action: suspend (T) -> Unit
) {
    coroutineScope.async {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            uiCoroutineScope.launch {
                flow.emit(result)
                logger.info { "emitted:$name:$result" }
                action.invoke(result)
            }
        }
    }
}

fun <T> channelToSharedFlow(
    name: String,
    channel: ReceiveChannel<T>,
    flow: MutableSharedFlow<T>,
    uiCoroutineScope: CoroutineScope,
    coroutineScope: CoroutineScope
) {
    coroutineScope.async {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            uiCoroutineScope.launch {
                flow.emit(result)
            }
            logger.info { "emitted:$name:$result" }
        }
    }
}

fun <T> channelToChannel(
    name: String,
    receiveChannel: ReceiveChannel<T>,
    sendChannel: SendChannel<T>,
    uiCoroutineScope: CoroutineScope,
    coroutineScope: CoroutineScope
) {
    coroutineScope.async {
        while (true) {
            val result = receiveChannel.receive()
            logger.info { "received:$name:$result" }
            logger.info { "sending:$name:$result" }
            uiCoroutineScope.launch {
                sendChannel.send(result)
            }
            logger.info { "sent:$name:$result" }
        }
    }
}

fun <T> channelToSharedFlow(
    name: String,
    channel: ReceiveChannel<T>,
    flow: MutableSharedFlow<T>,
    uiCoroutineScope: CoroutineScope,
    coroutineScope: CoroutineScope,
    action: suspend (T) -> Unit
) {
    coroutineScope.async {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            uiCoroutineScope.launch {
                flow.emit(result)
            }
            logger.info { "emitted:$name:$result" }
            action.invoke(result)
        }
    }
}

fun <T> sharedFlowToChannel(
    name: String,
    flow: SharedFlow<T>,
    channel: SendChannel<T>,
    coroutineScope: CoroutineScope
) {
    coroutineScope.async {
        flow.collect {
            logger.info { "collect:$name:$it" }
            channel.send(it)
            logger.info { "sent:$name:$it" }
        }
    }
}

fun <T> stateFlowToChannel(name: String, flow: StateFlow<T>, channel: SendChannel<T>, coroutineScope: CoroutineScope) {
    coroutineScope.async {
        flow.collect {
            logger.info { "collect:$name:$it" }
            channel.send(it)
            logger.info { "sent:$name:$it" }
        }
    }
}