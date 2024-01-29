package com.example.todo.ui.navigation

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
import java.time.ZoneId

object Destinations {
  const val HOME = "home"
  const val ENTRY = "entry"
  const val DETAIL = "detail"
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
    ) {
      HomeScreen(
        navigateToItemDetails = { itemId -> navController.navigate("${Destinations.DETAIL}/$itemId") },
        navigateToEntry = { day ->
          navController.navigate("${Destinations.ENTRY}/${day.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()}")
        }
      )
    }
    composable(
      route = "${Destinations.DETAIL}/{itemId}",
      arguments = listOf(navArgument("itemId") { type = NavType.IntType })
    ) {
      ItemDetailsScreen(
        navigateBack = { navController.popBackStack() },
        navigateToEdit = {}
      )
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