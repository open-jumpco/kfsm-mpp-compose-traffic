package com.example.kfsm.compose.traffic.fsm

enum class TrafficLightStates {
    RED,
    AMBER,
    GREEN,
    OFF,
    FLASHING_ON,
    FLASHING_OFF
}

enum class TrafficLightEvents {
    ON,
    OFF,
    STOP,
    GO,
    FLASH
}

interface TrafficLightContext {
    val name: String
    val amberTimeout: Long
    val flashingOnTimeout: Long
    val flashingOffTimeout: Long
    suspend fun setStopped()
    suspend fun switchRed(on: Boolean)
    suspend fun switchAmber(on: Boolean)
    suspend fun switchGreen(on: Boolean)
    suspend fun stateChanged(toState: TrafficLightStates)
}