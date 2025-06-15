import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun formatDate(date: LocalDate): String = date.format(dateFormatter)
    fun formatTime(dateTime: LocalDateTime): String = dateTime.format(timeFormatter)
    fun formatDateTime(dateTime: LocalDateTime): String = dateTime.format(dateTimeFormatter)
}