package io.jumpco.open.kfsm.mpp.example.traffic

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.currentComposer
import io.jumpco.open.kfsm.mpp.example.traffic.activity.Intersection
import io.jumpco.open.kfsm.mpp.example.traffic.view.TrafficIntersectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(InternalComposeApi::class)
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
