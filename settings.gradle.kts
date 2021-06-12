pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:3.5.2")
            } else if (requested.id.id == "com.squareup.sqldelight"){
                useModule("com.squareup.sqldelight:gradle-plugin:1.4.4")
            }
        }
    }
}
rootProject.name = "newsy-core"
