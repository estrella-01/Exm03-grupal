package org.donnico.projectv1.e1_firestore.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskScreen(
    viewModel: TaskViewModel,
    onRequestPermission: () -> Unit = {}
) {
    var nuevoTitulo by remember { mutableStateOf("") }
    val tareas by viewModel.tareas.collectAsState()
    val isSilenced by viewModel.isSilenced.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Ejercicio 1: Firestore + Notificaciones", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Fila para pedir permiso de notificación
        Button(
            onClick = onRequestPermission,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solicitar Permiso Notificaciones (Android 13+)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Switch para silenciar notificaciones (persiste en SharedPreferences)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Silenciar Notificaciones: ", modifier = Modifier.weight(1f))
            Switch(
                checked = isSilenced,
                onCheckedChange = { viewModel.toggleSilenced(it) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input para agregar nueva tarea
        OutlinedTextField(
            value = nuevoTitulo,
            onValueChange = { nuevoTitulo = it },
            label = { Text("Título de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.agregarTarea(nuevoTitulo)
                nuevoTitulo = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista reactiva de tareas desde Firestore
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tareas) { tarea ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(tarea.titulo, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
