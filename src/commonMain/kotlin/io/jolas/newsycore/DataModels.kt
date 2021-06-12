package io.jolas.newsycore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ItemType(val value: String) {
    @SerialName("job")
    JOB("job"),

    @SerialName("story")
    STORY("story"),

    @SerialName("comment")
    COMMENT("comment"),

    @SerialName("poll")
    POLL("poll"),

    @SerialName("pollopt")
    POLLOPT("pollopt")
}

@Serializable
data class User(
    val id: String?, //The user's unique username. Case-sensitive. Required.
    val delay: Int?, //Delay in minutes between a comment's creation and its visibility to other users.
    val created: Int?, //Creation date of the user, in Unix Time.
    val karma: Int?, //The user's karma.
    val about: String?, //The user's optional self-description. HTML.
    val submitted: Array<Int>?, //List of the user's stories, polls and comments.
    val lastRefreshTime: Long?
)

@Serializable
data class Item(
    val id: Long,
    @SerialName("deleted")
    val isDeleted: Boolean = false,
    @SerialName("type")
    val itemType: ItemType = ItemType.STORY,
    val by: String? = null,
    val time: Long,
    val text: String? = null,
    @SerialName("dead")
    val isDead: Boolean = false,
    @SerialName("parent")
    val parentItemId: Long? = null,
    val poll: Long? = null,
    val kids: Array<Long>? = null,
    @SerialName("url")
    val urlString: String? = null,
    val score: Int? = null,
    val title: String? = null,
    @SerialName("parts")
    val parts: Array<Int>? = null,
    val descendants: Int? = null,
    val lastRefreshTime: Long? = null
)