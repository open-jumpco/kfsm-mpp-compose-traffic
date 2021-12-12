@file:Suppress("FunctionName")

package io.jumpco.open.kfsm.mpp.example.traffic.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.State
import io.jumpco.open.kfsm.mpp.example.traffic.model.TrafficIntersectionViewModel
import io.jumpco.open.kfsm.mpp.example.traffic.web.view.Intersection

@OptIn(InternalComposeApi::class)
@Composable
fun App(portraitMode: State<Boolean>, intersectionViewModel: TrafficIntersectionViewModel) {
    Intersection(intersectionViewModel, portraitMode.value)
}
