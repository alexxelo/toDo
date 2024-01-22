package com.example.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [ToDoItem::class],
  version = 1,
  exportSchema = false
)
abstract class ToDoDatabase : RoomDatabase() {

  abstract fun dao(): ToDoDao

  companion object {

    @Volatile
    var Instance: ToDoDatabase? = null

    fun getDatabase(context: Context): ToDoDatabase {
      return Instance ?: synchronized(this) {
        Room.databaseBuilder(context, ToDoDatabase::class.java, "todos_database")
          .fallbackToDestructiveMigration()
          .build()
          .also { Instance = it }
      }

    }
  }

}