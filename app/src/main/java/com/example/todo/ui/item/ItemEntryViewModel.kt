package com.example.todo.ui.item

import android.util.Log
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
    Log.d("Debug", " updateUiState dateStart = ${itemUiState.itemDetails.dateStart}")
    Log.d("Debug", " updateUiState name = ${itemUiState.itemDetails.name}")
  }

  private fun validateInput(uiState: ToDoItemDetails = itemUiState.itemDetails): Boolean {
    return with(uiState) {
      name.isNotBlank()
    }
  }

  suspend fun saveToDoItem() {
    if (validateInput()) {
      addToDoItemUseCase.addToDoItem(itemUiState.itemDetails.toToDoItem())
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

fun ToDoItemDetails.toToDoItem(): ToDoItem {

  return ToDoItem(
    id = id,
    name = name,
    description = description,
    dateStart = LocalDateTime.parse(dateStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .toInstant(ZoneOffset.UTC).toEpochMilli().toString().toLongOrNull() ?: 0L,
    dateFinish = LocalDateTime.parse(dateFinish, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .toInstant(ZoneOffset.UTC).toEpochMilli().toString().toLongOrNull() ?: 0L,
  )
}
fun ToDoItem.toToDoItemDetails(): ToDoItemDetails = ToDoItemDetails(
  id = id,
  name = name,
  description = description,
  dateStart = dateStart.toString(),
  dateFinish = dateFinish.toString()

)

