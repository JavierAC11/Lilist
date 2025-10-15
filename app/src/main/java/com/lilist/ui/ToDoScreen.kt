package com.lilist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.lilist.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(
    tasks: List<Task>,
    onAddTask: (Task) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    var newTaskText by remember { mutableStateOf("") }

    val tasksSinFecha = remember(tasks) { tasks.filter { it.date == null } }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = newTaskText,
            onValueChange = { newTaskText = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (newTaskText.isNotBlank()) {
                    onAddTask(Task(text = newTaskText.trim()))
                    newTaskText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(tasksSinFecha.size) { index ->
                val task = tasksSinFecha[index]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { checked ->
                            onUpdateTask(task.copy(isDone = checked))
                        }
                    )
                    Text(
                        text = task.text,
                        style = if (task.isDone) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                        else MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onDeleteTask(task) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
