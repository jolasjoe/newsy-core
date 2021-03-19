package com.jolas.sdk.kn.newsycore

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlinx.coroutines.*

actual class NewsyPlatformDependencies actual constructor(private val sqlDriver: SqlDriver){
    constructor(): this(sqlDriver = NativeSqliteDriver(NewsyDatabase.Schema, "newsy.db"))
    actual fun getSqlDriver(): SqlDriver{
        return sqlDriver
    }
}

actual val mainCoroutineScope: CoroutineScope = MainScope()