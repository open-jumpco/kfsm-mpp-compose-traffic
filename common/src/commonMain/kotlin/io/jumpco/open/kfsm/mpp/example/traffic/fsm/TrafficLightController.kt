package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import com.example.kfsm.compose.traffic.fsm.TrafficLightContext
import com.example.kfsm.compose.traffic.fsm.TrafficLightStates
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow

interface TrafficLightController : TrafficLightContext {
    val currentState: TrafficLightStates
    val amber: StateFlow<Boolean>
    val red: StateFlow<Boolean>
    val green: StateFlow<Boolean>
    val stopped: ReceiveChannel<Long>
    val state: ReceiveChannel<TrafficLightStates>
    fun changeAmberTimeout(value: Long)
    fun changeFlashingOnTimeout(value: Long)
    fun changeFlashingOffTimeout(value: Long)
    suspend fun flash()
    suspend fun stop()
    suspend fun start()
    suspend fun on()
    suspend fun off()
}