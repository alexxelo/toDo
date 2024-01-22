package com.example.todo.data

import android.content.Context
import com.example.todo.domain.ToDoListRepository

interface AppContainer {
  val toDoListRepository: ToDoListRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

  override val toDoListRepository: ToDoListRepository by lazy {
    ToDoListRepositoryImpl(ToDoDatabase.getDatabase(context).dao())
  }
}

