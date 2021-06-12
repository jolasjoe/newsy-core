package io.jolas.newsycore

import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineScope

expect class NewsyPlatformDependencies(sqlDriver: SqlDriver) {
    fun getSqlDriver(): SqlDriver
}

expect val mainCoroutineScope: CoroutineScope

class NewsyRepository(platformDependencies: NewsyPlatformDependencies) {
    private val localRepository: LocalRepositoryProtocol = LocalRepository(platformDependencies)
    private val remoteRepository: RemoteRepositoryProtocol = RemoteRepository()

    fun getStories(storiesType: StoriesType, onSuccess: (Array<Int>) -> Unit, onFailure: () -> Unit) {
        localRepository.getValue(storiesType.name) { storiesItemIds ->
            if (storiesItemIds != null) {
                onSuccess(storiesItemIds.split(",").map { value -> value.toInt() }.toTypedArray())
            }
        }

        remoteRepository.getStories(storiesType, {
            onSuccess(it)
            localRepository.insertKeyValue(storiesType.name, it.joinToString(","))
        }, {
            onFailure()
        })
    }

    fun getItem(itemId: Long, onSuccess: (Item) -> Unit, onFailure: () -> Unit) {
        localRepository.getItem(itemId) {
            if (it != null) {
                println("successful getItem local response")
                onSuccess(it)
            }
        }

        remoteRepository.getItem(itemId, {
            onSuccess(it)
            localRepository.insertItem(it)
        }, {
            onFailure()
        })
    }
}