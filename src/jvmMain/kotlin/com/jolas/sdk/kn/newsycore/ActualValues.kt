package com.jolas.sdk.kn.newsycore

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.coroutines.MainScope

actual val httpClient = HttpClient(OkHttp){
    install(JsonFeature){
        serializer = KotlinxSerializer()
    }
}
actual val remoteRepoCoroutineScope = MainScope()