package com.example.todo.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository
import com.example.todo.domain.useCases.AddToDoItemUseCase
import java.time.LocalDateTime
import java.time.ZoneId
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

fun ToDoItemDetails.toToDoItem(): ToDoItem = ToDoItem(
  id = id,
  name = name,
  description = description,
  dateStart = dateStart.toLongOrNull() ?: LocalDateTime.parse(dateStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli(),

  dateFinish = dateFinish.toLongOrNull() ?: LocalDateTime.parse(dateFinish, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()
)


fun ToDoItem.toToDoItemDetails(): ToDoItemDetails = ToDoItemDetails(
  id = id,
  name = name,
  description = description,
  dateStart = dateStart.toString(),
  dateFinish = dateFinish.toString()

)
fun ToDoItem.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
  itemDetails = this.toToDoItemDetails(),
  isEntryValid = isEntryValid
)
