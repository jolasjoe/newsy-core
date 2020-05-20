package com.jolas.sdk.kn.newsycore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ItemType(val value: String){
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
    val lastRefreshTime: Int?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as User

        if (id != other.id) return false
        if (delay != other.delay) return false
        if (created != other.created) return false
        if (karma != other.karma) return false
        if (about != other.about) return false
        if (submitted != null) {
            if (other.submitted == null) return false
            if (!submitted.contentEquals(other.submitted)) return false
        } else if (other.submitted != null) return false
        if (lastRefreshTime != other.lastRefreshTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (delay ?: 0)
        result = 31 * result + (created ?: 0)
        result = 31 * result + (karma ?: 0)
        result = 31 * result + (about?.hashCode() ?: 0)
        result = 31 * result + (submitted?.contentHashCode() ?: 0)
        result = 31 * result + (lastRefreshTime ?: 0)
        return result
    }
}

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
    val lastRefreshTime: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Item

        if (id != other.id) return false
        if (isDeleted != other.isDeleted) return false
        if (itemType != other.itemType) return false
        if (by != other.by) return false
        if (time != other.time) return false
        if (text != other.text) return false
        if (isDead != other.isDead) return false
        if (parentItemId != other.parentItemId) return false
        if (poll != other.poll) return false
        if (kids != null) {
            if (other.kids == null) return false
            if (!kids.contentEquals(other.kids)) return false
        } else if (other.kids != null) return false
        if (urlString != other.urlString) return false
        if (score != other.score) return false
        if (title != other.title) return false
        if (parts != null) {
            if (other.parts == null) return false
            if (!parts.contentEquals(other.parts)) return false
        } else if (other.parts != null) return false
        if (descendants != other.descendants) return false
        if (lastRefreshTime != other.lastRefreshTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + isDeleted.hashCode()
        result = 31 * result + itemType.hashCode()
        result = 31 * result + (by?.hashCode() ?: 0)
        result = 31 * result + time.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + isDead.hashCode()
        result = 31 * result + (parentItemId?.hashCode() ?: 0)
        result = 31 * result + (poll?.hashCode() ?: 0)
        result = 31 * result + (kids?.contentHashCode() ?: 0)
        result = 31 * result + (urlString?.hashCode() ?: 0)
        result = 31 * result + (score ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (parts?.contentHashCode() ?: 0)
        result = 31 * result + (descendants ?: 0)
        result = 31 * result + (lastRefreshTime ?: 0)
        return result
    }
}