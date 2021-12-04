package io.jumpco.open.kfsm.mpp.example.traffic

enum class ComposePlatform {
    ANDROID,
    DESKTOP,
    WEB
}

expect fun getPlatformName(): ComposePlatform