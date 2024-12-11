package app.benchmarkapp.ui.components.benchmark

import CircularProgress
import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.viewinterop.AndroidView
import app.benchmarkapp.DeviceStats
import app.benchmarkapp.graphics.Renderer
import app.benchmarkapp.ui.components.GpuInfoWidget
import app.benchmarkapp.ui.theme.Purple40
import app.benchmarkapp.ui.theme.backgroundColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
@Composable
fun GpuBenchmarkWidget(modifier: Modifier = Modifier) {
    var isRunning by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var score by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var isLoaded by remember { mutableStateOf(Renderer.loaded) }

    LaunchedEffect(Unit) {
        while (!Renderer.loaded) {
            delay(100)
        }
        isLoaded = true
    }

    if (!isLoaded) {
        Text(
            modifier = modifier.padding(8.dp),
            text="Loading..."
        )
        return
    }

    if (DeviceStats.deviceInfo?.gpu == null) {
        Text(
            modifier = modifier.padding(8.dp),
            text="This device does not support GPU benchmarking."
        )
        return
    }


    LazyColumn(
        modifier = modifier
            .background(backgroundColor)
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AndroidView(
                    factory = {
                        GLSurfaceView(it).apply {
                            setEGLContextClientVersion(2)
                            setRenderer(Renderer)
                            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        item {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "GPU Benchmark",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "This benchmark will graphics benchmark your device.",
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
                                scope.launch {
                                    progress = 0f
                                    Renderer.startBenchmark()
                                    while (Renderer.getElapsedTime() < Renderer.BENCHMARK_DURATION) {
                                        progress = (Renderer.getElapsedTime().toFloat() / Renderer.BENCHMARK_DURATION) * 100
                                        delay(100)
                                    }
                                    delay(200)
                                    score = (Renderer.frameCounts.average() * 4 * 100).toInt()
                                    DeviceStats.gpuScore = score.toLong()
                                    progress = 100f
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
                            available = progress,
                            circleSize = 150.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Score: ${DeviceStats.gpuScore ?: if (score == 0) "N/A" else score}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        item { DeviceStats.deviceInfo?.gpu?.let { GpuInfoWidget(it) } }
    }
}