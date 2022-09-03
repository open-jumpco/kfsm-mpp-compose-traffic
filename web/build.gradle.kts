import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension


plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val coroutinesVersion: String by project

kotlin {
    js(IR) {
        browser {
            useCommonJs()
            binaries.executable()
        }
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
            }
        }
    }
    sourceSets {
        named("jsMain") {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.web.svg)
                implementation(compose.runtime)
                implementation(project(":common-model"))
                implementation("io.github.microutils:kotlin-logging-js:2.0.13")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")


//                implementation(npm("copy-webpack-plugin", "^10.0.0"))
//                implementation(npm("@material/theme", "^13.0.0"))
//                implementation(devNpm("sass", "^1.42.1"))
//                implementation(devNpm("sass-loader", "^12.3.0"))
            }
        }
    }
}
tasks.register<Copy>("development") {
    dependsOn("jsBrowserDevelopmentExecutableDistribution")
    into("${project.buildDir}/development")
    from("${project.buildDir}/developmentExecutable")
    from("${project.buildDir}/compileSync/main/developmentExecutable/kotlin")
}

afterEvaluate {
    rootProject.extensions.configure<NodeJsRootExtension> {
        versions.webpackDevServer.version = "4.0.0"
        versions.webpackCli.version = "4.9.0"
    }

}