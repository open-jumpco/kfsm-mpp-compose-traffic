package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import com.example.kfsm.compose.traffic.fsm.TrafficLightContext
import com.example.kfsm.compose.traffic.fsm.TrafficLightStates
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TrafficLightController : TrafficLightContext {
    val amber: StateFlow<Boolean>
    val red: StateFlow<Boolean>
    val green: StateFlow<Boolean>
    val stopped: SharedFlow<Long>
    val state: StateFlow<TrafficLightStates>
    fun changeAmberTimeout(value: Long)
    fun changeFlashingOnTimeout(value: Long)
    fun changeFlashingOffTimeout(value: Long)
    suspend fun flash()
    suspend fun stop()
    suspend fun start()
    suspend fun on()
    suspend fun off()
}