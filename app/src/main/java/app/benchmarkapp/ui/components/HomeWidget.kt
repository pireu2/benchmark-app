package app.benchmarkapp.ui.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceInfoProvider
import app.benchmarkapp.ui.theme.backgroundColor

@SuppressLint("NewApi")
@Composable
fun HomeWidget(context: Context, modifier: Modifier = Modifier) {
    val deviceInfoProvider = DeviceInfoProvider(context)
    deviceInfoProvider.updateDeviceInfo()



    Surface(
        color = backgroundColor,
        modifier = modifier.fillMaxWidth().fillMaxHeight()
    ) {
        LazyColumn(modifier = modifier.padding(8.dp)) {

        }
    }
}