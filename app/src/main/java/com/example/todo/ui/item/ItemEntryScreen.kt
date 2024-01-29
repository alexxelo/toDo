package com.example.todo.ui.item

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.AppViewModelProvider
import com.example.todo.R
import com.example.todo.utils.Utils
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
  date: LocalDateTime,
  modifier: Modifier = Modifier,
  viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
  navigateBack: () -> Unit,
) {
  val scope = rememberCoroutineScope()

  Scaffold(topBar = {
    CenterAlignedTopAppBar(
      title = {
        Text(text = stringResource(R.string.new_event))
      },
      navigationIcon = {
        TextButton(onClick = {
          navigateBack()
        }) {
          Text(text = stringResource(R.string.cancel), fontSize = 20.sp)
        }
      },
    )
  }) { innerPadding ->
    ToDoEntryBody(
      itemUiState = viewModel.itemUiState, date = date.toLocalDate(), onSaveClick = {
        scope.launch {
          viewModel.saveToDoItem()
          navigateBack()
        }
      }, onValueChange = viewModel::updateUiState,
      modifier = Modifier
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .fillMaxWidth()
    )
  }

}

@Composable
fun ToDoEntryBody(
  itemUiState: ItemUiState, date: LocalDate, onSaveClick: () -> Unit, onValueChange: (ToDoItemDetails) -> Unit = {}, modifier: Modifier = Modifier
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
    modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
  ) {
    ToDoItemEntryForm(
      itemDetails = itemUiState.itemDetails, date = date, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth()
    )
    Button(
      onClick = { onSaveClick() }, enabled = itemUiState.isEntryValid, modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = stringResource(R.string.save))
    }
  }

}

@Composable
fun ToDoItemEntryForm(
  itemDetails: ToDoItemDetails, date: LocalDate, modifier: Modifier = Modifier, onValueChange: (ToDoItemDetails) -> Unit = {}, enabled: Boolean = true
) {
  Column(modifier = modifier) {
    OutlinedTextField(
      value = itemDetails.name,
      onValueChange = {
        onValueChange(itemDetails.copy(name = it))
      }, colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
      ), label = {
        Text(text = stringResource(R.string.title))
      }, enabled = enabled, modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
    )

    TimePicker(text = stringResource(R.string.starts), date,
      itemDetails = itemDetails.copy(dateStart = itemDetails.dateStart, dateFinish = itemDetails.dateFinish),
      onValueChange = {updatedItemDetails ->
        onValueChange(updatedItemDetails)})
    TimePicker(
      text = stringResource(R.string.ends), date = date, itemDetails, isStartTime = false,
      initialTime = Utils.getCurrentTime().plusHours(1),
      onValueChange = { updatedItemDetails ->
        onValueChange(updatedItemDetails)
      },
    )

    OutlinedTextField(
      value = itemDetails.description,
      onValueChange = {
        onValueChange(itemDetails.copy(description = it))
      }, colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
      ), label = {
        Text(text = stringResource(R.string.description))
      }, enabled = enabled, modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .height(150.dp)
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
  text: String,
  date: LocalDate,
  itemDetails: ToDoItemDetails,
  onValueChange: (ToDoItemDetails) -> Unit = {},
  isStartTime: Boolean = true,
  initialTime: LocalTime = Utils.getCurrentTime()
) {
  var pickedDate by remember {
    mutableStateOf(date)
  }
  var pickedTime by remember {
    mutableStateOf(initialTime)
  }
  var dateTime by remember {
    mutableStateOf(LocalDateTime.of(pickedDate, pickedTime))
  }

  val formattedDate by remember {
    derivedStateOf {
      DateTimeFormatter.ofPattern("dd MMM y").format(pickedDate)
    }
  }
  val formattedTime by remember {
    derivedStateOf {
      DateTimeFormatter.ofPattern("HH:mm").format(pickedTime)
    }
  }
  val dateState = rememberUseCaseState()
  val clockState = rememberUseCaseState()
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  fun updateTime() {
    dateTime = LocalDateTime.of(pickedDate, pickedTime)
    val updatedItemDetails = if (isStartTime) {
      itemDetails.copy(dateStart = dateTime.format(formatter))
    } else {
      itemDetails.copy(dateFinish = dateTime.format(formatter))
    }
    onValueChange(updatedItemDetails)
  }

  if (itemDetails.dateStart.isBlank() && isStartTime) {
    onValueChange(itemDetails.copy(dateStart = dateTime.format(formatter)))

  } else if (itemDetails.dateFinish.isBlank() && !isStartTime) {
    onValueChange(itemDetails.copy(dateFinish = dateTime.format(formatter)))

  }


  CalendarDialog(
    state = dateState,
    config = CalendarConfig(monthSelection = true, yearSelection = true),
    selection = CalendarSelection.Date { day ->
      pickedDate = day
      updateTime()
    },
  )


  ClockDialog(
    state = clockState,
    config = ClockConfig(),
    selection = ClockSelection.HoursMinutes { hours, minutes ->
      pickedTime = LocalTime.of(hours, minutes)
      updateTime()
    })


  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp, horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,

    ) {
    Text(
      text = text, fontSize = 22.sp, textAlign = TextAlign.Left, modifier = Modifier
    )
    Row(
      horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
    ) {
      Button(onClick = {
        dateState.show()
      }, modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(text = formattedDate)
      }
      Button(onClick = {
        clockState.show()
      }) {
        Text(text = formattedTime)
      }
    }
  }
}

@Preview(showSystemUi = true)
@Composable
fun ItemEntryScreenPreview(modifier: Modifier = Modifier) {

}