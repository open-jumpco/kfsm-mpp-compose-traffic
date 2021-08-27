package com.example.kfsm.compose.traffic.fsm

enum class IntersectionStates {
    STOPPING,
    WAITING,
    GOING,
    NEXT,
    WAITING_STOPPED,
    STOPPED
}

enum class IntersectionEvents {
    SWITCH,
    STOPPED,
    STOP,
    START
}

interface TrafficIntersectionContext {
    val cycleTime: Long
    val cycleWaitTime: Long
    suspend fun stateChanged(toState: IntersectionStates)
    suspend fun start()
    suspend fun stop()
    suspend fun next()
    suspend fun off()
}