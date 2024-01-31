package com.example.todo.domain

import com.example.todo.data.ToDoItem
import kotlinx.coroutines.flow.Flow

interface ToDoListRepository {
  suspend fun addToDoItem(toDoItem: ToDoItem)
  suspend fun deleteToDoItem(toDoItem: ToDoItem)
  suspend fun editToDoItem(toDoItem: ToDoItem)
  fun getToDoItem(itemId: Int): Flow<ToDoItem?>
  fun getToDoList(): Flow<List<ToDoItem>>

}