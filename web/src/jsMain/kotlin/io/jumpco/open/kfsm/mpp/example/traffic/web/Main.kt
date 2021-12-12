package io.jumpco.open.kfsm.mpp.example.traffic.web

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope

import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficIntersectionService
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightService
import io.jumpco.open.kfsm.mpp.example.traffic.model.TrafficIntersectionViewModel
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.renderComposable

fun main() {
    val portraitMode = mutableStateOf(true)
    val intersectionModel = TrafficIntersectionService(
        listOf(
            TrafficLightService("1"),
            TrafficLightService("2"),
            TrafficLightService("3")
        )
    )
    val intersectionViewModel = TrafficIntersectionViewModel(intersectionModel)
    // TODO find a way to detect screen orientation change.
    renderComposable(rootElementId = "root") {
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            intersectionViewModel.setupIntersection()
        }
        portraitMode.value = window.screen.height > window.screen.width
        App(portraitMode, intersectionViewModel)
    }
}
