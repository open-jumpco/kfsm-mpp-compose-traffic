package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import mu.KotlinLogging

class TrafficIntersectionService(
    override val trafficLights: List<TrafficLightController>
) : TrafficIntersectionController {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val stateChannel = Channel<IntersectionStates>(1)
    private val stoppedChannel = Channel<Long>(1)
    private val trafficLightData = mutableMapOf<String, TrafficLightController>()
    private val order = mutableListOf<String>()
    private val intersectionFSM = TrafficIntersectionFSM(this)
    private var _currentLight: TrafficLightController

    private var _cycleWaitTime: Long = 1000L
    private var _cycleTime: Long = 5000L
    private var _amberTimeout: Long = 2000L
    private var _flashOnTimeout: Long = 600L
    private var _flashOffTimeout: Long = 400L
    override val state: ReceiveChannel<IntersectionStates> get() = stateChannel
    override val stopped: ReceiveChannel<Long> get() = stoppedChannel
    override val amberTimeout: Long get() = _amberTimeout
    override val flashOnTimeout: Long get() = _flashOnTimeout
    override val flashOffTimeout: Long get() = _flashOffTimeout
    override val currentState: IntersectionStates get() = intersectionFSM.currentState

    init {
        logger.info("init:start")
        require(trafficLights.isNotEmpty()) { "At least one light is required" }
        _currentLight = trafficLights[0]
        trafficLights.forEach {
            it.changeAmberTimeout(amberTimeout)
            it.changeFlashingOffTimeout(flashOffTimeout)
            it.changeFlashingOnTimeout(flashOnTimeout)
            addTrafficLight(it.name, it)
            order.add(it.name)
        }
        logger.info("init:end")
    }

    override fun changeAmberTimeout(value: Long) {
        _amberTimeout = value
        trafficLights.forEach {
            it.changeAmberTimeout(value)
        }
    }

    override fun changeFlashOnTimeout(value: Long) {
        _flashOnTimeout = value
        trafficLights.forEach {
            it.changeFlashingOnTimeout(value)
        }
    }

    override fun changeFlashOffTimeout(value: Long) {
        _flashOffTimeout = value
        trafficLights.forEach {
            it.changeFlashingOffTimeout(value)
        }
    }

    override suspend fun setupIntersection() {
        logger.info { "setupIntersection:start" }
        trafficLightData.values.forEach {
            setupTrafficLight(it.name)
        }
        CoroutineScope(Dispatchers.Default).async {
            while (true) {
                val event = stopped.receive()
                logger.info { "stopped:$event" }
                intersectionFSM.stopped()
            }
        }

        logger.info { "setupIntersection:end" }
    }

    override val listOrder: List<String> get() = order

    override fun get(name: String): TrafficLightController {
        return trafficLightData[name] ?: error("expected trafficLight:$name")
    }

    override val cycleTime: Long get() = _cycleTime
    override val currentName: String get() = _currentLight.name
    override val cycleWaitTime: Long get() = _cycleWaitTime

    override fun addTrafficLight(name: String, trafficLight: TrafficLightController) {
        trafficLightData[name] = trafficLight
    }

    override suspend fun setupTrafficLight(name: String) {
        logger.info { "setupTrafficLight:$name:start" }
        val trafficLight = trafficLightData[name]
        requireNotNull(trafficLight) { "Expected to find TrafficLight:$name" }
        sharedFlowToChannel("${trafficLight.name}:stoppedFlow", trafficLight.stopped, stoppedChannel)
        logger.info { "startTrafficLight:$name:end" }
    }

    override suspend fun stateChanged(toState: IntersectionStates) {
        logger.info { "stateChanged:$toState:start" }
        stateChannel.send(toState)
        logger.info { "stateChanged:$toState:end" }
    }

    override fun changeCycleTime(value: Long) {
        _cycleWaitTime = value
    }

    override fun changeCycleWaitTime(value: Long) {
        _cycleWaitTime = value
    }

    override suspend fun start() {
        logger.info { "$currentState:$currentName:start()" }
        _currentLight.start()
    }

    override suspend fun stop() {
        logger.info { "$currentState:$currentName:stop()" }
        _currentLight.stop()
    }

    override suspend fun on() {
        logger.info { "$currentState:on()" }
        trafficLights.forEach {
            it.on()
        }
    }

    override suspend fun off() {
        logger.info("$currentState:off")
        trafficLights.forEach {
            it.off()
        }
    }

    override suspend fun next() {
        val oldName = currentName
        var index = order.indexOf(currentName) + 1
        if (index >= order.size) {
            index = 0
        }
        val name = order[index]
        logger.info { "$currentState:$oldName -> $name" }
        _currentLight = requireNotNull(trafficLightData[name])
    }

    override suspend fun startSystem() {
        logger.info("startSystem")
        intersectionFSM.startIntersection()
    }

    override suspend fun onOffSystem() {
        logger.info("onOffSystem")
        intersectionFSM.onOffIntersection()
    }

    override suspend fun stopSystem() {
        logger.info("stopSystem")
        intersectionFSM.stopIntersection()
    }

    override suspend fun switch() {
        logger.info("switch")
        intersectionFSM.switchIntersection()
    }

    override suspend fun flashSystem() {
        logger.info("flash")
        intersectionFSM.flashIntersection()
    }

    override suspend fun stopAll() {
        logger.info("offAll")
        trafficLights.forEach {
            it.stop()
        }
    }

    override suspend fun flashAll() {
        logger.info("flashAll")
        trafficLights.forEach { it.flash() }
    }

    override fun allowedEvents(): Set<IntersectionEvents> = intersectionFSM.allowedEvents()

}