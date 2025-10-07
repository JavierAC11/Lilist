package com.lilist

data class Task(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isDone: Boolean = false
)
