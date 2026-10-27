package org.donnico.projectv1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.donnico.projectv1.e1_firestore.presentation.TaskScreen
import org.donnico.projectv1.e1_firestore.presentation.TaskViewModel

@Composable
fun App(
    taskViewModel: TaskViewModel? = null,
    onRequestPermission: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // Navegación entre los 3 ejercicios del examen
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("E1: Firestore") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("E2: Room") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("E3: WorkManager") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedTab) {
                0 -> {
                    if (taskViewModel != null) {
                        TaskScreen(
                            viewModel = taskViewModel,
                            onRequestPermission = onRequestPermission
                        )
                    } else {
                        Text("Cargando Firestore...", modifier = Modifier.padding(16.dp))
                    }
                }
                1 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("Ejercicio 2: Room (Se implementará a continuación)")
                    }
                }
                2 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("Ejercicio 3: WorkManager (Se implementará a continuación)")
                    }
                }
            }
        }
    }
}
