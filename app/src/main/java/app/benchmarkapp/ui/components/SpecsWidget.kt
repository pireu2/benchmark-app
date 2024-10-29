package app.benchmarkapp.ui.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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

    LaunchedEffect(deviceInfoProvider) {
        delay(100)
        specs = deviceInfoProvider.getDeviceInfo()
    }

    Surface(
        color = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        LazyColumn(modifier = modifier.padding(8.dp)) {
            item {DeviceInfoWidget(info = specs)}
            item {CpuInfoWidget(info = specs.cpu)}
            specs.gpu?.let { item {GpuInfoWidget(info = it)} }
            item {MemoryInfoWidget(info = specs.memory)}
            item {BatteryInfoWidget(info = specs.battery)}
        }
    }
}