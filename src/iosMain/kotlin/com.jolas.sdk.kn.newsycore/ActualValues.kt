package com.jolas.sdk.kn.newsycore

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.ios.Ios
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

@UnstableDefault
actual val httpClient = HttpClient(Ios) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(
            Json(
                JsonConfiguration.Default.copy(
                    useArrayPolymorphism = true,
                    ignoreUnknownKeys = true,
                    isLenient = true
                )
            )
        )
    }
}

actual val sqlDriver: SqlDriver = NativeSqliteDriver(NewsyDatabase.Schema, "newsy.db")

private class MainDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) {
            block.run()
        }
    }
}

class MainScope : CoroutineScope {
    private val dispatcher = MainDispatcher()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job
}

actual val mainCoroutineScope: CoroutineScope = MainScope()