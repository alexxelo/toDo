package com.example.todo.domain.useCases

import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository

class DeleteToDoItemUseCase(private val repository: ToDoListRepository) {
  suspend fun deleteToDoItem(toDoItem: ToDoItem) {
    repository.deleteToDoItem(toDoItem)
  }
}