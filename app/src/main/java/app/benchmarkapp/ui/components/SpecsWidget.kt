package app.benchmarkapp.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceInfoProvider
import kotlinx.coroutines.delay

@Composable
fun SpecsWidget(context: Context, modifier: Modifier = Modifier) {
    val deviceInfoProvider = DeviceInfoProvider(context)
    var specs by remember { mutableStateOf(deviceInfoProvider.getDeviceInfo()) }

    LaunchedEffect(Unit) {
        delay(10)
        val newSpecs = deviceInfoProvider.getDeviceInfo()
        specs = newSpecs
    }

    Surface(
        color = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = modifier.padding(8.dp)) {
            CpuInfoWidget(info = specs.cpu)
            specs.gpu?.let { GpuInfoWidget(info = it) }
            MemoryInfoWidget(info = specs.memory)
        }
    }
}