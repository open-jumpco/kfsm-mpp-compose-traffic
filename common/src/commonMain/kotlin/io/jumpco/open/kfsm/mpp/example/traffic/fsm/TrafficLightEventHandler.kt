package com.example.kfsm.compose.traffic.fsm

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TrafficLightEventHandler : TrafficLightContext {
    val amber: StateFlow<Boolean>
    val red: StateFlow<Boolean>
    val green: StateFlow<Boolean>
    val stopped: SharedFlow<Long>
    val state: StateFlow<TrafficLightStates>
    fun changeAmberTimeout(value: Long)
}