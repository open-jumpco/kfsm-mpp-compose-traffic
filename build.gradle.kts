import io.jumpco.open.kfsm.gradle.VizPluginExtension
buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.0.1")
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath(kotlin("gradle-plugin", version = "1.6.10"))
    }
}
plugins {
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