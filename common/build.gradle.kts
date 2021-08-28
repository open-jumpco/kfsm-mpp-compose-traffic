import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.4.0"
    id("com.android.library")
    id("kotlin-android-extensions")
}

group = "io.jumpco.open.kfsm.mpp.example.traffic"
version = "1.0"

repositories {
    google()
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
                api("io.github.microutils:kotlin-logging:2.0.10")
                api("io.jumpco.open:kfsm:1.5.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.3.1")
                api("androidx.core:core-ktx:1.6.0")
                runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
                runtimeOnly("io.github.microutils:kotlin-logging-jvm:2.0.10")
                runtimeOnly("org.slf4j:slf4j-api:1.7.32")
                runtimeOnly("org.slf4j:slf4j-android:1.7.32")
                implementation("io.jumpco.open:kfsm-jvm:1.5.2")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }
        val desktopMain by getting {
            dependencies {
                runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
                runtimeOnly("io.github.microutils:kotlin-logging-jvm:2.0.10")
                runtimeOnly("ch.qos.logback:logback-classic:1.2.5")
                implementation("io.jumpco.open:kfsm-jvm:1.5.2")
            }
        }
        val desktopTest by getting
    }
    targets.all {
        compilations.all {
            kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalMultiplatform"
            kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
}