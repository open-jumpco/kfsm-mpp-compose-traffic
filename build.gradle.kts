import io.jumpco.open.kfsm.gradle.VizPluginExtension
plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("io.jumpco.open.kfsm.viz-plugin") version "1.5.2.4"
}
group = "io.jumpco.open.kfsm.mpp.example.traffic"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

val commonModelProjectFolder = project(":common-model").projectDir

val fsmFolder =  "$commonModelProjectFolder/src/commonMain/kotlin/io/jumpco/open/kfsm/mpp/example/traffic/fsm"

configure<VizPluginExtension> {
    fsm("TrafficLightFSM") {
        input = file("${fsmFolder}/TrafficLightFSM.kt")
        isGeneratePlantUml = true
        isGenerateAsciidoc = true
        output = "traffic-light"
        outputFolder = file("generated")
    }
    fsm("TrafficIntersectionFSM") {
        input = file("${fsmFolder}/TrafficIntersectionFSM.kt")
        isGeneratePlantUml = true
        isGenerateAsciidoc = true
        output = "traffic-intersection"
        outputFolder = file("generated")
    }
}