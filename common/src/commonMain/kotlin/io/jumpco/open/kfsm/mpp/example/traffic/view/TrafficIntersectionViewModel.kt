package io.jumpco.open.kfsm.mpp.example.traffic.view

import androidx.compose.runtime.snapshots.Snapshot
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.IntersectionEvents
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.IntersectionStates
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficIntersectionEventHandler
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightEventHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import mu.KotlinLogging


class TrafficIntersectionViewModel constructor(
    private val trafficIntersection: TrafficIntersectionEventHandler
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }


    private val _allowStart = MutableStateFlow(false)
    private val _allowSwitch = MutableStateFlow(false)
    private val _allowStop = MutableStateFlow(false)
    private val _allowFlash = MutableStateFlow(false)
    private val _allowOnOff = MutableStateFlow(false)
    private val _intersectionState = MutableStateFlow<IntersectionStates>(IntersectionStates.OFF)
    val intersectionState: StateFlow<IntersectionStates> get() = _intersectionState
    val currentState: IntersectionStates get() = trafficIntersection.currentState
    val allowStart: StateFlow<Boolean> get() = _allowStart
    val allowSwitch: StateFlow<Boolean> get() = _allowSwitch
    val allowStop: StateFlow<Boolean> get() = _allowStop
    val allowFlash: StateFlow<Boolean> get() = _allowFlash
    val allowOnOff: StateFlow<Boolean> get() = _allowOnOff
    val amberTimeout: Long get() = trafficIntersection.amberTimeout
    val flashOnTimeout: Long get() = trafficIntersection.flashOnTimeout
    val flashOffTimeout: Long get() = trafficIntersection.flashOffTimeout
    val cycleWaitTime: Long get() = trafficIntersection.cycleWaitTime
    val cycleTime: Long get() = trafficIntersection.cycleTime
    val currentName: String get() = trafficIntersection.currentName

    val trafficLights: List<TrafficLightEventHandler> = trafficIntersection.trafficLights

    private suspend fun determineAllowed() {
        logger.info("determineAllowed")
        val allowedEvents = trafficIntersection.allowedEvents()
        val allowedStart = allowedEvents.contains(IntersectionEvents.START)
        val allowedSwitch = allowedEvents.contains(IntersectionEvents.SWITCH)
        val allowedStop = allowedEvents.contains(IntersectionEvents.STOP)
        val allowedFlash = allowedEvents.contains(IntersectionEvents.FLASH)
        val allowedOnOff = allowedEvents.contains(IntersectionEvents.ON_OFF)
        withContext(Dispatchers.Main) {
            _allowStart.value = allowedStart
            _allowStop.value = allowedStop
            _allowSwitch.value = allowedSwitch
            _allowFlash.value = allowedFlash
            _allowOnOff.value = allowedOnOff
        }
        logger.info { "determineAllowed:start:${allowedStart}" }
        logger.info { "determineAllowed:stop:${allowedStop}" }
        logger.info { "determineAllowed:switch:${allowedSwitch}" }
        logger.info { "determineAllowed:flash:${allowedFlash}" }
        logger.info { "determineAllowed:onOff:${allowedOnOff}" }
    }

    suspend fun setupIntersection() {
        logger.info("setupIntersection:start")
        trafficIntersection.setupIntersection()
        determineAllowed()
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                val toState = trafficIntersection.state.receive()
                CoroutineScope(Dispatchers.Main).launch {
                    determineAllowed()
                    _intersectionState.emit(toState)
                    logger.info { "trafficIntersection.state.collect:$toState" }
                }
            }
        }
        logger.info("setupIntersection:end")
    }


    suspend fun onOffSystem() {
        logger.info { "onOffSystem" }
        val snapshot = Snapshot.takeMutableSnapshot()
        try {
            snapshot.enter {
                trafficIntersection.onOffSystem()
            }
            snapshot.apply()
        } catch (x: Throwable) {
            logger.error("onOffSystem:$x", x)
            snapshot.dispose()
            throw x
        }
    }

    suspend fun startSystem() {
        logger.info { "startSystem" }
        val snapshot = Snapshot.takeMutableSnapshot()
        try {
            snapshot.enter {
                trafficIntersection.startSystem()
            }
            snapshot.apply()
        } catch (x: Throwable) {
            logger.error("startSystem:$x", x)
            snapshot.dispose()
            throw x
        }
    }

    suspend fun stopSystem() {
        logger.info { "stopSystem" }
        val snapshot = Snapshot.takeMutableSnapshot()
        try {
            snapshot.enter {
                trafficIntersection.stopSystem()
            }
            snapshot.apply()
        } catch (x: Throwable) {
            logger.error("stopSystem:$x", x)
            snapshot.dispose()
            throw x
        }
    }

    suspend fun switch() {
        logger.info { "switch" }
        val snapshot = Snapshot.takeMutableSnapshot()
        try {
            snapshot.enter {
                trafficIntersection.switch()
            }
        } catch (x: Throwable) {
            logger.error("switch:$x", x)
            snapshot.dispose()
            throw x
        }
    }

    suspend fun flashSystem() {
        logger.info("flash")
        val snapshot = Snapshot.takeMutableSnapshot()
        try {
            snapshot.enter {
                trafficIntersection.flashSystem()
            }
        } catch (x: Throwable) {
            logger.error("flash:$x", x)
            snapshot.dispose()
            throw x
        }
    }
}