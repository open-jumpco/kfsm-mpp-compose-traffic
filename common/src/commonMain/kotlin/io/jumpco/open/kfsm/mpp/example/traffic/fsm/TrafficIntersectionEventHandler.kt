package com.example.kfsm.compose.traffic.fsm

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TrafficIntersectionEventHandler : TrafficIntersectionContext {
    val amberTimeout: Long
    val state: StateFlow<IntersectionStates>
    val stopped: SharedFlow<Long>
    val currentName: String
    val current: TrafficLightContext
    val listOrder: List<String>
    val trafficLights: List<TrafficLightEventHandler>
    fun get(name: String): TrafficLightEventHandler
    fun changeCycleTime(value: Long)
    fun changeCycleWaitTime(value: Long)
    fun changeAmberTimeout(value: Long)
    fun addTrafficLight(name: String, trafficLight: TrafficLightEventHandler)
    fun allowedEvents(): Set<IntersectionEvents>
    suspend fun setupIntersection()
    suspend fun stopped()
    suspend fun startTrafficLight(name: String)
    suspend fun startSystem()
    suspend fun stopSystem()
    suspend fun switch()
}