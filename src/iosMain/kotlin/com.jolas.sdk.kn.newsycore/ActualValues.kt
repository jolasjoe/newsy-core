package com.jolas.sdk.kn.newsycore

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

actual class NewsyPlatformDependencies actual constructor(private val sqlDriver: SqlDriver){
    constructor(): this(sqlDriver = NativeSqliteDriver(NewsyDatabase.Schema, "newsy.db"))
    actual fun getSqlDriver(): SqlDriver{
        return sqlDriver
    }
}

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