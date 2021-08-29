package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import com.example.kfsm.compose.traffic.fsm.TrafficLightStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicLong

class TrafficLightService(lightName: String) : TrafficLightController {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val fsm = TrafficLightFSM(this)
    private val amberChannel = Channel<Boolean>(2, BufferOverflow.DROP_OLDEST)
    private val redChannel = Channel<Boolean>(2, BufferOverflow.DROP_OLDEST)
    private val greenChannel = Channel<Boolean>(2, BufferOverflow.DROP_OLDEST)
    private val stateChannel = Channel<TrafficLightStates>(2, BufferOverflow.DROP_OLDEST)
    private val stoppedChannel = Channel<Long>(2, BufferOverflow.DROP_OLDEST)
    private var _amber = MutableStateFlow(false)
    private var _red = MutableStateFlow(false)
    private var _green = MutableStateFlow(false)
    private val _counter = AtomicLong(1)
    private var amberTimeoutValue: Long = 2000L
    private var flashingOnTimeoutValue: Long = 600L
    private var flashingOffTimeoutValue: Long = 400L
    override val flashingOnTimeout: Long get() = flashingOnTimeoutValue
    override val flashingOffTimeout: Long get() = flashingOffTimeoutValue
    override val currentState: TrafficLightStates get() = fsm.currentState
    override val name: String = lightName
    override val amberTimeout: Long get() = amberTimeoutValue
    override val amber: StateFlow<Boolean> get() = _amber
    override val red: StateFlow<Boolean> get() = _red
    override val green: StateFlow<Boolean> get() = _green
    override val stopped: ReceiveChannel<Long> get() = stoppedChannel
    override val state: ReceiveChannel<TrafficLightStates> get() = stateChannel


    init {
        logger.info { "init:start" }
        channelToStateFlow("$name:amberChannel", amberChannel, _amber, Dispatchers.Main)
        channelToStateFlow("$name:redChannel", redChannel, _red, Dispatchers.Main)
        channelToStateFlow("$name:greenChannel", greenChannel, _green, Dispatchers.Main)
        logger.info { "init:end" }
    }

    override fun changeAmberTimeout(value: Long) {
        logger.info { "changeAmberTimeout:$name:$value" }
        amberTimeoutValue = value
    }

    override fun changeFlashingOnTimeout(value: Long) {
        logger.info { "changeFlashingOnTimeout:$name:$value" }
        flashingOnTimeoutValue = value
    }

    override fun changeFlashingOffTimeout(value: Long) {
        logger.info { "changeFlashingOffTimeout:$name:$value" }
        flashingOffTimeoutValue = value
    }

    override suspend fun setStopped() {
        logger.info { "stopped:$name:start" }
        stoppedChannel.send(_counter.incrementAndGet())
        logger.info { "stopped:$name:done" }
    }

    override suspend fun switchRed(on: Boolean) {
        logger.info { "switchRed:$name:$on:start" }
        redChannel.send(on)
        logger.info { "switchRed:$name:$on:done" }
    }

    override suspend fun switchAmber(on: Boolean) {
        logger.info { "switchAmber:$name:$on:start" }
        amberChannel.send(on)
        logger.info { "switchAmber:$name:$on:done" }
    }

    override suspend fun switchGreen(on: Boolean) {
        logger.info { "switchGreen:$name:$on:start" }
        greenChannel.send(on)
        logger.info { "switchGreen:$name:$on:end" }
    }

    override suspend fun stateChanged(toState: TrafficLightStates) {
        logger.info { "stateChanged:$name:$toState:start" }
        stateChannel.send(toState)
        logger.info { "stateChanged:$name:$toState:end" }
    }

    override suspend fun flash() {
        logger.info { "flash:$name" }
        fsm.flash()
    }

    override suspend fun stop() {
        logger.info { "stop:$name" }
        fsm.stop()
    }

    override suspend fun start() {
        logger.info { "start:$name" }
        fsm.start()
    }

    override suspend fun on() {
        logger.info { "on:$name" }
        fsm.on()
    }

    override suspend fun off() {
        logger.info { "off:$name" }
        fsm.off()
    }
}