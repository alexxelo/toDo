package com.example.todo.ui.item

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository
import com.example.todo.domain.useCases.AddToDoItemUseCase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ItemEntryViewModel(
  repository: ToDoListRepository
) : ViewModel() {

  private val addToDoItemUseCase = AddToDoItemUseCase(repository)
  var itemUiState by mutableStateOf(ItemUiState())
    private set

  fun updateUiState(itemDetails: ToDoItemDetails) {
    itemUiState = ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
  }

  private fun validateInput(uiState: ToDoItemDetails = itemUiState.itemDetails): Boolean {
    return with(uiState) {
      name.isNotBlank()
    }
  }

  suspend fun saveToDoItem(time: LocalTime) {

    val dateStartString = itemUiState.itemDetails.dateStart
    if (dateStartString.isNotBlank()) {
      try {
        val dateStart = LocalDate.parse(itemUiState.itemDetails.dateStart, DateTimeFormatter.ISO_LOCAL_DATE)
        val localDateTime = LocalDateTime.of(dateStart, time)
        val timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        addToDoItemUseCase.addToDoItem(itemUiState.itemDetails.toToDoItem().copy(dateStart = timestamp))
      } catch (e: DateTimeParseException) {
        Log.e("Debug", "Error parsing date: $dateStartString", e)
      }
    } else {
    // Обработка случая, когда строка даты пуста
    Log.e("Debug", "Date string is blank or empty")
  }
  }
}

data class ItemUiState(
  val itemDetails: ToDoItemDetails = ToDoItemDetails(),
  val isEntryValid: Boolean = false
)

data class ToDoItemDetails(
  val id: Int = 0,
  val name: String = "",
  val description: String = "",
  val dateStart: String = "",
  val dateFinish: String = "",
)

fun ToDoItemDetails.toToDoItem(): ToDoItem = ToDoItem(
  id = id,
  name = "",
  description = description ?: "",
  dateStart = dateStart.toLongOrNull() ?: 0L,
  dateFinish = dateFinish.toLongOrNull() ?: 0L
)

fun ToDoItem.toToDoItemDetails(): ToDoItemDetails = ToDoItemDetails(
  id = id,
  name = name,
  description = description,
  dateStart = dateStart.toString(),
  dateFinish = dateFinish.toString()

)

