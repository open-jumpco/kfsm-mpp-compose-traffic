package io.jumpco.open.kfsm.mpp.example.traffic.web

import io.jumpco.open.kfsm.mpp.example.traffic.App

fun main() {
    val intersectionModel = TrafficIntersectionService(
        listOf(
            TrafficLightService("1"),
            TrafficLightService("2"),
            TrafficLightService("3")
        )
    )
    val intersectionViewModel = TrafficIntersectionViewModel(intersectionModel)

    renderComposable(rootElementId = "root") {
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            intersectionViewModel.setupIntersection()
        }
        portraitMode.value = window.bounds.height > window.bounds.width
        App(portraitMode, intersectionViewModel)
    }
}
