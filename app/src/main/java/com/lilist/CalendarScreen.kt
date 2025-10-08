package com.lilist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.core.CalendarDay
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    events: Map<LocalDate, List<String>> // Tus tareas/eventos por día
) {
    val currentMonth = YearMonth.now()

    HorizontalCalendar(
        /*currentMonth = currentMonth,
        yearMonth = currentMonth,
        onDayClick = { date -> onDateSelected(date) },*/
        dayContent = { day: CalendarDay ->
            val isSelected = day.date == selectedDate
            val hasTasks = events.containsKey(day.date)
            val backgroundColor = when {
                isSelected -> MaterialTheme.colorScheme.primary
                hasTasks -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.background
            }
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .background(backgroundColor, MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    if (hasTasks) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "\u2022", // Punto para indicar evento/tarea
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}
