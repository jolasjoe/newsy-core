package io.jolas.newsycore

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.MainScope

actual class NewsyPlatformDependencies actual constructor(private val sqlDriver: SqlDriver){
    constructor(context: Context): this(sqlDriver = AndroidSqliteDriver(NewsyDatabase.Schema, context = context, name = "newsy.db"))
    actual fun getSqlDriver(): SqlDriver{
        return sqlDriver
    }
}

actual val mainCoroutineScope = MainScope()