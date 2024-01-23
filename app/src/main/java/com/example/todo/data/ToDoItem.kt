package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.ZoneId

@Entity(tableName = "todo")
data class ToDoItem(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val name: String,
  val description: String,
  val dateStart: Long,
  val dateFinish: Long
)