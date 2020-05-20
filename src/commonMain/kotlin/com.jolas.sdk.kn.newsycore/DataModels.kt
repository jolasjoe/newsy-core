package com.jolas.sdk.kn.newsycore

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.serializer

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
    val isDeleted: Boolean = false, //Needs mapping
    @SerialName("type")
    val itemType: ItemType = ItemType.STORY, //Needs mapping
    val by: String? = null,
    val time: Long,
    val text: String? = null,
    @SerialName("dead")
    val isDead: Boolean = false, //Needs mapping
    @SerialName("parent")
    val parentItemId: Long? = null, //Needs mapping
    val poll: Long? = null,
    val kids: Array<Long>? = null,
    @SerialName("url")
    val urlString: String? = null, //Needs mapping
    val score: Int? = null,
    val title: String? = null,
    @SerialName("parts")
    val parts: Array<Int>? = null,
    val descendants: Int? = null,
    val lastRefreshTime: Int? = null
)/* {

    @InternalSerializationApi
    @ImplicitReflectionSerializer
    @Serializer(forClass = Item::class)
    companion object : KSerializer<Item> {
        override val descriptor: SerialDescriptor = SerialDescriptor("Item"){
            element<Long>("id")
            element<Boolean>("deleted")
            element<String>("type")
            element<String>("by")
            element<Long>("time")
            element<String>("text")
            element<Boolean>("dead")
            element<Long>("parent")
            element<Long>("poll")
            element<Array<Long>>("kids")
            element<String>("url")
            element<Int>("score")
            element<String>("title")
            element<Array<Long>>("parts")
            element<Int>("descendants")
        }

        override fun deserialize(decoder: Decoder): Item {
            var id: Long = -1
            var isDeleted: Boolean = false
            var type: String = ItemType.STORY.value
            var by: String? = null
            var time: Long = -1
            var text: String? = null
            var isDead: Boolean = false
            var parentItemId: Long? = null
            var poll: Long? = null
            var kids: Array<Long>? = null
            var urlString: String? = null
            var score: Int? = null
            var title: String? = null
            var parts: Array<Int>? = null
            var descendants: Int? = null

            val itemDecoder = decoder.beginStructure(descriptor = descriptor)
            loop@ while (true){
                when (val i = itemDecoder.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> id = itemDecoder.decodeLongElement(descriptor, i)
                    1 -> isDeleted = itemDecoder.decodeBooleanElement(descriptor, i)
                    2 -> type = itemDecoder.decodeStringElement(descriptor, i)
                    3 -> by = null
                    4 -> time = itemDecoder.decodeLongElement(descriptor, i)
                    5 -> text = itemDecoder.decodeStringElement(descriptor, i)
                    6 -> isDead = itemDecoder.decodeBooleanElement(descriptor, i)
                    7 -> parentItemId = itemDecoder.decodeLongElement(descriptor, i)
                    8 -> poll = itemDecoder.decodeLongElement(descriptor, i)
                    9 -> kids = itemDecoder.decodeSerializableElement(descriptor, i, ArraySerializer(Long.serializer()))
                    10 -> urlString = itemDecoder.decodeStringElement(descriptor, i)
                    11 -> score = itemDecoder.decodeIntElement(descriptor, i)
                    12 -> title = itemDecoder.decodeStringElement(descriptor, i)
                    13 -> parts = itemDecoder.decodeSerializableElement(descriptor, i, ArraySerializer(Int.serializer()))
                    14 -> descendants = itemDecoder.decodeIntElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
            itemDecoder.endStructure(descriptor)

            val itemType = when (type){
                "story" -> ItemType.STORY
                "job" -> ItemType.JOB
                "comment" -> ItemType.COMMENT
                "poll" -> ItemType.POLL
                "pollopt" -> ItemType.POLLOPT
                else -> ItemType.STORY
            }

            return Item(
                id = id,
                isDeleted = isDeleted,
                itemType = itemType,
                by = by,
                time = time,
                text = text,
                isDead = isDead,
                parentItemId = parentItemId,
                poll = poll,
                kids = kids,
                urlString = urlString,
                score = score,
                title = title,
                parts = parts,
                descendants = descendants,
                lastRefreshTime = 0
            )
        }
    }
}*/