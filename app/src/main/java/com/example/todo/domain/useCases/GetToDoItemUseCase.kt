package com.example.todo.domain.useCases

import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository
import kotlinx.coroutines.flow.Flow

class GetToDoItemUseCase(private val repository: ToDoListRepository) {
  fun getToDoItem(itemId: Int): Flow<ToDoItem?> {
    return repository.getToDoItem(itemId)
  }
}