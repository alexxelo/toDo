package com.example.todo.domain.useCases

import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository

class EditToDoItemUseCase(private val repository: ToDoListRepository) {
  suspend fun editToDoItem(toDoItem: ToDoItem){
    repository.editToDoItem(toDoItem)
  }
}