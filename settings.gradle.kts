pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}
rootProject.name = "kfsm-mpp-compose-traffic"
include(":common-model")
include(":common-ui")
include(":android")
include(":desktop")
include(":web")


