package io.jumpco.open.kfsm.mpp.example.traffic.web.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.IntersectionStates
import io.jumpco.open.kfsm.mpp.example.traffic.model.TrafficIntersectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun StateButton(
    name: String,
    allow: Boolean,
    model: TrafficIntersectionViewModel,
    coroutineScope: CoroutineScope,
    onChange: suspend TrafficIntersectionViewModel.() -> Unit
) {
    Button(
        onClick = {
            coroutineScope.launch {
                onChange.invoke(model)
            }
        },
        enabled = allow,
        contentPadding = PaddingValues(4.dp),
        elevation = ButtonDefaults.elevation()
    ) {
        Text(name)
    }
}

@Composable
fun IntersectionControls(
    modifier: Modifier,
    model: TrafficIntersectionViewModel,
    portraitMode: Boolean,
    coroutineScope: CoroutineScope
) {
    val allowOnOff = model.allowOnOff.collectAsState(false)
    val allowStart = model.allowStart.collectAsState(false)
    val allowStop = model.allowStop.collectAsState(false)
    val allowSwitch = model.allowSwitch.collectAsState(false)
    val allowFlash = model.allowFlash.collectAsState(false)
    if(portraitMode) {
        Row(
            modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StateButton(
                "On / Off",
                allowOnOff.value,
                model,
                coroutineScope
            ) {
                onOffSystem()
            }
        }
        Row(
            modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
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
        Row(
            modifier.padding(4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StateButton(
                "On / Off",
                allowOnOff.value,
                model,
                coroutineScope
            ) {
                onOffSystem()
            }
        }
        Row(
            modifier.padding(4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
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
        Row(
            modifier.padding(4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
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

@Composable
fun IntersectionState(state: IntersectionStates, viewModel: TrafficIntersectionViewModel) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(overflow = TextOverflow.Visible,
            text = buildAnnotatedString {
                append("State: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(state.name)
                }
                append(", Active: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(viewModel.currentName)
                }
                append("\nCycle time: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${viewModel.cycleTime}ms")
                }
                append(", Wait time: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${viewModel.cycleWaitTime}ms")
                }
                append("\nAmber time: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${viewModel.amberTimeout}ms")
                }
                append(", Flash On time: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${viewModel.flashOnTimeout}ms")
                }
                append(", Flash Off time: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${viewModel.flashOffTimeout}ms")
                }
            }
        )
    }
}

@Composable
fun Intersection(viewModel: TrafficIntersectionViewModel, portraitMode: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    Column {
        val state = viewModel.intersectionState.collectAsState(viewModel.currentState)
        if (portraitMode) {
            Column {
                IntersectionState(state.value, viewModel)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically)
                        .weight(0.6f)
                        .padding(16.dp)
                        .drawBehind { drawRect(Color.LightGray) },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    viewModel.trafficLights.forEach {
                        TrafficLightView(
                            Modifier
                                .aspectRatio(0.4f, true)
                                .weight(1f)
                                .padding(16.dp),
                            it
                        )
                    }
                }
                IntersectionControls(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(0.2f),
                    viewModel,
                    portraitMode,
                    coroutineScope
                )
            }
        } else {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    IntersectionState(state.value, viewModel)
                    IntersectionControls(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .weight(1f),
                        viewModel,
                        portraitMode,
                        coroutineScope
                    )
                }
                Row(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(16.dp)
                        .drawBehind { drawRect(Color.LightGray) },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    viewModel.trafficLights.forEach {
                        TrafficLightView(
                            Modifier
                                .fillMaxWidth(0.3f)
                                .fillMaxHeight()
                                .weight(1f)
                                .padding(16.dp),
                            it
                        )
                    }
                }
            }
        }
    }
}
