package io.jumpco.open.kfsm.mpp.example.traffic

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import io.jumpco.open.kfsm.mpp.example.traffic.model.TrafficIntersectionViewModel
import io.jumpco.open.kfsm.mpp.example.traffic.view.Intersection


@Composable
fun App(portraitMode: State<Boolean>, intersectionViewModel: TrafficIntersectionViewModel) {
    MaterialTheme {
        Scaffold {
            Surface(color = MaterialTheme.colors.background) {
                Intersection(intersectionViewModel, portraitMode.value)
            }
        }
    }
}
