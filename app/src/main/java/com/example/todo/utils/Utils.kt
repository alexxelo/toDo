package com.example.todo.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun getCurrentTime(): LocalTime {
  return LocalTime.now()
}
fun timestampToZonedDateTime(timestamp: Long): ZonedDateTime {
  val instant = Instant.ofEpochMilli(timestamp)
  return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
}
fun timestampToLocalDate(timestamp: Long): LocalDate {
  val instant = Instant.ofEpochMilli(timestamp)
  return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Long.toFormattedDateTime(): String {
  val instant = Instant.ofEpochMilli(this)
  val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
  val formatter = DateTimeFormatter.ofPattern("HH:mm")
  return localDateTime.format(formatter)
}

fun Long.toFormattedDate(): String {
  val instant = Instant.ofEpochMilli(this)
  val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
  val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
  return localDateTime.format(formatter)
}

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
  val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
  LaunchedEffect(state) {
    snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
      .filterNotNull()
      .collect { month -> visibleMonth.value = month }
  }
  return visibleMonth.value
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
  get() {
    val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
    return if (visibleItemsInfo.isEmpty()) {
      emptyList()
    } else {
      val lastItem = visibleItemsInfo.last()
      val viewportSize = this.viewportEndOffset + this.viewportStartOffset
      if (lastItem.offset + lastItem.size > viewportSize) {
        visibleItemsInfo.removeLast()
      }
      val firstItem = visibleItemsInfo.firstOrNull()
      if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
        visibleItemsInfo.removeFirst()
      }
      visibleItemsInfo.map { it.month }
    }
  }

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
  return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
    if (uppercase) value.uppercase(Locale.ENGLISH) else value
  }
}
