package com.example.todo.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository
import com.example.todo.domain.useCases.AddToDoItemUseCase
import com.example.todo.utils.Utils
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
      name.isNotBlank() //&& Utils.convertTimestampToDateTime(dateStart.trim()) < Utils.convertTimestampToDateTime(dateFinish.trim())
    }
  }

  suspend fun saveToDoItem() {
    if (validateInput()) {
      addToDoItemUseCase.addToDoItem(itemUiState.itemDetails.toToDoItem())
    }
//
//
//    val dateStartString = itemUiState.itemDetails.dateStart
//    val dateFinishString = itemUiState.itemDetails.dateFinish
//
//    if (dateStartString.isNotBlank()) {
//      try {
//        val dateStart = LocalDate.parse(itemUiState.itemDetails.dateStart, DateTimeFormatter.ISO_LOCAL_DATE)
//        val localDateTime = LocalDateTime.of(dateStart, time)
//        val timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
//        addToDoItemUseCase.addToDoItem(itemUiState.itemDetails.toToDoItem().copy(dateStart = timestamp))
//      } catch (e: DateTimeParseException) {
//        Log.e("Debug", "Error parsing date: $dateStartString", e)
//      }
//    } else {
//    // Обработка случая, когда строка даты пуста
//    Log.e("Debug", "Date string is blank or empty")
//  }
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

fun ToDoItemDetails.toToDoItem(): ToDoItem {
  if (dateStart.isBlank() || dateFinish.isBlank()) {
    // Обработка случая, когда строки дат пусты
    return ToDoItem(id = id, name = name, description = description, dateStart = 0L, dateFinish = 0L)
  }
  return ToDoItem(
    id = id,
    name = name,
    description = description,
    dateStart = LocalDateTime.parse(dateStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .toInstant(ZoneOffset.UTC).toEpochMilli(),
    dateFinish = LocalDateTime.parse(dateFinish, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .toInstant(ZoneOffset.UTC).toEpochMilli()
  )
}
fun ToDoItem.toToDoItemDetails(): ToDoItemDetails = ToDoItemDetails(
  id = id,
  name = name,
  description = description,
  dateStart = dateStart.toString(),
  dateFinish = dateFinish.toString()

)

