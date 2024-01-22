package com.example.todo.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.AppViewModelProvider
import com.example.todo.data.ToDoItem
import com.example.todo.utils.Utils
import com.example.todo.utils.displayText
import com.example.todo.utils.rememberFirstCompletelyVisibleMonth
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  // int for item id
  navigateToItemDetails: (Int) -> Unit,
  navigateBack: () -> Unit,
  navigateToEntry: (date: LocalDate) -> Unit,
  viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),

  ) {
  // :State<HomeUiState>
  // дела которые добавлены в базу
  val homeUi = viewModel.homeUiState.collectAsState()
//
//  val toDoItemList by remember {
//    mutableStateOf(createCalendarList(homeUi.value.toDoList))
//  }

  var outerSelection by remember { mutableStateOf<CalendarDay?>(null) }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(onClick = {
        if (outerSelection != null) {
          navigateToEntry(outerSelection!!.date)
        }
      }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize(),
    ) {
      Calendar() { clickedDay ->
        outerSelection = clickedDay
      }
      Spacer(modifier = Modifier.height(20.dp))

//      Column(
//        modifier = Modifier
//          .fillMaWidth()
//      ) {
//        clickedCalendarElem?.let { toDoItem ->
//
//          val findDayOfWeek = clickedCalendarElem?.day?.let { Utils.getCurrentDate().withDayOfMonth(it).dayOfWeek.name }
//          val findDayOfWeekCount = clickedCalendarElem?.day?.let { Utils.getCurrentDate().withDayOfMonth(it).dayOfMonth }
//
//          dayOfWeek = Utils.capitalizeFirstLetter(findDayOfWeek)
//          dayOfWeekCount = findDayOfWeekCount.toString()
//
//          //TodoTable(dayOfWeekCount, dayOfWeek,)
//        }
//      }
    }
  }
}


@Composable
fun Calendar(

  onClick: (CalendarDay) -> Unit
) {

  val currentMonth = remember { YearMonth.now() }
  val startMonth = remember { currentMonth.minusMonths(100) }
  val endMonth = remember { currentMonth.plusMonths(100) }
  val daysOfWeek = remember { daysOfWeek() }

  var selection: CalendarDay? by remember { mutableStateOf<CalendarDay?>(null) }

  val state = rememberCalendarState(
    startMonth = startMonth,
    endMonth = endMonth,
    firstVisibleMonth = currentMonth,
    firstDayOfWeek = daysOfWeek.first(),
  )

  val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(visibleMonth) {
    selection = null
  }

  SimpleCalendarTitle(
    modifier = Modifier
      .padding(horizontal = 8.dp, vertical = 12.dp),
    currentMonth = visibleMonth.yearMonth,
    goToPrevious = {
      coroutineScope.launch {
        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
      }
    },
    goToNext = {
      coroutineScope.launch {
        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
      }
    },
  )
  HorizontalCalendar(
    state = state,
    dayContent = { day ->
      Day(
        day = day,
        isSelected = selection == day,
      ) {
        onClick(it)
        selection = it
      }
    },
    monthHeader = {
      MonthHeader(
        modifier = Modifier.padding(vertical = 8.dp),
        daysOfWeek = daysOfWeek
      )
    },
  )
  LazyColumn(modifier = Modifier.fillMaxWidth()) {

  }
}

@Composable
private fun MonthHeader(
  modifier: Modifier = Modifier,
  daysOfWeek: List<DayOfWeek> = emptyList(),
) {
  Row(modifier.fillMaxWidth()) {
    for (dayOfWeek in daysOfWeek) {
      Text(
        modifier = Modifier.weight(1f),
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        color = Color.Black,
        text = dayOfWeek.displayText(uppercase = true),
        fontWeight = FontWeight.Light,
      )
    }
  }
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean = false, onClick: (CalendarDay) -> Unit) {
  Box(
    modifier = Modifier
      .aspectRatio(1f)
      .border(
        width = if (isSelected) 1.dp else 0.dp,
        color = if (isSelected) Color.Red else Color.Transparent
      )
      .padding(1.dp)
      .background(color = Color.LightGray)
      .clickable(
        enabled = day.position == DayPosition.MonthDate,
        onClick = { onClick(day) }),
  ) {
    val textColor = when (day.position) {
      DayPosition.MonthDate -> Color.Unspecified
      DayPosition.InDate, DayPosition.OutDate -> Color.DarkGray
    }
    Text(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(top = 3.dp, end = 4.dp),
      text = day.date.dayOfMonth.toString(),
      color = textColor,//if (day.position == DayPosition.MonthDate) Color.Black else Color.Gray,
      fontSize = 12.sp,
    )
  }
}

@Composable
fun SimpleCalendarTitle(
  modifier: Modifier,
  currentMonth: YearMonth,
  goToPrevious: () -> Unit,
  goToNext: () -> Unit,
) {
  Row(
    modifier = modifier.height(40.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CalendarNavigationIcon(
      icon = Icons.AutoMirrored.Filled.ArrowBack,
      contentDescription = "Previous",
      onClick = goToPrevious,
    )
    Text(
      modifier = Modifier
        .weight(1f)
        .testTag("MonthTitle"),
      text = "${currentMonth.month} ${currentMonth.year}",
      fontSize = 22.sp,
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Medium,
    )
    CalendarNavigationIcon(
      icon = Icons.AutoMirrored.Filled.ArrowForward,
      contentDescription = "Next",
      onClick = goToNext,
    )
  }
}

@Composable
private fun CalendarNavigationIcon(
  icon: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
) = Box(
  modifier = Modifier
    .fillMaxHeight()
    .aspectRatio(1f)
    .clip(shape = CircleShape)
    .clickable(role = Role.Button, onClick = onClick),
) {
  Icon(
    modifier = Modifier
      .fillMaxSize()
      .padding(4.dp)
      .align(Alignment.Center), imageVector = icon, contentDescription = contentDescription
  )
}


@Composable
fun TodoTable(
  day: String, dayOfWeek: String, hoursInDay: Int = 24, onItemClick: (ToDoItem) -> Unit
) {
  //add day of week
  val listState = rememberLazyListState()

  Row {
    Text(
      modifier = Modifier.padding(horizontal = 8.dp),
      text = "$dayOfWeek $day ",
      color = Color.Blue,
      fontWeight = FontWeight.SemiBold,
      fontSize = 25.sp
    )
  }
  LazyColumn(
    state = listState,
    modifier = Modifier,
    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
  ) {
    items(hoursInDay) { hour ->
      TimeLine(hour, onItemClick)
    }

  }

  LaunchedEffect(true) {
    listState.scrollToItem(Utils.getCurrentTime().hour)
  }

}

@Composable
fun TimelineNode(
  contentStartOffset: Dp = 16.dp,
  spacerBetweenNodes: Dp = 32.dp,
  content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
  Box(
    modifier = Modifier
      .wrapContentSize()
      .drawBehind {
        // 2. draw a circle here ->
        val circleRadiusInPx = 8.sp
        drawCircle(
          color = Color.Magenta,
          radius = circleRadiusInPx.toPx(),
          center = Offset(circleRadiusInPx.toPx(), circleRadiusInPx.toPx())
        )
      }
  ) {
    content(
      Modifier
        .padding(
          // 2. we apply our paddings
          start = contentStartOffset,
          bottom = spacerBetweenNodes
        )
    )
  }
}

@Composable
private fun MessageBubble(modifier: Modifier, containerColor: Color) {
  Card(
    modifier = modifier
      .width(200.dp)
      .height(100.dp),
    colors = CardDefaults.cardColors(containerColor = containerColor)
  ) {}
}

@Composable
fun ToDoHourItem(
  hour: Int,
  homeUiState: HomeUiState,
  onItemClick: (ToDoItem) -> Unit
) {
  val matchingData = homeUiState.toDoList.find { Utils.timestampToZonedDateTime(it.dateStart).hour == hour }

  if (matchingData != null) {
    Box(modifier = Modifier.clickable { onItemClick(matchingData) }) {
      Text(
        text = " A name \n dateStart - dateEnd", //"${matchingData.name}\n\n${matchingData.dateStart} - ${matchingData.dateFinish}",
        modifier = Modifier
          .drawBehind {
            drawRoundRect(
              Color(0xFFBBAAEE),
              cornerRadius = CornerRadius(10.dp.toPx())
            )
          }
          .padding(4.dp)
      )
    }
  }

//  if (matchingData != null) {
//    Box(
//      modifier = Modifier
//        .fillMaxWidth()
//        .padding(start = 52.dp)
//        .height(50.dp)
//        .background(Color.Red, shape = RectangleShape)
//        .border(border = BorderStroke(2.dp, Color.Blue))
//    ) {
//      Text(
//        text = homeUiState.toDoList[matchingData.id].name,
//        modifier = Modifier
//          .align(Alignment.Center)
//          .padding(16.dp),
//        color = Color.Black,
//        fontWeight = FontWeight.Bold,
//        fontSize = 20.sp,
//      )
//    }
//  }
}

@Composable
fun TimeLine(hour: Int, onItemClick: (ToDoItem) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(48.dp)
  ) {
    Text(
      text = "${hour} : 00",
      modifier = Modifier
        .padding(horizontal = 8.dp)
    )

    Spacer(
      modifier = Modifier
        .weight(3f)
        .padding(vertical = 10.dp)
        .height(1.dp)
        .background(Color.Gray)
        .width(1.dp)
    )
    //ToDoHourItem("123", onItemClick)
  }
}


@Preview(showSystemUi = true, showBackground = true, device = Devices.NEXUS_5)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
  //ToDoHourItem(2,)
  //Calendar()
}

@Preview(showBackground = true)
@Composable
private fun TimelinePreview() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    TimelineNode() { modifier -> MessageBubble(modifier, containerColor = Color.Blue) }
    TimelineNode() { modifier -> MessageBubble(modifier, containerColor = Color.Red) }
    TimelineNode() { modifier -> MessageBubble(modifier, containerColor = Color.Green) }
  }

}