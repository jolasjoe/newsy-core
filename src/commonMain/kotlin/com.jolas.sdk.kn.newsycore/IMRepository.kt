package com.jolas.sdk.kn.newsycore

import kotlinx.coroutines.CoroutineScope

expect val mainCoroutineScope: CoroutineScope

class IMRepository {
    private val localRepository: LocalRepositoryProtocol = LocalRepository()
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