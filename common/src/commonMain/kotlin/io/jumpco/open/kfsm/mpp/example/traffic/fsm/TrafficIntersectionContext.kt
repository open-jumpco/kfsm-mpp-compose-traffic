package io.jumpco.open.kfsm.mpp.example.traffic.fsm

enum class IntersectionStates {
    OFF,
    STOPPING,
    WAITING,
    GOING,
    NEXT,
    FLASHING,
    WAITING_STOPPED,
    STOPPED
}

enum class IntersectionEvents {
    ON_OFF,
    SWITCH,
    STOPPED,
    STOP,
    START,
    FLASH
}

interface TrafficIntersectionContext {
    val cycleTime: Long
    val cycleWaitTime: Long
    suspend fun stateChanged(toState: IntersectionStates)
    suspend fun on()
    suspend fun off()
    suspend fun start()
    suspend fun stop()
    suspend fun next()
    suspend fun stopAll()
    suspend fun flashAll()
}