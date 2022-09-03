package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import io.jumpco.open.kfsm.async.asyncStateMachine
import kotlinx.coroutines.CoroutineScope
import mu.KotlinLogging


class TrafficIntersectionFSM(context: TrafficIntersectionContext, coroutineScope: CoroutineScope) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val definition = asyncStateMachine(
            IntersectionStates.values().toSet(),
            IntersectionEvents.values().toSet(),
            TrafficIntersectionContext::class
        ) {
            initialState { IntersectionStates.OFF }
            onStateChange { _, toState ->
                stateChanged(toState)
            }
            whenState(IntersectionStates.OFF) {
                onEntry { _, _, _ ->
                    logger.info { ">> OFF" }
                }
                onEvent(IntersectionEvents.ON_OFF to IntersectionStates.STOPPED) {
                    on()
                }
            }
            whenState(IntersectionStates.STOPPED) {
                onEntry { _, _, _ ->
                    logger.info { ">> STOPPED" }
                }
                onEvent(IntersectionEvents.ON_OFF to IntersectionStates.OFF) {
                    logger.info { "STOPPED:ON_OFF" }
                    off()
                }
                onEvent(IntersectionEvents.START to IntersectionStates.GOING) {
                    logger.info { "STOPPED:START" }
                    start()
                }
                onEvent(IntersectionEvents.STOPPED) {
                    logger.info { "STOPPED:STOPPED" }
                }
                onEvent(IntersectionEvents.FLASH to IntersectionStates.FLASHING) {
                    logger.info { "STOPPED:FLASH" }
                    flashAll()
                }
            }
            whenState(IntersectionStates.GOING) {
                onEntry { _, _, _ ->
                    logger.info { ">> GOING:$cycleTime" }
                }
                timeout(IntersectionStates.STOPPING, { cycleTime }) {
                    logger.info { "GOING:timeout" }
                    stop()
                }
                onEvent(IntersectionEvents.SWITCH to IntersectionStates.STOPPING) {
                    logger.info { "GOING:SWITCH" }
                    stop()
                }
                onEvent(IntersectionEvents.STOP to IntersectionStates.WAITING_STOPPED) {
                    logger.info { "GOING:STOP" }
                    stop()
                }
            }
            whenState(IntersectionStates.STOPPING) {
                onEntry { _, _, _ ->
                    logger.info { ">> STOPPING" }
                }
                onEvent(IntersectionEvents.STOPPED to IntersectionStates.WAITING) {
                    logger.info { "STOPPING:STOPPED" }
                }
                onEvent(IntersectionEvents.SWITCH) {
                    logger.info { "STOPPING:SWITCH" }
                }
                onEvent(IntersectionEvents.STOP to IntersectionStates.WAITING_STOPPED) {
                    logger.info { "STOPPING:STOP" }
                }
            }
            whenState(IntersectionStates.WAITING) {
                onEntry { _, _, _ ->
                    logger.info { ">> WAITING:$cycleWaitTime" }
                }
                timeout(IntersectionStates.GOING, { cycleWaitTime }) {
                    logger.info { "WAITING:timeout" }
                    next()
                    start()
                }
                onEvent(IntersectionEvents.SWITCH) {
                    logger.info { "WAITING:SWITCH" }
                }
                onEvent(IntersectionEvents.STOP to IntersectionStates.STOPPED) {
                    logger.info { "WAITING:STOP" }
                    stop()
                }
            }
            whenState(IntersectionStates.WAITING_STOPPED) {
                onEntry { _, _, _ ->
                    logger.info { ">> WAITING_STOPPED:${cycleWaitTime / 2}" }
                }
                timeout(IntersectionStates.STOPPED, { cycleWaitTime / 2 }) {
                    logger.info { "WAITING_STOPPED:timeout" }
                }
                onEvent(IntersectionEvents.STOPPED to IntersectionStates.STOPPED) {
                    logger.info { "WAITING_STOPPED:STOPPED:ignore" }
                }
            }
            whenState(IntersectionStates.FLASHING) {
                onEntry { _, _, _ ->
                    logger.info { ">> FLASHING" }
                }
                onEvent(IntersectionEvents.STOP to IntersectionStates.STOPPED) {
                    stopAll()
                }
            }
        }.build()
    }

    private val fsm = definition.create(context, coroutineScope)

    val currentState: IntersectionStates get() = fsm.currentState
    suspend fun onOffIntersection() = fsm.sendEvent(IntersectionEvents.ON_OFF)
    suspend fun startIntersection() = fsm.sendEvent(IntersectionEvents.START)
    suspend fun stopIntersection() = fsm.sendEvent(IntersectionEvents.STOP)
    suspend fun switchIntersection() = fsm.sendEvent(IntersectionEvents.SWITCH)
    suspend fun stopped() = fsm.sendEvent(IntersectionEvents.STOPPED)
    suspend fun flashIntersection() = fsm.sendEvent(IntersectionEvents.FLASH)
    fun allowedEvents() = fsm.allowed()
}


