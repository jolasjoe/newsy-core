pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:3.5.2")
            } else if (requested.id.id == "com.squareup.sqldelight"){
                useModule("com.squareup.sqldelight:gradle-plugin:1.4.2")
            }
        }
    }
}
rootProject.name = "newsy-core"
