package com.example.todo

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todo.ui.home.HomeViewModel
import com.example.todo.ui.item.ItemDetailsViewModel
import com.example.todo.ui.item.ItemEditViewModel
import com.example.todo.ui.item.ItemEntryViewModel

object AppViewModelProvider {
  val Factory = viewModelFactory {

    initializer {
      ItemEntryViewModel(
        toDoApplication().container.toDoListRepository
      )
    }

    // Initializer for ItemDetailsViewModel
    initializer {
      ItemDetailsViewModel(
        this.createSavedStateHandle(),
        toDoApplication().container.toDoListRepository
      )
    }

    // Initializer for HomeViewModel
    initializer {
      HomeViewModel(
        toDoApplication().container.toDoListRepository
      )
    }
    initializer {
      ItemEditViewModel(
        this.createSavedStateHandle(),
        toDoApplication().container.toDoListRepository
      )
    }
  }
}

fun CreationExtras.toDoApplication(): ToDoApplication =
  (this[AndroidViewModelFactory.APPLICATION_KEY] as ToDoApplication)
