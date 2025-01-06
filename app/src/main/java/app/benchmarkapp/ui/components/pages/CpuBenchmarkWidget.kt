package app.benchmarkapp.ui.components.pages


import CircularProgress
import GraphDialog
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.mutableIntStateOf
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
import app.benchmarkapp.ui.components.CpuInfoWidget
import app.benchmarkapp.ui.theme.Purple40
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.math.floor

@SuppressLint("NewApi")
@Composable
fun CpuBenchmarkWidget(context: Context) {

    val singleThreadedDispatcher = Executors.newSingleThreadExecutor {
            runnable -> Thread(runnable).apply {
                priority = Thread.MAX_PRIORITY
        }
    }

    var isRunning by remember { mutableStateOf(false) }
    val cores = DeviceStats.deviceInfo?.cpu?.cores
    val scope = rememberCoroutineScope()


    var singleThreadedProgress by remember { mutableFloatStateOf(0f) }
    var multiThreadedProgress by remember { mutableFloatStateOf(0f) }

    var singleThreadedScore by remember { mutableStateOf<Long?>(null) }
    var multiThreadedScore by remember { mutableStateOf<Long?>(null) }

    var showDialog by remember { mutableStateOf(false) }

    var lastBenchmark by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            singleThreadedProgress = MainActivity.getSingleThreadedProgress()
            cores?.let { MainActivity.getMultiThreadedProgress(it) }?.let { multiThreadedProgress = it }
            kotlinx.coroutines.delay(100)
        }
    }

    LazyColumn {
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { CpuInfoWidget(DeviceStats.deviceInfo?.cpu ?: return@item) }
        item {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Single Threaded Benchmark",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "This benchmark will perform a single threaded benchmark on the CPU of the device.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                            enabled = !isRunning,
                            onClick = {
                                lastBenchmark = 1
                                if (isRunning) return@Button
                                isRunning = true
                                DeviceStats.disableNavigation = true
                                scope.launch(singleThreadedDispatcher.asCoroutineDispatcher()) {
                                    singleThreadedScore = MainActivity.singleThreadedBenchmark()
                                    DeviceStats.setSingleThreadedScore(singleThreadedScore!!)
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
                            available = floor(singleThreadedProgress * 100),
                            circleSize = 150.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Score: ${singleThreadedScore ?: (DeviceStats.getSingleThreadedScore() ?: "N/A")}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                            onClick = { showDialog = true },
                            enabled = singleThreadedScore != null && lastBenchmark == 1
                        ) {
                            Text(text = "View Graphs")
                        }
                    }
                }
            }
        }
        cores?.let { Executors.newFixedThreadPool(it) }?.let {
            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Multi Threaded Benchmark",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "This benchmark will perform a multi threaded benchmark on the CPU of the device.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                                enabled = !isRunning,
                                onClick = {
                                    lastBenchmark = 2
                                    if (isRunning) return@Button
                                    isRunning = true
                                    DeviceStats.disableNavigation = true
                                    scope.launch(it.asCoroutineDispatcher()) {
                                        multiThreadedScore = MainActivity.multiThreadedBenchmark(cores)
                                        DeviceStats.setMultiThreadedScore(multiThreadedScore!!)
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
                                available = floor(multiThreadedProgress * 100),
                                circleSize = 150.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Score: ${multiThreadedScore ?: (DeviceStats.getMultiThreadedScore() ?: "N/A")}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                                onClick = { showDialog = true },
                                enabled = multiThreadedScore != null && lastBenchmark == 2
                            ) {
                                Text(text = "View Graphs")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        GraphDialog(context = context, onDismiss = { showDialog = false })
    }
}
