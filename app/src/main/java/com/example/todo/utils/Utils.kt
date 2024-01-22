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
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

class Utils {
  companion object {
    fun getCurrentDate(): LocalDate {
      return LocalDate.now()
    }

    fun getCurrentTime(): LocalTime {
      return LocalTime.now()
    }

    fun capitalizeFirstLetter(input: String?): String {
      if (input.isNullOrBlank()) {
        return ""
      }
      return input.substring(0, 1).uppercase() + input.substring(1).lowercase()
    }

    fun getDaysInMonth(): Int {
      val year = getCurrentDate().year
      val month = getCurrentDate().monthValue
      val yearMonth = YearMonth.of(year, month)
      return yearMonth.lengthOfMonth()
    }

    fun timestampToZonedDateTime(timestamp: Long): ZonedDateTime {
      val instant = Instant.ofEpochMilli(timestamp)
      return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    fun timestampToLocalDate(timestamp: Long): LocalDate {
      val instant = Instant.ofEpochMilli(timestamp)
      return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }


    fun getLocalDateTime(date: LocalDate, time: LocalTime): LocalDateTime {
      return LocalDateTime.of(date, time)
    }

    // Добавляем еще одну перегрузку, которая принимает timestamp в миллисекундах
    fun getLocalDateTime(timestamp: Long): LocalDateTime {
      return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
    }
  }
}

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
  val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
  // Only take non-null values as null will be produced when the
  // list is mid-scroll as no index will be completely visible.
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

fun YearMonth.displayText(short: Boolean = false): String {
  return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
  val style = if (short) TextStyle.SHORT else TextStyle.FULL
  return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
  return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
    if (uppercase) value.uppercase(Locale.ENGLISH) else value
  }
}
