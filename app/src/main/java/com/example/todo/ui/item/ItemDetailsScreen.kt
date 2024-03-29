package com.example.todo.ui.item


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.AppViewModelProvider
import com.example.todo.R
import com.example.todo.utils.toFormattedDate
import com.example.todo.utils.toFormattedDateTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
  modifier: Modifier = Modifier,
  navigateBack: () -> Unit,
  navigateToEdit: (Int) -> Unit,
  viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),

  ) {
  val scope = rememberCoroutineScope()
  val uiState = viewModel.uiState.collectAsState()

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(text = stringResource(R.string.event_details))
        },
        navigationIcon = {
          TextButton(onClick = {
            navigateBack()
          }) {
            Text(text = stringResource(R.string.cancel), fontSize = 20.sp)
          }
        },
        actions = {
          TextButton(onClick = {
            navigateToEdit(uiState.value.itemDetails.id)
          }) {
            Text(text = stringResource(R.string.edit), fontSize = 20.sp)
          }
        }
      )
    },
    bottomBar = {
      CenterAlignedTopAppBar(title = {
        TextButton(onClick = {
          scope.launch {
            viewModel.delete()
            navigateBack()
          }
        }) {
          Text(text = stringResource(R.string.delete), fontSize = 20.sp)
        }
      })
    }

  ) { padding ->
    Box(
      modifier = modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      ItemDetails(
        itemDetailsUiState = uiState.value,
      )
    }
  }
}

@Composable
fun ItemDetails(
  itemDetailsUiState: ToDoItemDetailsUiState,
) {
  Column(
    modifier = Modifier
      .padding(8.dp)
      .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = itemDetailsUiState.itemDetails.name,
      fontSize = 26.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Start,
      modifier = Modifier.fillMaxWidth()
    )

    Text(
      text = "${itemDetailsUiState.itemDetails.dateStart.toLongOrNull()?.toFormattedDate()}, " +
          "${itemDetailsUiState.itemDetails.dateStart.toLongOrNull()?.toFormattedDateTime()} " +
          "- ${itemDetailsUiState.itemDetails.dateFinish.toLongOrNull()?.toFormattedDateTime()}",
      textAlign = TextAlign.Left,
      modifier = Modifier.fillMaxWidth()

    )

    Text(
      text = itemDetailsUiState.itemDetails.description,
      fontSize = 20.sp,
      modifier = Modifier.fillMaxWidth()

    )
  }
}

@Preview(showSystemUi = true)
@Composable
fun ItemDetailsScreenPreview(modifier: Modifier = Modifier) {
  //ItemDetails()
}