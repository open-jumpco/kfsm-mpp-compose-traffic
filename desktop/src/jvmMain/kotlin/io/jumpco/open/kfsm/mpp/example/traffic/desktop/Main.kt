package io.jumpco.open.kfsm.mpp.example.traffic.desktop

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.example.kfsm.compose.traffic.fsm.TrafficIntersectionService
import com.example.kfsm.compose.traffic.fsm.TrafficLightService
import io.jumpco.open.kfsm.mpp.example.traffic.App
import io.jumpco.open.kfsm.mpp.example.traffic.view.TrafficIntersectionViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import mu.KotlinLogging
import kotlin.system.exitProcess

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val logger = KotlinLogging.logger { }
    val intersectionModel = TrafficIntersectionService(
        listOf(
            TrafficLightService("1"),
            TrafficLightService("2"),
            TrafficLightService("3")
        )
    )
    val intersectionViewModel = TrafficIntersectionViewModel(intersectionModel)
    val portraitMode = mutableStateOf(true)
    logger.info { "init:Window" }
    Window(
        title = "Traffic Intersection",
        state = WindowState(),
        onCloseRequest = { exitProcess(0) }) {
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.async {
            intersectionViewModel.setupIntersection()
        }
        portraitMode.value = window.bounds.height > window.bounds.width
        App(portraitMode, intersectionViewModel)
    }
}