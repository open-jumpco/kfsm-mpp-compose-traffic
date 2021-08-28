package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import io.jumpco.open.kfsm.mpp.example.traffic.fsm.IntersectionEvents
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.IntersectionStates
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficIntersectionContext
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightEventHandler
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TrafficIntersectionEventHandler : TrafficIntersectionContext {
    val amberTimeout: Long
    val flashOnTimeout: Long
    val flashOffTimeout: Long
    val state: ReceiveChannel<IntersectionStates>
    val stopped: ReceiveChannel<Long>
    val currentName: String
    val listOrder: List<String>
    val trafficLights: List<TrafficLightEventHandler>
    val currentState: IntersectionStates
    fun get(name: String): TrafficLightEventHandler
    fun changeCycleTime(value: Long)
    fun changeCycleWaitTime(value: Long)
    fun changeAmberTimeout(value: Long)
    fun changeFlashOnTimeout(value: Long)
    fun changeFlashOffTimeout(value: Long)
    fun addTrafficLight(name: String, trafficLight: TrafficLightEventHandler)
    fun allowedEvents(): Set<IntersectionEvents>
    suspend fun setupIntersection()
    suspend fun setupTrafficLight(name: String)
    suspend fun onOffSystem()
    suspend fun startSystem()
    suspend fun stopSystem()
    suspend fun switch()
    suspend fun flashSystem()
}