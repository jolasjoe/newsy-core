package com.jolas.sdk.kn.newsycore

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

expect val httpClient: HttpClient

enum class StoriesType {
    TopStories, BestStories, NewStories, ShowStories, AskStories, JobStories
}

interface RemoteRepositoryProtocol {
    val baseHNFBUrl: String
        get() = "https://hacker-news.firebaseio.com/v0/"

    fun getStories(type: StoriesType, onSuccess: (Array<Int>) -> Unit, onFailure: () -> Unit)
    fun getItem(itemId: Long, onSuccess: (Item) -> Unit, onFailure: () -> Unit)
}

class RemoteRepository : RemoteRepositoryProtocol {
    @UnstableDefault
    override fun getStories(type: StoriesType, onSuccess: (Array<Int>) -> Unit, onFailure: () -> Unit) {
        val path: String = when (type) {
            StoriesType.TopStories -> "topstories.json"
            StoriesType.BestStories -> "beststories.json"
            StoriesType.NewStories -> "newstories.json"
            StoriesType.ShowStories -> "showstories.json"
            StoriesType.AskStories -> "askstories.json"
            StoriesType.JobStories -> "jobstories.json"
        }
        mainCoroutineScope.launch {
            executeGetStories(path, onSuccess)
        }
    }

    @UnstableDefault
    private suspend fun executeGetStories(path: String, onSuccess: (Array<Int>) -> Unit) {
        val resultValue = httpClient.get<Array<Int>> {
            url(urlString = baseHNFBUrl + path)
        }
        onSuccess(resultValue)
    }

    @UnstableDefault
    override fun getItem(itemId: Long, onSuccess: (Item) -> Unit, onFailure: () -> Unit) {
        val path = "item/$itemId.json"
        mainCoroutineScope.launch {
            val resultItem = httpClient.get<Item> {
                url(urlString = baseHNFBUrl + path)
            }
            onSuccess(resultItem)
        }
    }
}