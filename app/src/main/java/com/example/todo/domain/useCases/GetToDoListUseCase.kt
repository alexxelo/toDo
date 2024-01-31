package com.example.todo.domain.useCases

import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository
import kotlinx.coroutines.flow.Flow

class GetToDoListUseCase(private val repository: ToDoListRepository) {
  fun getToDoList(): Flow<List<ToDoItem>> {
    return repository.getToDoList()
  }
}