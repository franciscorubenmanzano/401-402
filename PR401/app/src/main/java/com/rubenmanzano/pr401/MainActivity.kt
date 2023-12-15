
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Notas()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notas() {
    var nota by remember { mutableStateOf("") }
    var notasList by remember { mutableStateOf(listOf<String>()) }
    var showStatistics by remember { mutableStateOf(false) }
    var statisticsText by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var noteToDeletePosition by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nota,
            onValueChange = { nota = it },
            label = { Text("Introduce una nota") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (nota.isNotBlank()) {
                        notasList = notasList + nota
                        nota = ""
                    }
                },
                enabled = nota.isNotBlank()
            ) {
                Text("Añadir Nota")
            }

            Button(
                onClick = {
                    if (nota.isNotBlank()) {
                        notasList = notasList + nota
                        nota = ""
                    }

                    // Calcular y mostrar la nota más alta, su posición y el promedio especial
                    calcularEstadisticas(notasList) { result ->
                        // Actualizar las variables de estado para provocar la recomposición
                        statisticsText = result
                        showStatistics = true
                    }
                },
                enabled = notasList.isNotEmpty()
            ) {
                Text("Finalizar Inserción")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la lista de notas ingresadas
        for ((index, notaItem) in notasList.withIndex()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = notaItem)
                Button(
                    onClick = {
                        showDeleteDialog = true
                        noteToDeletePosition = index
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Eliminar")
                }
            }
        }

        if (showStatistics) {
            Text(statisticsText)
        }

        // Cuadro de diálogo de confirmación para eliminar la nota
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    noteToDeletePosition = -1
                },
                title = {
                    Text("Confirmar Eliminación")
                },
                text = {
                    Text("¿Estás seguro de que quieres eliminar esta nota?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (noteToDeletePosition != -1) {
                                // Eliminar la nota en la posición especificada
                                notasList = notasList.toMutableList().apply {
                                    removeAt(noteToDeletePosition)
                                }
                                showDeleteDialog = false
                                noteToDeletePosition = -1
                            }
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            noteToDeletePosition = -1
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

// Función para calcular y mostrar estadísticas
fun calcularEstadisticas(notas: List<String>, onComplete: (String) -> Unit) {
    val notasDoubles = notas.map { it.toDouble() }
    val notamasalta = notasDoubles.max()
    val posicionnotamasalta = notasDoubles.indexOf(notamasalta)
    val notamasbaja = notasDoubles.min()

    val mediaespecial =
        (notasDoubles.sum() - notamasalta!! - notamasbaja!!).toDouble() / (notasDoubles.size - 2)

    // Llamar a onComplete con el resultado
    onComplete(
        "La nota más alta es $notamasalta y su posición es $posicionnotamasalta\n" +
                "La media especial es $mediaespecial"
    )
}
