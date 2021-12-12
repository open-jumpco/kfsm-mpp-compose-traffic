@file:Suppress("FunctionName")

package io.jumpco.open.kfsm.mpp.example.traffic.web.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightController
import io.jumpco.open.kfsm.mpp.example.traffic.web.Amber
import io.jumpco.open.kfsm.mpp.example.traffic.web.Green
import io.jumpco.open.kfsm.mpp.example.traffic.web.Red
import org.jetbrains.compose.web.ExperimentalComposeWebSvgApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.svg.Circle
import org.jetbrains.compose.web.svg.Svg

@OptIn(ExperimentalComposeWebSvgApi::class)
@ExperimentalComposeWebSvgApi
@Composable
fun LightView(
    interior: CSSColorValue,
    state: State<Boolean>
) {
    val color = if (state.value) interior else Color.black
    Svg(viewBox = "0 0 200 200") {
        Circle(100, 100, 80, attrs = {
            attr("fill", color.toString())
        })
    }
}

@OptIn(ExperimentalComposeWebSvgApi::class)
@Composable
fun TrafficLightView(context: TrafficLightController) {
    Div({
        style {
            padding(1.em)
            backgroundColor(Color.darkgray)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            alignContent(AlignContent.SpaceEvenly)
        }
    }) {
        Div({
            style {
                maxWidth(100.percent)
                padding(1.em)
                fontSize(2.em)
                color(Color.white)
                textAlign("center")
                fontWeight("bold")
            }
        }) {
            Text(context.name)
        }
        LightView(
            Red,
            context.red.collectAsState(false)
        )
        LightView(
            Amber,
            context.amber.collectAsState(false)
        )
        LightView(
            Green,
            context.green.collectAsState(false)
        )
    }
}