@file:Suppress("FunctionName")

package io.jumpco.open.kfsm.mpp.example.traffic.web.view


import LandscapeContainer
import PortraitContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.IntersectionStates
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightController
import io.jumpco.open.kfsm.mpp.example.traffic.model.TrafficIntersectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

//private val logger: KotlinLogging.logger { }

@Composable
fun StateButton(
    name: String,
    allow: Boolean,
    model: TrafficIntersectionViewModel,
    coroutineScope: CoroutineScope,
    onChange: suspend TrafficIntersectionViewModel.() -> Unit
) {
    Button(attrs = {
        style {
            if (allow) {
                backgroundColor(Color.blue)
                color(Color.white)
            } else {
                backgroundColor(Color.lightgray)
                color(Color.darkgray)
            }
            flexGrow(1)
        }
        if (!allow) {
            this.disabled();
        }
        onClick {
            // logger.info("Click:$name")
            console.info("Click:$name")
            coroutineScope.launch {
                onChange.invoke(model)
            }
        }
    }) {
        Text(name)
    }
}

@Composable
fun IntersectionControls(
    grow: Number,
    width: CSSNumeric,
    height: CSSNumeric,
    model: TrafficIntersectionViewModel,
    portraitMode: Boolean,
    coroutineScope: CoroutineScope
) {
    val allowOnOff = model.allowOnOff.collectAsState(false)
    val allowStart = model.allowStart.collectAsState(false)
    val allowStop = model.allowStop.collectAsState(false)
    val allowSwitch = model.allowSwitch.collectAsState(false)
    val allowFlash = model.allowFlash.collectAsState(false)
    if (portraitMode) {
        Div({
            id("state-buttons")
            style {
                maxWidth(width)
                minWidth(width)
                maxHeight(height)
                minHeight(height)
                flexGrow(grow)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                flexWrap(FlexWrap.Wrap)
                justifyContent(JustifyContent.SpaceEvenly)
                alignItems(AlignItems.Center)
            }
        }) {
            StateButton(
                "On / Off",
                allowOnOff.value,
                model,
                coroutineScope
            ) {
                onOffSystem()
            }
            StateButton(
                "Start",
                allowStart.value,
                model,
                coroutineScope
            ) {
                startSystem()
            }
            StateButton(
                "Stop",
                allowStop.value,
                model,
                coroutineScope
            ) {
                stopSystem()
            }
            StateButton(
                "Switch",
                allowSwitch.value,
                model,
                coroutineScope
            ) {
                switch()
            }
            StateButton(
                "Flash",
                allowFlash.value,
                model,
                coroutineScope
            ) {
                flashSystem()
            }
        }
    } else {
        Div({
            id("state-button-on-off")
            style {
                maxWidth(width)
                minWidth(width)
                maxHeight(height)
                minHeight(height)
                flexGrow(grow)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                justifyContent(JustifyContent.SpaceBetween)
                alignItems(AlignItems.Center)
            }
        }) {
            StateButton(
                "On / Off",
                allowOnOff.value,
                model,
                coroutineScope
            ) {
                onOffSystem()
            }

            Div({
                id("state-button-start-stop")
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                    alignContent(AlignContent.SpaceEvenly)
                    // alignItems(AlignItems.Center)
                }
            }) {
                StateButton(
                    "Start",
                    allowStart.value,
                    model,
                    coroutineScope
                ) {
                    startSystem()
                }
                StateButton(
                    "Stop",
                    allowStop.value,
                    model,
                    coroutineScope
                ) {
                    stopSystem()
                }
            }
            Div({
                id("state-button-switch-flash")
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                    alignContent(AlignContent.SpaceEvenly)
                    // alignItems(AlignItems.FlexStart)
                }
            }) {
                StateButton(
                    "Switch",
                    allowSwitch.value,
                    model,
                    coroutineScope
                ) {
                    switch()
                }
                StateButton(
                    "Flash",
                    allowFlash.value,
                    model,
                    coroutineScope
                ) {
                    flashSystem()
                }
            }
        }
    }
}

@Composable
fun IntersectionState(
    grow: Number,
    width: CSSNumeric,
    height: CSSNumeric,
    state: IntersectionStates,
    viewModel: TrafficIntersectionViewModel
) {
    Div({
        style {
            flexGrow(grow)
            maxWidth(width)
            minWidth(width)
            maxHeight(height)
            minHeight(height)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            flexWrap(FlexWrap.Wrap)
            alignContent(AlignContent.Start)
            alignItems(AlignItems.Start)
        }
    }) {
        Span { Text("State: ") }
        B { Text(state.name) }
        Span { Text(", Active: ") }
        B { Text(viewModel.currentName) }
        Span { Text(", Cycle time: ") }
        B { Text("${viewModel.cycleTime}ms") }
        Span { Text(", Wait time: ") }
        B { Text("${viewModel.cycleWaitTime}ms") }
        Span { Text(", Amber time: ") }
        B { Text("${viewModel.amberTimeout}ms") }
        Span { Text(", Flash On time: ") }
        B { Text("${viewModel.flashOnTimeout}ms") }
        Span { Text(", Flash Off time: ") }
        B { Text("${viewModel.flashOffTimeout}ms") }
    }
}

@Composable
fun Trafflights(grow: Number, width: CSSNumeric, height: CSSNumeric, trafficLights: List<TrafficLightController>) {
    Div({
        id("lights")
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            maxHeight(height)
            minHeight(height)
            maxWidth(width)
            minWidth(width)
            flexGrow(grow)
            backgroundColor(Color.darkslategray)
            alignContent(AlignContent.SpaceBetween)
            justifyContent(JustifyContent.SpaceEvenly)
            alignItems(AlignItems.Stretch)
        }
    }) {
        trafficLights.forEach {
            Div({
                id("light-${it.name}")
                style {
                    flexGrow(1)
                    alignSelf(AlignSelf.Center)
                    padding(1.em)
                }
            }) {
                TrafficLightView(it)
            }
        }
    }
}

@Composable
fun Intersection(viewModel: TrafficIntersectionViewModel, portraitMode: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.intersectionState.collectAsState(viewModel.currentState)
    if (portraitMode) {
        PortraitContainer {
            IntersectionState(1, 100.percent, 10.percent, state.value, viewModel)
            Trafflights(6, 100.percent, 60.percent, viewModel.trafficLights)
            IntersectionControls(
                3,
                100.percent,
                30.percent,
                viewModel,
                portraitMode,
                coroutineScope
            )
        }
    } else {
        LandscapeContainer {
            Div({
                id("state-control-holder")
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    flexGrow(1)
                    maxWidth(50.percent)
                    minWidth(50.percent)
                    maxHeight(100.percent)
                    minHeight(100.percent)
                    alignItems(AlignItems.Start)
                    alignContent(AlignContent.SpaceEvenly)
                }
            }) {
                IntersectionState(2, 100.percent, 20.percent, state.value, viewModel)
                IntersectionControls(
                    8,
                    100.percent,
                    80.percent,
                    viewModel,
                    portraitMode,
                    coroutineScope
                )
            }
            Trafflights(1, 50.percent, 100.percent, viewModel.trafficLights)
        }
    }
}

