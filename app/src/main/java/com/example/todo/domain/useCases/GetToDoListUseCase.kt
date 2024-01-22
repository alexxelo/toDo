package com.example.todo.domain.useCases

import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository
import kotlinx.coroutines.flow.Flow

class GetToDoListUseCase(private val toDoListRepository: ToDoListRepository) {
  fun getToDoList(): Flow<List<ToDoItem>> {
    return toDoListRepository.getToDoList()
  }
}