plugins {
    id("org.jetbrains.compose") version "0.4.0"
    id("com.android.application")
    kotlin("android")
}

group = "io.jumpco.open.kfsm.mpp.example.traffic"
version = "1.0"

repositories {
    google()
}

dependencies {
    implementation(project(":common"))
//    compose_version = '1.0.1'
//    material_version = '1.0.1'
//    activity_version = '1.3.1'
//    lifecycle_version = '2.3.1'

    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.activity:activity-ktx:1.3.1")
    implementation("androidx.compose.ui:ui:1.0.1")
    implementation("androidx.compose.material:material:1.0.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")
    implementation("org.slf4j:slf4j-api:1.7.32")
    runtimeOnly("org.slf4j:slf4j-android:1.7.32")
    implementation("io.jumpco.open:kfsm-jvm:1.5.2")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "io.jumpco.open.kfsm.mpp.example.traffic.android"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}