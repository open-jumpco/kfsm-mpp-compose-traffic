package com.example.kfsm.compose.traffic.fsm

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
                onEvent(TrafficLightEvents.GO to TrafficLightStates.GREEN) {
                    logger.info { "OFF:GO:$name" }
                    switchRed(false)
                    switchGreen(true)
                }
                onEvent(TrafficLightEvents.STOP to TrafficLightStates.RED) {
                    logger.info { "OFF:STOP:$name" }
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
                    logger.info { "RED:OFF:$name" }
                    switchGreen(false)
                    switchAmber(false)
                    switchRed(true)
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
                    logger.info { "AMBER:OFF:$name" }
                    switchGreen(false)
                    switchAmber(false)
                    switchRed(true)
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

    suspend fun off() {
        fsm.sendEvent(TrafficLightEvents.OFF)
    }
}

