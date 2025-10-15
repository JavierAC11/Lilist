package com.lilist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.lilist.data.Task
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    tasks: List<Task>,
    onAddTask: (LocalDate, String) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val calendarState = rememberCalendarState(startMonth = currentMonth)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp),
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

        val eventsByDate = tasks
            .filter { it.date != null }
            .groupBy { it.date!! }
            .mapValues { it.value.map(Task::text) }

        HorizontalCalendar(
            modifier = Modifier.fillMaxWidth(),
            state = calendarState,
            calendarScrollPaged = true,
            userScrollEnabled = true,
            dayContent = { day ->
                val isSelected = day.date == selectedDate
                val isToday = day.date == today
                val hasTasks = eventsByDate.containsKey(day.date)

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

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
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

        val fecha = selectedDate ?: LocalDate.now()
        val tareasDeLaFecha = tasks.filter { it.date == fecha }

        var newTaskText by remember { mutableStateOf("") }
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(text = "Tareas para $fecha:")
            tareasDeLaFecha.forEach { tarea ->
                var isDone by remember { mutableStateOf(tarea.isDone) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isDone,
                        onCheckedChange = {
                            isDone = it
                            onUpdateTask(tarea.copy(isDone = isDone))
                        }
                    )
                    Text(
                        text = tarea.text,
                        modifier = Modifier.weight(1f),
                        style = if (isDone) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                        else MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = { onDeleteTask(tarea) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = newTaskText,
                onValueChange = { newTaskText = it },
                label = { Text("Nueva tarea para el día") }
            )
            Button(
                onClick = {
                    if (newTaskText.isNotBlank()) {
                        onAddTask(fecha, newTaskText)
                        newTaskText = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Añadir tarea")
            }
        }
    }
}
