package com.lilist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lilist.data.Challenge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(
    challenges: List<Challenge>,
    onAddChallenge: (Challenge) -> Unit,
    onUpdateChallenge: (Challenge) -> Unit,
    onDeleteChallenge: (Challenge) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newStart by remember { mutableStateOf("") }
    var newTarget by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Añadir nuevo reto", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Nombre del reto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = newStart,
            onValueChange = { newStart = it },
            label = { Text("Valor inicial") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = newTarget,
            onValueChange = { newTarget = it },
            label = { Text("Objetivo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                val startVal = newStart.toFloatOrNull()
                val targetVal = newTarget.toFloatOrNull()
                if (newName.isNotBlank() && startVal != null && targetVal != null) {
                    onAddChallenge(
                        Challenge(
                            name = newName.trim(),
                            description = newDescription.trim(),
                            startValue = startVal,
                            targetValue = targetVal,
                            progress = startVal
                        )
                    )
                    newName = ""
                    newDescription = ""
                    newStart = ""
                    newTarget = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir reto")
        }
        Spacer(Modifier.height(16.dp))
        Text("Retos existentes", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(challenges.size) { index ->
                val challenge = challenges[index]
                ChallengeItem(
                    challenge = challenge,
                    onUpdateChallenge = onUpdateChallenge,
                    onDeleteChallenge = onDeleteChallenge
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ChallengeItem(
    challenge: Challenge,
    onUpdateChallenge: (Challenge) -> Unit,
    onDeleteChallenge: (Challenge) -> Unit
) {
    var progressText by remember { mutableStateOf(challenge.progress.toString()) }
    val denominator = challenge.targetValue - challenge.startValue
    val fraction = if (denominator != 0f)
        ((challenge.progress - challenge.startValue) / denominator).coerceIn(0f, 1f)
    else 0f

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(challenge.name, style = MaterialTheme.typography.titleSmall)
            Text(challenge.description, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = fraction,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(2.dp))
            Text("Progreso: ${challenge.progress} / ${challenge.targetValue}")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = progressText,
                    onValueChange = { progressText = it },
                    label = { Text("Actualizar avance") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    val value = progressText.toFloatOrNull()
                    if (value != null) {
                        val newProgress = value.coerceIn(challenge.startValue, challenge.targetValue)
                        onUpdateChallenge(challenge.copy(progress = newProgress))
                        progressText = newProgress.toString()
                    }
                }) {
                    Text("Actualizar")
                }
                IconButton(onClick = { onDeleteChallenge(challenge) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar reto")
                }
            }
        }
    }
}
