package com.jolas.sdk.kn.newsycore

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.MainScope

actual class ZIMPlatformDependencies actual constructor(private val sqlDriver: SqlDriver){
    constructor(context: Context): this(sqlDriver = AndroidSqliteDriver(NewsyDatabase.Schema, context = context, name = "im.db"))
    actual fun getSqlDriver(): SqlDriver{
        return sqlDriver
    }
}

actual val mainCoroutineScope = MainScope()