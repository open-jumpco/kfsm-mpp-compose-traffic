import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val coroutinesVersion: String by project

kotlin {
    android()
    jvm()
    js(IR) {
        browser {
            useCommonJs()
            binaries.executable()
        }
    }
    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api("io.github.microutils:kotlin-logging:2.0.10")
                api("io.jumpco.open:kfsm:1.6.1-SNAPSHOT")

            }
        }
        named("jvmMain") {
        }
        named("jsMain") {
        }
    }
}
android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}