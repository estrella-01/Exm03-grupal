package org.donnico.projectv1.e1_firestore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.ChangeType
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.donnico.projectv1.e1_firestore.model.Tarea
import org.donnico.projectv1.notification.TaskNotifier

class TaskViewModel(
    private val notifier: TaskNotifier
) : ViewModel() {
    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas = _tareas.asStateFlow()

    private val _isSilenced = MutableStateFlow(notifier.isSilenced())
    val isSilenced = _isSilenced.asStateFlow()

    private val knownIds = mutableSetOf<String>()
    private var firstServerSnapshotReceived = false

    init {
        listenRealtimeChanges()
    }

    private fun listenRealtimeChanges() {
        viewModelScope.launch {
            try {
                Firebase.firestore.collection("tareas")
                    .snapshots
                    .collect { snapshot ->
                        println("FIRESTORE_DEBUG: snapshot recibido, isFromCache=${snapshot.metadata.isFromCache}, docs=${snapshot.documents.size}, changes=${snapshot.documentChanges.size}")

                        for (change in snapshot.documentChanges) {
                            println("FIRESTORE_DEBUG: change type=${change.type} id=${change.document.id}")
                            when (change.type) {
                                ChangeType.ADDED -> {
                                    val id = change.document.id

                                    if (!snapshot.metadata.isFromCache &&
                                        firstServerSnapshotReceived &&
                                        id !in knownIds
                                    ) {
                                        try {
                                            val titulo: String =
                                                change.document.get<String?>("titulo") ?: "Nueva tarea"
                                            println("FIRESTORE_DEBUG: notificando '$titulo'")
                                            notifier.showNotification(titulo)
                                        } catch (e: Exception) {
                                            println("FIRESTORE_DEBUG: error titulo: ${e.message}")
                                        }
                                    }
                                    knownIds.add(id)
                                }
                                ChangeType.REMOVED -> {
                                    knownIds.remove(change.document.id)
                                }
                                else -> { /* MODIFIED: no notificamos */ }
                            }
                        }

                        if (!snapshot.metadata.isFromCache) {
                            firstServerSnapshotReceived = true
                        }

                        val list = snapshot.documents.mapNotNull { doc ->
                            try {
                                val titulo: String = doc.get<String?>("titulo") ?: "Sin título"
                                val completada: Boolean = doc.get<Boolean?>("completada") ?: false
                                val timestamp: Long = try {
                                    doc.get<Long?>("timestamp") ?: 0L
                                } catch (e: Exception) {
                                    0L
                                }
                                Tarea(
                                    id = doc.id,
                                    titulo = titulo,
                                    completada = completada,
                                    timestamp = timestamp
                                )
                            } catch (e: Exception) {
                                println("FIRESTORE_DEBUG: error doc id=${doc.id}: ${e.message}")
                                null
                            }
                        }.sortedByDescending { it.timestamp }

                        _tareas.value = list
                    }
            } catch (e: Exception) {
                println("FIRESTORE_DEBUG: error listener: ${e.message}")
            }
        }
    }

    fun agregarTarea(titulo: String) {
        if (titulo.isBlank()) return
        viewModelScope.launch {
            try {
                val data = mapOf(
                    "titulo" to titulo,
                    "completada" to false,
                    "timestamp" to Clock.System.now().toEpochMilliseconds()
                )
                Firebase.firestore.collection("tareas").add(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleSilenced(silenced: Boolean) {
        notifier.setSilenced(silenced)
        _isSilenced.value = silenced
    }
}