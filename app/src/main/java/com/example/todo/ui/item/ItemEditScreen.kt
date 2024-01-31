package com.example.todo.ui.item


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.AppViewModelProvider
import com.example.todo.R
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    date: LocalDateTime,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: ItemEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.edit_item))
                },
                navigationIcon = {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }

            )

        }
    ) { innerPadding ->
        ToDoEntryBody(
            itemUiState = viewModel.itemUiState,
            date = date.toLocalDate(),
            onValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Preview
@Composable
fun ItemEditScreenPreview(modifier: Modifier = Modifier) {
   // ItemEditScreen(modifier = modifier)
}