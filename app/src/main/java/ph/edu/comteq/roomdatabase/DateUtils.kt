package ph.edu.comteq.roomdatabase

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



object DateUtils {
    private val dateFormat =
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val timeFormat =
        SimpleDateFormat("h:mm a", Locale.getDefault())
    private val dataTimeFormat =
        SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        return dataTimeFormat.format(Date(timestamp))
    }
}
