package com.jolas.sdk.kn.newsycore

import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.launch

expect val sqlDriver: SqlDriver

interface LocalRepositoryProtocol {
    fun insertKeyValue(key: String, value: String)
    fun getValue(key: String, onSuccess: (String?) -> Unit)
    fun insertItem(item: Item)
    fun getItem(itemId: Long, onSuccess: (Item?) -> Unit)
}

class LocalRepository : LocalRepositoryProtocol {
    private val database = NewsyDatabase.invoke(sqlDriver)
    private val dataQueries = database.itemQueries

    init {
        dataQueries.createKeyValueTable()
        dataQueries.createItemTable()
    }

    override fun insertKeyValue(key: String, value: String) {
        mainCoroutineScope.launch {
            executeInsertKeyValue(key, value)
        }
    }

    private suspend fun executeInsertKeyValue(key: String, value: String) {
        dataQueries.insertKeyValue(key, value)
    }

    override fun getValue(key: String, onSuccess: (String?) -> Unit) {
        mainCoroutineScope.launch {
            executeGetValue(key, onSuccess)
        }
    }

    private suspend fun executeGetValue(key: String, onSuccess: (String?) -> Unit) {
        onSuccess(dataQueries.queryValueForKey(key).executeAsOneOrNull())
    }

    override fun insertItem(item: Item) {
        mainCoroutineScope.launch {
            executeInsertItem(item)
        }
    }

    private suspend fun executeInsertItem(item: Item) {
        dataQueries.insertItem(
            item.id,
            if (item.isDeleted) 1 else 0,
            item.itemType.value,
            item.by,
            item.time,
            item.text,
            if (item.isDead) 1 else 0,
            item.parentItemId,
            item.poll,
            item.kids?.joinToString(),
            item.urlString,
            item.score?.toLong(),
            item.title,
            item.parts?.toString(),
            item.descendants?.toLong(),
            item.lastRefreshTime
        )
    }

    override fun getItem(itemId: Long, onSuccess: (Item?) -> Unit) {
        mainCoroutineScope.launch {
            executeGetItem(itemId, onSuccess)
        }
    }

    private suspend fun executeGetItem(itemId: Long, onSuccess: (Item?) -> Unit) {
        val itemRow = dataQueries.queryItemWithId(itemId).executeAsOneOrNull()
        if (itemRow != null) {
            val kids = itemRow.kids?.split(",")?.map {
                it.trim().toLong()
            }?.toTypedArray()
            val parts = itemRow.parts?.split(",")?.map {
                it.trim().toInt()
            }?.toTypedArray()

            onSuccess(
                Item(
                    id = itemRow.id,
                    isDeleted = itemRow.isDeleted == 1L,
                    itemType = ItemType.valueOf(itemRow.itemType.toUpperCase()),
                    by = itemRow.by,
                    time = itemRow.time,
                    text = itemRow.text,
                    isDead = itemRow.isDead == 1L,
                    parentItemId = itemRow.parentItemId,
                    poll = itemRow.poll,
                    kids = kids,
                    urlString = itemRow.urlString,
                    score = itemRow.score?.toInt(),
                    title = itemRow.title,
                    parts = parts,
                    descendants = itemRow.descendants?.toInt(),
                    lastRefreshTime = itemRow.lastRefreshTime
                )
            )
        }
    }
}