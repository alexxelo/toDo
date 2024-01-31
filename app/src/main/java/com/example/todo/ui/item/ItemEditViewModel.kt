package com.example.todo.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.domain.ToDoListRepository
import com.example.todo.domain.useCases.EditToDoItemUseCase
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ItemEditViewModel(
  savedStateHandle: SavedStateHandle,
  repository: ToDoListRepository
) : ViewModel() {

  private val editToDoItemUseCase = EditToDoItemUseCase(repository)

  private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
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

  suspend fun updateItem() {
    if (validateInput(itemUiState.itemDetails)) {
      editToDoItemUseCase.editToDoItem(itemUiState.itemDetails.toToDoItem())
    }
  }

  init {
    viewModelScope.launch {
      itemUiState = repository.getToDoItem(itemId)
        .filterNotNull()
        .first()
        .toItemUiState(true)
    }
  }

}