package app.benchmarkapp.ui.components

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
import androidx.compose.runtime.getValue
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
import app.benchmarkapp.ui.theme.Purple40
import app.benchmarkapp.ui.theme.backgroundColor
import kotlinx.coroutines.launch
import kotlin.math.floor

@SuppressLint("NewApi")
@Composable
fun GpuBenchmarkWidget(modifier: Modifier = Modifier) {
    var isRunning by remember { mutableStateOf(false) }
    var fps by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()

    if(DeviceStats.deviceInfo?.gpu == null) {
        return Text("This device does not support GPU benchmarking.")
    }


    LazyColumn(
        modifier = modifier
            .background(backgroundColor)
    ){
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
                                scope.launch {
                                    fps = Renderer.fps
                                    isRunning = false
                                }
                            }
                        ) {
                            Text(text = "Start Benchmark")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        CircularProgress(
                            total = 100f,
                            available = 0f,
                            circleSize = 150.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Fps: ${floor(fps).toInt()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        item{ DeviceStats.deviceInfo?.gpu?.let { GpuInfoWidget(it) } }
    }
}