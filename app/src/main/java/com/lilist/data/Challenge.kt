package com.lilist.data

import java.util.UUID

data class Challenge(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val startValue: Float,
    val targetValue: Float,
    val progress: Float = 0f   // Valor actual del avance
)
