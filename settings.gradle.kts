pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins {
        val kotlinVersion = "1.7.10"
        val agpVersion = "7.0.4"
        val composeVersion = "1.2.0"

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)
        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)
        id("org.jetbrains.compose").version(composeVersion)
    }
}

rootProject.name = "kfsm-mpp-compose-traffic"

include(":common-model")
include(":common-ui")
include(":android")
include(":desktop")
include(":web")


