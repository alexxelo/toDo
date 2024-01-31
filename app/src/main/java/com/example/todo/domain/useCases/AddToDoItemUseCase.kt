package com.example.todo.domain.useCases

import com.example.todo.data.ToDoItem
import com.example.todo.domain.ToDoListRepository

class AddToDoItemUseCase(private val repository: ToDoListRepository) {
  suspend fun addToDoItem(toDoItem: ToDoItem){
    repository.addToDoItem(toDoItem)
  }
}