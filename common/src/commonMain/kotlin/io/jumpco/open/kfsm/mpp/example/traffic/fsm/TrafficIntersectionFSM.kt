package com.example.kfsm.compose.traffic.fsm

import io.jumpco.open.kfsm.async.asyncStateMachine
import mu.KotlinLogging


class TrafficIntersectionFSM(context: TrafficIntersectionContext) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val definition = asyncStateMachine(
            IntersectionStates.values().toSet(),
            IntersectionEvents.values().toSet(),
            TrafficIntersectionContext::class
        ) {
            initialState { IntersectionStates.STOPPED }
            onStateChange { _, toState ->
                stateChanged(toState)
            }
            whenState(IntersectionStates.STOPPED) {
                onEntry { _, _, _ ->
                    logger.info("STOPPED")
                }
                onEvent(IntersectionEvents.START to IntersectionStates.GOING) {
                    logger.info { "STOPPED:START" }
                    start()
                }
                onEvent(IntersectionEvents.STOPPED) {
                    logger.info { "STOPPED:STOPPED" }
                }
            }
            whenState(IntersectionStates.GOING) {
                onEntry { _, _, _ ->
                    logger.info("GOING:$cycleTime")
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
                    logger.info("STOPPING")
                }
                onEvent(IntersectionEvents.STOPPED to IntersectionStates.WAITING) {
                    logger.info { "STOPPED" }
                }
                onEvent(IntersectionEvents.SWITCH) {
                    logger.info { "SWITCH" }
                }
                onEvent(IntersectionEvents.STOP to IntersectionStates.WAITING_STOPPED) {
                    logger.info { "STOP" }
                }
            }
            whenState(IntersectionStates.WAITING) {
                onEntry { _, _, _ ->
                    logger.info("WAITING:$cycleWaitTime")
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
                    off()
                }
            }
            whenState(IntersectionStates.WAITING_STOPPED) {
                onEntry { _, _, _ ->
                    logger.info("WAITING_STOPPED:${cycleWaitTime / 2}")
                }
                timeout(IntersectionStates.STOPPED, { cycleWaitTime / 2 }) {
                    logger.info { "WAITING_STOPPED:timeout" }
                    off()
                }
                onEvent(IntersectionEvents.STOPPED to IntersectionStates.STOPPED) {
                    logger.info { "WAITING_STOPPED:STOPPED:ignore" }
                    off()
                }
            }
        }.build()
    }

    private val fsm = definition.create(context)
    val currentState: IntersectionStates
        get() = fsm.currentState

    suspend fun startIntersection() = fsm.sendEvent(IntersectionEvents.START)
    suspend fun stopIntersection() = fsm.sendEvent(IntersectionEvents.STOP)
    suspend fun switchIntersection() = fsm.sendEvent(IntersectionEvents.SWITCH)
    suspend fun stopped() = fsm.sendEvent(IntersectionEvents.STOPPED)
    fun allowedEvents() = fsm.allowed()
}


