package com.lilist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    events: Map<LocalDate, List<String>>
) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val calendarState = rememberCalendarState(startMonth = currentMonth)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔹 Encabezado con nombre del mes actual
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // 🔹 Encabezado de días (Lu, Ma, Mi, etc.)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do").forEach { dayName ->
                Text(
                    text = dayName,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // 🔹 Calendario principal
        HorizontalCalendar(
            modifier = Modifier.fillMaxWidth(),
            state = calendarState,
            calendarScrollPaged = true,
            userScrollEnabled = true,
            dayContent = { day ->
                val isSelected = day.date == selectedDate
                val isToday = day.date == today
                val hasTasks = events.containsKey(day.date)

                val backgroundColor = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.tertiaryContainer
                    hasTasks -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.background
                }

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .background(backgroundColor, MaterialTheme.shapes.small)
                        .clickable { onDateSelected(day.date) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = day.date.dayOfMonth.toString(),
                            color = if (isSelected || isToday)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                        if (hasTasks) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "\u2022",
                                color = if (isSelected || isToday)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            },
            monthHeader = {}
        )

        // 🔹 Controles de navegación del mes (AHORA ABAJO)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                val previousMonth = currentMonth.minusMonths(1)
                currentMonth = previousMonth
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(previousMonth)
                }
            }) {
                Text("◀ Anterior")
            }

            Button(onClick = {
                val nextMonth = currentMonth.plusMonths(1)
                currentMonth = nextMonth
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(nextMonth)
                }
            }) {
                Text("Siguiente ▶")
            }
        }
    }
}
