package com.example.todo.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todo.ui.home.HomeScreen
import com.example.todo.ui.item.ItemDetailsScreen
import com.example.todo.ui.item.ItemEntryScreen
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object Destinations {
  const val HOME = "home"
  const val ENTRY = "entry"
  const val DETAIL = "detail/{itemId}"
}

@Composable
fun AppNavGraph(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  NavHost(
    navController = navController,
    startDestination = Destinations.HOME,
    modifier = modifier
  ) {

    composable(
      route = Destinations.HOME,
      arguments = listOf(
        navArgument("itemId") { type = NavType.IntType }),
    ) { backStackEntry ->
      val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
      HomeScreen(
        navigateToItemDetails = { navController.navigate("${Destinations.DETAIL}/$itemId") },
        navigateBack = { navController.popBackStack() },
        navigateToEntry = { day ->
          navController.navigate("${Destinations.ENTRY}/${day.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()}")
        }
      )
    }
    composable(
      route = "${Destinations.DETAIL}/{itemId}",
      arguments = listOf(navArgument("itemId") { type = NavType.IntType })
    ) { backStackEntry ->
      val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0

      ItemDetailsScreen(
        itemId = itemId,
        navigateBack = { navController.popBackStack() })
    }
    composable(
      route = "${Destinations.ENTRY}/{date}",
      arguments = listOf(navArgument("date") { type = NavType.LongType })
    ) { backStackEntry ->
      val date = backStackEntry.arguments?.getLong("date") ?: 0L
      val localDateTime = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDateTime()
      ItemEntryScreen(
        date = localDateTime,
        navigateBack = { navController.popBackStack() },
      )
    }

  }
}