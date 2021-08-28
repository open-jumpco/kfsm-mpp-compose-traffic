package io.jumpco.open.kfsm.mpp.example.traffic.fsm

import kotlinx.coroutines.channels.ReceiveChannel

interface TrafficIntersectionController : TrafficIntersectionContext {
    val amberTimeout: Long
    val flashOnTimeout: Long
    val flashOffTimeout: Long
    val state: ReceiveChannel<IntersectionStates>
    val stopped: ReceiveChannel<Long>
    val currentName: String
    val listOrder: List<String>
    val trafficLights: List<TrafficLightController>
    val currentState: IntersectionStates
    fun get(name: String): TrafficLightController
    fun changeCycleTime(value: Long)
    fun changeCycleWaitTime(value: Long)
    fun changeAmberTimeout(value: Long)
    fun changeFlashOnTimeout(value: Long)
    fun changeFlashOffTimeout(value: Long)
    fun addTrafficLight(name: String, trafficLight: TrafficLightController)
    fun allowedEvents(): Set<IntersectionEvents>
    suspend fun setupIntersection()
    suspend fun setupTrafficLight(name: String)
    suspend fun onOffSystem()
    suspend fun startSystem()
    suspend fun stopSystem()
    suspend fun switch()
    suspend fun flashSystem()
}