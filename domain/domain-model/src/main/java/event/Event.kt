package event

import java.time.LocalDateTime

data class Event(
    val id: String,
    val title: String,
    val artist: String,
    val eventName: String?,
    val address: String,
    val dateTime: LocalDateTime,
    val imageUrl: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isFavorite: Boolean = false
)