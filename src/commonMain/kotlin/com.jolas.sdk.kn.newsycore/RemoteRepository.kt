package com.jolas.sdk.kn.newsycore

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

expect val httpClient: HttpClient
expect val remoteRepoCoroutineScope: CoroutineScope

enum class StoriesType{
    TopStories, BestStories, NewStories, ShowStories, AskStories, JobStories
}

interface RemoteRepositoryProtocol{
    val baseHNFBUrl : String
        get() = "https://hacker-news.firebaseio.com/v0/"

    fun fetchStories(type: StoriesType, onSuccess: (Array<Int>)->Unit, onFailure: ()->Unit)
    fun fetchItem(itemId: Int, onSuccess: (Item) -> Unit, onFailure: () -> Unit)
}

class RemoteRepository: RemoteRepositoryProtocol {

    @UnstableDefault
    override fun fetchStories(type: StoriesType, onSuccess: (Array<Int>) -> Unit, onFailure: ()->Unit) {
        val path: String = when (type){
            StoriesType.TopStories -> "topstories.json"
            StoriesType.BestStories -> "beststories.json"
            StoriesType.NewStories -> "newstories.json"
            StoriesType.ShowStories -> "showstories.json"
            StoriesType.AskStories -> "askstories.json"
            StoriesType.JobStories -> "jobstories.json"
        }
        remoteRepoCoroutineScope.launch {
            val resultValue = httpClient.get<Array<Int>> {
                url(urlString = baseHNFBUrl + path)
            }
            onSuccess(resultValue)
        }
    }

    @UnstableDefault
    override fun fetchItem(itemId: Int, onSuccess: (Item) -> Unit, onFailure: () -> Unit) {
        val path = "item/$itemId.json"
        remoteRepoCoroutineScope.launch {
            val resultValue = httpClient.get<Item> {
                url(urlString = baseHNFBUrl + path)
            }
            onSuccess(resultValue)
        }
    }
}