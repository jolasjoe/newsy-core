import java.util.*

plugins {
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("multiplatform")version("1.4.32")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.kotlin.plugin.serialization")version("1.4.32")
    id("org.jetbrains.dokka")version("1.4.32")
}

repositories {
    mavenCentral()
    jcenter()
    google()
}

group = "io.github.jolasjoe"
version = "1.0.0"

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
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
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
        packageName = "io.jolas.newsycore"
    }
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("Newsy Core")
            description.set("HN API library for multiple platforms using KMP")
            url.set("https://github.com/jolasjoe/newsy-core")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("jolasjoe")
                    name.set("Jolas Joe")
                    email.set("jolasjoe@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/jolasjoe/newsy-core")
            }

        }
    }
}

signing {
    sign(publishing.publications)
}