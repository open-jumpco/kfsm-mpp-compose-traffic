package io.jumpco.open.kfsm.mpp.example.traffic.web

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope

import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficIntersectionService
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightService
import io.jumpco.open.kfsm.mpp.example.traffic.model.TrafficIntersectionViewModel
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.renderComposable

fun main() {
    val portraitMode = mutableStateOf(true)
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val uiCoroutineScope = CoroutineScope(Dispatchers.Main)
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
