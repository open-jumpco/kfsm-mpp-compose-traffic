package io.jumpco.open.kfsm.mpp.example.traffic.activity

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.jumpco.open.kfsm.mpp.example.traffic.fsm.TrafficLightController
import io.jumpco.open.kfsm.mpp.example.traffic.Amber


@Composable
fun LightView(
    modifier: Modifier,
    interior: Color,
    state: State<Boolean>
) {
    val color = if (state.value) interior else Color.Black
    Canvas(modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        drawCircle(color, size.minDimension / 4, center)
    }
}

@Composable
fun TrafficLightView(modifier: Modifier, context: TrafficLightController) {
    Column(
        modifier.drawBehind { drawRect(Color.DarkGray) },
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val lightModifier = Modifier.fillMaxWidth(1f)
            .aspectRatio(1f)
            .weight(1f)
            .padding(4.dp)
        Text(
            text = context.name,
            modifier = lightModifier.wrapContentHeight(),
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        LightView(
            lightModifier,
            Color.Red,
            context.red.collectAsState(false)
        )
        LightView(
            lightModifier,
            Amber,
            context.amber.collectAsState(false)
        )
        LightView(
            lightModifier,
            Color.Green,
            context.green.collectAsState(false)
        )
    }
}