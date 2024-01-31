package com.example.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository
import com.example.todo.domain.useCases.GetToDoListUseCase
import com.example.todo.utils.timestampToLocalDate
import com.example.todo.utils.timestampToZonedDateTime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class HomeViewModel(repository: ToDoListRepository) : ViewModel() {
  fun getToDoItemForDay(date: LocalDate, toDoList: List<ToDoItem>, hour: Int): List<ToDoItem>  {
    return toDoList.filter {
      timestampToLocalDate(it.dateStart) == date &&
      timestampToZonedDateTime(it.dateStart).hour == hour
    }
  }

  private val getToDoListUseCase = GetToDoListUseCase(repository)

  val homeUiState: StateFlow<HomeUiState> = getToDoListUseCase.getToDoList().map { HomeUiState(it) }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
      initialValue = HomeUiState()
    )

  companion object {
    private const val TIMEOUT_MILLIS = 5_000L

  }
}

data class HomeUiState(val toDoList: List<ToDoItem> = listOf())
