package com.lilist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf("todo") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "ToDo") },
                    label = { Text("ToDo") },
                    selected = selectedScreen == "todo",
                    onClick = { selectedScreen = "todo" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendario") },
                    label = { Text("Calendario") },
                    selected = selectedScreen == "calendar",
                    onClick = { selectedScreen = "calendar" }
                )
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                "todo" -> ToDoScreen() // Tu pantalla de tareas
                "calendar" -> CalendarScreenWrapper() // Adaptar calendar screen para usar sin parámetros
            }
        }
    }
}

@Composable
fun CalendarScreenWrapper() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val events = remember { mapOf<LocalDate, List<String>>() } // Carga o pasa tus eventos aquí

    CalendarScreen(
        selectedDate = selectedDate,
        onDateSelected = { selectedDate = it },
        events = events
    )
}
