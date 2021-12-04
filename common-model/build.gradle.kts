import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

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
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
                api("io.github.microutils:kotlin-logging:2.0.10")
                api("io.jumpco.open:kfsm:1.5.2")

            }
        }
        named("jvmMain") {
        }
        named("jsMain") {
        }
    }
}
android {
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}