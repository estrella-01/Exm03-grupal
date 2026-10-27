package org.donnico.projectv1.e1_firestore.model

import kotlinx.serialization.Serializable

// Modelo de datos para cada tarea en Firestore con valores por defecto
@Serializable
data class Tarea(
    val id: String = "",
    val titulo: String = "",
    val completada: Boolean = false,
    val timestamp: Long = 0L
)
