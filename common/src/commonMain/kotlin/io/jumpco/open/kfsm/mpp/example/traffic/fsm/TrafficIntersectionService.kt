package com.example.kfsm.compose.traffic.fsm

import io.jumpco.open.kfsm.mpp.example.traffic.fsm.sendToChannel
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.sharedFlowToChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicLong

class TrafficIntersectionService(
    override val trafficLights: List<TrafficLightEventHandler>
) : TrafficIntersectionEventHandler {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val stateChannel = Channel<IntersectionStates>(2)
    private val stoppedChannel = Channel<Long>(2)
    private val trafficLightData = mutableMapOf<String, TrafficLightEventHandler>()
    private val stateMachines = mutableMapOf<String, TrafficLightFSM>()
    private val order = mutableListOf<String>()
    private val intersectionFSM = TrafficIntersectionFSM(this)
    private var _currentName: String
    private var _current: TrafficLightContext
    private val _counter = AtomicLong(1)
    private val _stopped = MutableSharedFlow<Long>()
    private val _state = MutableStateFlow(IntersectionStates.STOPPED)
    private var _cycleWaitTime: Long = 1000L
    private var _cycleTime: Long = 5000L
    private var _amberTimeout: Long = 2000L
    override val state: StateFlow<IntersectionStates> get() = _state
    override val stopped: SharedFlow<Long> get() = _stopped
    override val amberTimeout: Long get() = _amberTimeout

    init {
        require(trafficLights.isNotEmpty()) { "At least one light is required" }
        _current = trafficLights[0]
        _currentName = _current.name
        trafficLights.forEach {
            it.changeAmberTimeout(amberTimeout)
            addTrafficLight(it.name, it)
            order.add(it.name)
        }
        sendToChannel(stateChannel, _state, Dispatchers.Main)
        sendToChannel(stoppedChannel, _stopped, Dispatchers.Main)
    }

    override fun changeAmberTimeout(value: Long) {
        _amberTimeout = value
        trafficLightData.values.forEach {
            it.changeAmberTimeout(value)
        }
    }

    override suspend fun setupIntersection() {
        logger.info { "setupIntersection:start" }
        trafficLightData.values.forEach {
            startTrafficLight(it.name)
        }
        logger.info { "setupIntersection:end" }
    }

    override val listOrder: List<String> get() = order

    override fun get(name: String): TrafficLightEventHandler {
        return trafficLightData[name] ?: error("expected trafficLight:$name")
    }

    override val cycleTime: Long get() = _cycleTime
    override val currentName: String get() = _currentName
    override val current: TrafficLightContext get() = _current
    override val cycleWaitTime: Long get() = _cycleWaitTime
    val currentState: IntersectionStates get() = intersectionFSM.currentState

    override fun addTrafficLight(name: String, trafficLight: TrafficLightEventHandler) {
        val fsm = TrafficLightFSM(trafficLight)
        stateMachines[name] = fsm
        trafficLightData[name] = trafficLight
    }

    override suspend fun startTrafficLight(name: String) {
        logger.info { "startTrafficLight:$name:start" }
        val trafficLight = trafficLightData[name]
        requireNotNull(trafficLight) { "Expected to find TrafficLight:$name" }
        val fsm = stateMachines[name]
        requireNotNull(fsm) { "Expected to find TrafficLightFSM:$name" }
        fsm.stop()
        sharedFlowToChannel(trafficLight.stopped, stoppedChannel)
        logger.info { "startTrafficLight:$name:end" }
    }

    override suspend fun stateChanged(toState: IntersectionStates) {
        stateChannel.send(toState)
    }

    override fun changeCycleTime(value: Long) {
        _cycleWaitTime = value
    }

    override fun changeCycleWaitTime(value: Long) {
        _cycleWaitTime = value
    }

    override suspend fun start() {
        val fsm = stateMachines[currentName]
        requireNotNull(fsm) { "expected stateMachine for $currentName" }
        logger.info { "$currentState:$currentName:start()" }
        fsm.start()
    }

    override suspend fun stop() {
        val fsm = stateMachines[currentName]
        requireNotNull(fsm) { "expected stateMachine for $currentName" }
        logger.info { "$currentState:$currentName:stop()" }
        fsm.stop()
    }

    override suspend fun off() {
        val fsm = stateMachines[currentName]
        requireNotNull(fsm) { "expected stateMachine for $currentName" }
        logger.info { "$currentState:$currentName:off()" }
        fsm.off()
    }

    override suspend fun next() {
        val oldName = currentName
        var index = order.indexOf(currentName) + 1
        if (index >= order.size) {
            index = 0
        }
        _currentName = order[index]
        logger.info { "$currentState:$oldName -> $currentName" }
    }

    override suspend fun startSystem() {
        logger.info("startSystem")
        intersectionFSM.startIntersection()
    }

    override suspend fun stopSystem() {
        logger.info("stopSystem")
        intersectionFSM.stopIntersection()
    }

    override suspend fun switch() {
        logger.info("switch")
        intersectionFSM.switchIntersection()
    }

    override suspend fun stopped() {
        logger.info("stopped")
        intersectionFSM.stopped()
    }

    override fun allowedEvents(): Set<IntersectionEvents> = intersectionFSM.allowedEvents()

}