package com.example.todo.data

import com.example.todo.domain.ToDoListRepository
import kotlinx.coroutines.flow.Flow

class ToDoListRepositoryImpl(private val toDoDao: ToDoDao) : ToDoListRepository {
  override suspend fun addToDoItem(toDoItem: ToDoItem) {
    toDoDao.insertToDo(toDoItem)
  }

  override suspend fun deleteToDoItem(toDoItem: ToDoItem) {
    toDoDao.deleteToDo(toDoItem)
  }

  override suspend fun editToDoItem(toDoItem: ToDoItem) {
    toDoDao.insertToDo(toDoItem)
  }

  override fun getToDoItem(itemId: Int): Flow<ToDoItem> {
    return toDoDao.getToDoItem(itemId)
  }

  override fun getToDoList(): Flow<List<ToDoItem>> {
    return toDoDao.getToDoList()
  }
}