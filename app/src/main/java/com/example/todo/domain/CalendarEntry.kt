package com.example.todo.domain

import com.example.todo.data.ToDoItem
import com.example.todo.utils.Utils


//data class CalendarEntry(
//  val day: Int,
//  val toDos: List<ToDoItem> = emptyList()
//) {
//
//  companion object {
//
//    fun createCalendarList(toDoList: List<ToDoItem>): List<CalendarEntry> {
//      val calendarEntry = mutableListOf<CalendarEntry>()
//      for (i in 0 until  Utils.getDaysInMonth()) {
//        val toDosForDay: List<ToDoItem> = toDoList.filter { it.day == i }
//        calendarEntry.add(CalendarEntry(day = i, toDos = toDosForDay))
//      }
//      return calendarEntry
//    }
//  }
//}
//
