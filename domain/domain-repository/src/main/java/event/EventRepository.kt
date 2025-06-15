package event

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EventRepository {
    suspend fun getEvents(): Result<List<Event>>
    suspend fun getFavoriteEvents(): Flow<List<Event>>
    suspend fun toggleFavorite(eventId: String): Result<Unit>
    suspend fun getEventsByDate(date: LocalDate): Flow<List<Event>>
    suspend fun searchEvents(filter: EventFilter): Result<List<Event>>
}