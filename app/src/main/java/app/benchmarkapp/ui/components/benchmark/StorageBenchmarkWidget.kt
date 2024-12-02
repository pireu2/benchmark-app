package app.benchmarkapp.ui.components.benchmark


import CircularProgress
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceStats
import app.benchmarkapp.MainActivity
import app.benchmarkapp.ui.components.MemoryInfoWidget
import app.benchmarkapp.ui.theme.Purple40
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.math.floor

@SuppressLint("NewApi")
@Composable
fun StorageBenckmarkWidget() {

    val threadDispatcher = Executors.newSingleThreadExecutor {
            runnable -> Thread(runnable).apply {
        priority = Thread.MAX_PRIORITY
    }
    }

    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var score by remember { mutableStateOf<Long?>(null) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            progress = MainActivity.getStorageProgress()
            kotlinx.coroutines.delay(100)
        }
    }

    LazyColumn {
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { MemoryInfoWidget(DeviceStats.deviceInfo?.memory ?: return@item) }
        item {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Storage Benchmark",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "This benchmark will perform a benchmark of the storage of the device.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                            enabled = !isRunning,
                            onClick = {
                                if (isRunning) return@Button
                                isRunning = true
                                DeviceStats.disableNavigation = true
                                scope.launch(threadDispatcher.asCoroutineDispatcher()) {
                                    score = MainActivity.storageBenchmark()
                                    DeviceStats.storageScore = score
                                    isRunning = false
                                    DeviceStats.disableNavigation = false
                                }
                            }
                        ) {
                            Text(text = "Start Benchmark")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        CircularProgress(
                            total = 100f,
                            available = floor(progress * 100),
                            circleSize = 150.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Score: ${score ?: (DeviceStats.storageScore ?: "N/A")}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

    }
}
