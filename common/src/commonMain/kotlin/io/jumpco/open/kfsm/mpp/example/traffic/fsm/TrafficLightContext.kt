package com.example.kfsm.compose.traffic.fsm

enum class TrafficLightStates {
    RED,
    AMBER,
    GREEN,
    OFF
}

enum class TrafficLightEvents {
    STOP,
    GO,
    OFF
}

interface TrafficLightContext {
    val name: String
    val amberTimeout: Long
    suspend fun setStopped()
    suspend fun switchRed(on: Boolean)
    suspend fun switchAmber(on: Boolean)
    suspend fun switchGreen(on: Boolean)
    suspend fun stateChanged(toState: TrafficLightStates)
}