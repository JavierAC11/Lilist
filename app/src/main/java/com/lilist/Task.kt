package com.lilist

import java.time.LocalDate

data class Task(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isDone: Boolean = false,
    val date: LocalDate? = null // puede ser null si no tiene fecha asignada
)
