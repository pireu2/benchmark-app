package app.benchmarkapp.ui.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceInfoProvider
import app.benchmarkapp.ui.theme.backgroundColor

@SuppressLint("NewApi")
@Composable
fun SpecsWidget(context: Context, modifier: Modifier = Modifier) {
    val deviceInfoProvider = DeviceInfoProvider(context)
    val specs = remember { deviceInfoProvider.getDeviceInfo() }



    Surface(
        color = backgroundColor,
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