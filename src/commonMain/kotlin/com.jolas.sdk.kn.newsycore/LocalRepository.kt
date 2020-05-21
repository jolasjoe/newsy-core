package com.jolas.sdk.kn.newsycore

import com.squareup.sqldelight.db.SqlDriver

expect val sqlDriver: SqlDriver

interface LocalRepositoryProtocol{
    fun insertItem(item: Item)
    fun getItem(itemId: Long): Item
}

class LocalRepository: LocalRepositoryProtocol {
    val database: NewsyDatabase
    constructor(){
        database = NewsyDatabase.invoke(sqlDriver)
        database.itemQueries.createItemTable()
    }

    override fun insertItem(item: Item) {
        val database = NewsyDatabase.invoke(sqlDriver)
        database.itemQueries.insertItem(
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

    override fun getItem(itemId: Long): Item {
        val database = NewsyDatabase.invoke(sqlDriver)
        val itemRow = database.itemQueries.queryItemWithId(itemId).executeAsOne()
        print("ItemRow: $itemRow")
        val kids = itemRow.kids?.split(",")?.map {
            it.trim().toLong()
        }?.toTypedArray()
        val parts = itemRow.parts?.split(",")?.map {
            it.trim().toInt()
        }?.toTypedArray()

        return Item(
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
    }
}