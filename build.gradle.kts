plugins {
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("multiplatform") version "1.4.31"
    id("maven-publish")
    id("org.jetbrains.kotlin.plugin.serialization")version("1.4.31")
}

repositories {
    mavenCentral()
    jcenter()
    google()
}

group = "com.jolas.sdk.kn.newsycore"
version = "0.0.1-alpha-10"

val ktorVersion = "1.5.2"
val sqlDelightVersion = "1.4.4"

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(30)
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

    macosX64("macos") {
        binaries {
            framework()
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
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
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
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val mingwMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val macosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
    }
}

sqldelight {
    database("NewsyDatabase") {
        packageName = "com.jolas.sdk.kn.newsycore"
    }
}