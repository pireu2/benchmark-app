package app.benchmarkapp.ui.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.benchmarkapp.ui.theme.Purple40
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Composable
fun CpuBenchmarkWidget(cpuBenchmark: () -> Long){
    val scope = rememberCoroutineScope()
    var score by remember { mutableStateOf<Long?> (null)}

    val highPriorityDispatcher = Executors.newSingleThreadExecutor {
        runnable -> Thread(runnable).apply{
            priority = Thread.MAX_PRIORITY
        }
    }.asCoroutineDispatcher()

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "CPU Benchmark", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40
                ),
                onClick = {
                    scope.launch(highPriorityDispatcher) {
                        score = cpuBenchmark()
                    }
                }
            ) {
                Text(text = "Start Benchmark")
            }
            score?.let {
                Text(text = "Score: $it", style = MaterialTheme.typography.bodyMedium)
            }

        }
    }
}