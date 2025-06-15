package event

data class EventFilter(
    val artist: String? = null,
    val eventName: String? = null,
    val dateRange: DateRange? = null
)

data class DateRange(
    val startDate: java.time.LocalDate,
    val endDate: java.time.LocalDate
)