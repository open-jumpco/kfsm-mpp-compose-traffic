plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

val coroutinesVersion: String by project

android {
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
configurations {
    // all.exclude
}
dependencies {
    implementation(project(":common-model"))
    implementation(project(":common-ui"))
    implementation(compose.material)
    implementation(compose.ui)
    implementation(compose.uiTooling)
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.13")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")


}

android {
    defaultConfig {
        applicationId = "io.jumpco.open.kfsm.mpp.example.traffic.android"
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}