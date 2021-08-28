package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import com.example.kfsm.compose.traffic.fsm.TrafficLightContext
import com.example.kfsm.compose.traffic.fsm.TrafficLightEvents
import com.example.kfsm.compose.traffic.fsm.TrafficLightStates
import io.jumpco.open.kfsm.async.asyncStateMachine
import mu.KotlinLogging

class TrafficLightFSM(context: TrafficLightContext) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val definition = asyncStateMachine(
            TrafficLightStates.values().toSet(),
            TrafficLightEvents.values().toSet(),
            TrafficLightContext::class
        ) {
            initialState { TrafficLightStates.OFF }
            onStateChange { _, toState ->
                stateChanged(toState)
            }
            whenState(TrafficLightStates.OFF) {
                onEntry { _, _, _ ->
                    logger.info { "OFF:$name" }
                }
                onEvent(TrafficLightEvents.ON to TrafficLightStates.RED) {
                    logger.info { "OFF:ON_OFF:$name" }
                    switchRed(true)
                }
            }
            whenState(TrafficLightStates.RED) {
                onEntry { _, _, _ ->
                    logger.info { "RED:$name" }
                }
                onEvent(TrafficLightEvents.GO to TrafficLightStates.GREEN) {
                    logger.info { "RED:GO:$name" }
                    switchRed(false)
                    switchGreen(true)
                }
                onEvent(TrafficLightEvents.STOP) {
                    logger.info { "RED:STOP:$name" }
                    switchGreen(false)
                    switchAmber(false)
                    switchRed(true)
                }
                onEvent(TrafficLightEvents.OFF to TrafficLightStates.OFF) {
                    logger.info { "RED:ON_OFF:$name" }
                    switchGreen(false)
                    switchAmber(false)
                    switchRed(false)
                }
                onEvent(TrafficLightEvents.FLASH to TrafficLightStates.FLASHING_ON) {
                    logger.info { "RED:FLASH:$name" }
                }
            }
            whenState(TrafficLightStates.AMBER) {
                onEntry { _, _, _ ->
                    logger.info { "AMBER:$name" }
                }
                timeout(TrafficLightStates.RED, { amberTimeout }) {
                    logger.info { "AMBER:timeout:$name" }
                    switchRed(true)
                    switchAmber(false)
                    setStopped()
                }
                onEvent(TrafficLightEvents.STOP) {
                    logger.info { "AMBER:STOP:$name" }
                }
                onEvent(TrafficLightEvents.OFF to TrafficLightStates.OFF) {
                    logger.info { "AMBER:ON_OFF:$name" }
                    switchGreen(false)
                    switchAmber(false)
                    switchRed(false)
                }
            }
            whenState(TrafficLightStates.GREEN) {
                onEntry { _, _, _ ->
                    logger.info { "GREEN:$name" }
                }
                onEvent(TrafficLightEvents.STOP to TrafficLightStates.AMBER) {
                    logger.info { "GREEN:STOP:$name" }
                    switchGreen(false)
                    switchAmber(true)
                }
                onEvent(TrafficLightEvents.OFF to TrafficLightStates.OFF) {
                    logger.info { "GREEN:OFF:$name" }
                    switchGreen(false)
                    switchAmber(false)
                    switchRed(false)
                }
            }
            whenState(TrafficLightStates.FLASHING_ON) {
                onEntry { _, _, _ ->
                    logger.info { "FLASHING_ON:$name" }
                }
                timeout(TrafficLightStates.FLASHING_OFF, { flashingOnTimeout }) {
                    switchRed(false)
                }
                onEvent(TrafficLightEvents.OFF to TrafficLightStates.OFF) {
                    logger.info { "FLASHING_ON:OFF:$name" }
                    switchGreen(false)
                    switchAmber(false)
                    switchRed(false)
                }
                onEvent(TrafficLightEvents.STOP to TrafficLightStates.RED) {
                }
            }
            whenState(TrafficLightStates.FLASHING_OFF) {
                onEntry { _, _, _ ->
                    logger.info { "FLASHING_OFF:$name" }
                }
                timeout(TrafficLightStates.FLASHING_ON, { flashingOffTimeout }) {
                    switchRed(true)
                }
                onEvent(TrafficLightEvents.OFF to TrafficLightStates.OFF) {
                    logger.info { "FLASHING_OFF:OFF:$name" }
                }
                onEvent(TrafficLightEvents.STOP to TrafficLightStates.RED) {
                    switchRed(true)
                }
            }
        }.build()
    }

    private val fsm = definition.create(context)

    suspend fun start() {
        fsm.sendEvent(TrafficLightEvents.GO)
    }

    suspend fun stop() {
        fsm.sendEvent(TrafficLightEvents.STOP)
    }

    suspend fun on() {
        fsm.sendEvent(TrafficLightEvents.ON)
    }

    suspend fun off() {
        fsm.sendEvent(TrafficLightEvents.OFF)
    }

    suspend fun flash() {
        fsm.sendEvent(TrafficLightEvents.FLASH)
    }
}

