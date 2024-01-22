package com.example.todo.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.domain.ToDoListRepository
import com.example.todo.domain.useCases.DeleteToDoItemUseCase
import com.example.todo.domain.useCases.GetToDoItemUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ItemDetailsViewModel(
  savedStateHandle: SavedStateHandle,
  toDoListRepository: ToDoListRepository
) : ViewModel() {
  private val getToDoItemUseCase = GetToDoItemUseCase(toDoListRepository)
  private val deleteToDoItemUseCase = DeleteToDoItemUseCase(toDoListRepository)

  private val itemId: Int = checkNotNull(savedStateHandle["itemId"] ?: -1)

  val uiState = getToDoItemUseCase.getToDoItem(itemId)
    .filterNotNull()
    .map { ToDoItemDetailsUiState(it.toToDoItemDetails()) }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
      initialValue = ToDoItemDetailsUiState()
    )
  suspend fun delete(){
    deleteToDoItemUseCase.deleteToDoItem(uiState.value.itemDetails.toToDoItem())
  }

  companion object {
    private const val TIMEOUT_MILLIS = 5_000L
  }

}
data class ToDoItemDetailsUiState(
  //val outOfStock: Boolean = true,
  val itemDetails: ToDoItemDetails = ToDoItemDetails()
)