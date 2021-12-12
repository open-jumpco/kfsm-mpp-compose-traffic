@file:Suppress("FunctionName")

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div

@Composable
fun PortraitContainer(
    content: @Composable () -> Unit
) {
    Div({
        classes("portrait", "container")
    }) {
        content()
    }
}

@Composable
fun LandscapeContainer(
    content: @Composable () -> Unit
) {
    Div({
        classes("landscape", "container")
    }) {
        content()
    }
}