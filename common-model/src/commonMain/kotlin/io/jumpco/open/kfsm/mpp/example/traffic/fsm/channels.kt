package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
fun <T> channelToStateFlow(
    name: String,
    channel: ReceiveChannel<T>,
    flow: MutableStateFlow<T>,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            CoroutineScope(dispatcher).launch {
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
    dispatcher: CoroutineDispatcher,
    action: suspend (T) -> Unit
) {
    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            CoroutineScope(dispatcher).launch {
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
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            CoroutineScope(dispatcher).launch {
                flow.emit(result)
                logger.info { "emitted:$name:$result" }
            }
        }
    }
}

fun <T> channelToChannel(
    name: String,
    receiveChannel: ReceiveChannel<T>,
    sendChannel: SendChannel<T>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val result = receiveChannel.receive()
            logger.info { "received:$name:$result" }
            CoroutineScope(dispatcher).launch {
                logger.info { "sending:$name:$result" }
                sendChannel.send(result)
                logger.info { "sent:$name:$result" }
            }
        }
    }
}

fun <T> channelToSharedFlow(
    name: String,
    channel: ReceiveChannel<T>,
    flow: MutableSharedFlow<T>,
    dispatcher: CoroutineDispatcher,
    action: suspend (T) -> Unit
) {
    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val result = channel.receive()
            logger.info { "received:$name:$result" }
            CoroutineScope(dispatcher).launch {
                flow.emit(result)
                logger.info { "emitted:$name:$result" }
                action.invoke(result)
            }
        }
    }
}

fun <T> sharedFlowToChannel(name: String, flow: SharedFlow<T>, channel: SendChannel<T>) {
    CoroutineScope(Dispatchers.Default).launch {
        flow.collect {
            logger.info { "collect:$name:$it" }
            channel.send(it)
            logger.info { "sent:$name:$it" }
        }
    }
}

fun <T> stateFlowToChannel(name: String, flow: StateFlow<T>, channel: SendChannel<T>) {
    CoroutineScope(Dispatchers.Default).launch {
        flow.collect {
            logger.info { "collect:$name:$it" }
            channel.send(it)
            logger.info { "sent:$name:$it" }
        }
    }
}