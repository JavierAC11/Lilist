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
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    events: Map<LocalDate, List<String>>
) {
    val currentMonth = YearMonth.now()
    val today = LocalDate.now()

    HorizontalCalendar(
        modifier = Modifier.fillMaxWidth(),
        state = rememberCalendarState(currentMonth),
        calendarScrollPaged = true,
        userScrollEnabled = true,
        dayContent = { day ->
            val isSelected = day.date == selectedDate
            val isToday = day.date == today
            val hasTasks = events.containsKey(day.date)
            val backgroundColor = when {
                isSelected -> MaterialTheme.colorScheme.primary
                isToday -> MaterialTheme.colorScheme.tertiaryContainer // Color distinto para hoy
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
                        color = if (isSelected || isToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                    )
                    if (hasTasks) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "\u2022",
                            color = if (isSelected || isToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        monthHeader = { month ->
            Column {
                Text(
                    text = "${month.yearMonth.month.name} ${month.yearMonth.year}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
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
            }
        }
    )
}
