package io.jumpco.open.kfsm.mpp.example.traffic.desktop

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import io.jumpco.open.kfsm.mpp.example.traffic.App
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficIntersectionService
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightService
import io.jumpco.open.kfsm.mpp.example.traffic.model.TrafficIntersectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import mu.KotlinLogging
import kotlin.system.exitProcess

private val coroutineScope = CoroutineScope(Dispatchers.Default)
fun main() = application {
    val uiCoroutineScope = rememberCoroutineScope()
    val logger = KotlinLogging.logger { }
    val intersectionModel = TrafficIntersectionService(
        listOf(
            TrafficLightService("1", uiCoroutineScope, coroutineScope),
            TrafficLightService("2", uiCoroutineScope, coroutineScope),
            TrafficLightService("3", uiCoroutineScope, coroutineScope)
        ),
        uiCoroutineScope,
        coroutineScope
    )
    val intersectionViewModel = TrafficIntersectionViewModel(intersectionModel, uiCoroutineScope, coroutineScope)
    val portraitMode = mutableStateOf(true)
    logger.info { "init:Window" }
    Window(
        title = "Traffic Intersection",
        state = WindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition(alignment = Alignment.Center)
        ),
        onCloseRequest = { exitProcess(0) }) {
        coroutineScope.async {
            intersectionViewModel.setupIntersection()
        }
        portraitMode.value = window.bounds.height > window.bounds.width
        App(portraitMode, intersectionViewModel)
    }
}