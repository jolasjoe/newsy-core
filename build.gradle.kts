import org.jetbrains.kotlin.gradle.tasks.*
plugins {
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("multiplatform") version "1.4.0"
    id("maven-publish")
    id("org.jetbrains.kotlin.plugin.serialization")version("1.4.0")
}

repositories {
    mavenCentral()
    jcenter()
    google()
}

group = "com.jolas.sdk.kn.newsycore"
version = "0.0.1-alpha-10"

val ktor_version = "1.4.0"
val sqldelight_version = "1.4.2"

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "0.0.1-alpha-10"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

kotlin {

    android {
        publishLibraryVariants("release", "debug")
    }

    iosX64("ios") {
        binaries {
            framework()
        }
    }

    mingwX64( "mingw" ) {
        binaries {
            sharedLib()
        }
    }

//    tasks.create("debugFatFramework", FatFrameworkTask::class) {
//        destinationDir = buildDir.resolve("fat-framework/debug")
//        // Specify the frameworks to be merged.
//        from(
//            iosArm64().binaries.getFramework("DEBUG"),
//            iosX64().binaries.getFramework("DEBUG")
//        )
//    }
//
//    tasks.create("releaseFatFramework", FatFrameworkTask::class) {
//        destinationDir = buildDir.resolve("fat-framework/release")
//        // Specify the frameworks to be merged.
//        from(
//            iosArm64().binaries.getFramework("RELEASE"),
//            iosX64().binaries.getFramework("RELEASE")
//        )
//    }

    sourceSets {
        val commonMain by getting  {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                implementation("com.squareup.sqldelight:runtime:$sqldelight_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
                implementation("com.squareup.sqldelight:android-driver:1.4.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktor_version")
                implementation("com.squareup.sqldelight:native-driver:1.4.2")
            }
        }
        val mingwMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktor_version")
                implementation("com.squareup.sqldelight:native-driver:1.4.2")
            }
        }
    }
}

sqldelight {
    database("NewsyDatabase") {
        packageName = "com.jolas.sdk.kn.newsycore"
    }
}