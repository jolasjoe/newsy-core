package com.jolas.sdk.kn.newsycore

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

actual val httpClient = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}
actual val sqlDriver: SqlDriver
    get() {
        val sqlDriver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        NewsyDatabase.Schema.create(sqlDriver)
        return sqlDriver
    }

actual val mainCoroutineScope = MainScope()