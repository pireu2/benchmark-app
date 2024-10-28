package app.benchmarkapp


import androidx.compose.runtime.*
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.benchmarkapp.ui.theme.BenchmarkAppTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BenchmarkAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        SpecsWidget(this@MainActivity, Modifier.padding(innerPadding))
                    }

                }
            }
        }
    }
}


fun getDeviceSpecs(context: Context) : String{
    val deviceInfoProvider = DeviceInfoProvider(context)
    val deviceInfo = deviceInfoProvider.getDeviceInfo()

    return """
        Model: ${deviceInfo.model}
        Brand: ${deviceInfo.brand}
        Version: ${deviceInfo.version}
        SDK: ${deviceInfo.sdk}
        CPU Info:
            Vendor: ${deviceInfo.cpu.vendor}
            Model: ${deviceInfo.cpu.model}
            Physical Cores: ${deviceInfo.cpu.physicalCores}
            Total Cores: ${deviceInfo.cpu.totalCores}
            Frequency: ${deviceInfo.cpu.frequency} MHz
            Architecture: ${deviceInfo.cpu.architecture}
            Cache Size: ${deviceInfo.cpu.cacheSize}
        Memory Info:
            Total RAM: ${deviceInfo.memory.totalRam} bytes
            Available RAM: ${deviceInfo.memory.availableRam} bytes
            Total Storage: ${deviceInfo.memory.totalStorage} bytes
            Available Storage: ${deviceInfo.memory.availableStorage} bytes
        GPU Info:
            Vendor: ${deviceInfo.gpu?.vendor}
            Model: ${deviceInfo.gpu?.model}
            Shading Language Version: ${deviceInfo.gpu?.shadingLanguageVersion}
            Version: ${deviceInfo.gpu?.version}
    """.trimIndent()
}

@Composable
fun SpecsWidget(context: Context, modifier: Modifier = Modifier) {
    var specs by remember { mutableStateOf(getDeviceSpecs(context)) }

    LaunchedEffect(Unit) {
        delay(10)
        val newSpecs = getDeviceSpecs(context)
        specs = newSpecs
    }

    Surface(color = Color.White) {
        Text(text = specs, modifier = modifier.padding(16.dp))
    }
}