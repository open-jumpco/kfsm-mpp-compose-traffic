package io.jumpco.open.kfsm.mpp.example.traffic.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.kfsm.compose.traffic.fsm.TrafficIntersectionService
import com.example.kfsm.compose.traffic.fsm.TrafficLightService
import io.jumpco.open.kfsm.mpp.example.traffic.activity.Intersection
import io.jumpco.open.kfsm.mpp.example.traffic.view.TrafficIntersectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private val intersectionModel = TrafficIntersectionService(
    listOf(
        TrafficLightService("1"),
        TrafficLightService("2"),
        TrafficLightService("3")
    )
)
private val intersectionViewModel = TrafficIntersectionViewModel(intersectionModel)
private val portraitMode = MutableStateFlow(true)

class MainActivity : AppCompatActivity() {
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        portraitMode.value = Configuration.ORIENTATION_PORTRAIT == newConfig.orientation
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Default).launch {
            intersectionViewModel.setupIntersection()
        }
        setContent {
            MainWindow()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainWindow() {
    MaterialTheme {
        val portrait =
            portraitMode.collectAsState(Configuration.ORIENTATION_PORTRAIT == LocalConfiguration.current.orientation)
        Scaffold {
            Surface(color = MaterialTheme.colors.background) {
                Intersection(intersectionViewModel, portrait.value)
            }
        }
    }

}

