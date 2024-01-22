package com.example.todo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

  @Upsert
  suspend fun insertToDo(toDoItem: ToDoItem)

  @Delete
  suspend fun deleteToDo(toDoItem: ToDoItem)

  @Query("SELECT * FROM todo")
  fun getToDoList(): Flow<List<ToDoItem>>

  @Query("SELECT * FROM todo WHERE id = :id")
  fun getToDoItem(id: Int): Flow<ToDoItem>


}