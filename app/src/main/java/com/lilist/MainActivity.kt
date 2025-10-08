package com.lilist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
            val sampleEvents = mapOf<LocalDate, List<String>>()

            ToDoScreen()

            CalendarScreen(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                events = sampleEvents
            )
        }
    }
}
