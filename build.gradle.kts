import io.jumpco.open.kfsm.gradle.VizPluginExtension
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.android.tools.build:gradle:4.2.0")
    }
}
plugins {
    id("io.jumpco.open.kfsm.viz-plugin") version "1.5.2.4"
}
group = "io.jumpco.open.kfsm.mpp.example.traffic"
version = "1.0"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

configure<VizPluginExtension> {
    fsm("TrafficLightFSM") {
        input = file("common/src/commonMain/kotlin/io/jumpco/open/kfsm/mpp/example/traffic/fsm/TrafficLightFSM.kt")
        isGeneratePlantUml = true // (2)
        isGenerateAsciidoc = true // (3)
        output = "traffic-light" // (4)
        outputFolder = file("generated") // (5)
    }
    fsm("TrafficIntersectionFSM") {
        input = file("common/src/commonMain/kotlin/io/jumpco/open/kfsm/mpp/example/traffic/fsm/TrafficIntersectionFSM.kt")
        isGeneratePlantUml = true // (2)
        isGenerateAsciidoc = true // (3)
        output = "traffic-intersection" // (4)
        outputFolder = file("generated") // (5)
    }
}