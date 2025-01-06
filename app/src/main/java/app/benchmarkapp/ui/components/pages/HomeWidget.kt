package app.benchmarkapp.ui.components.pages

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceInfoProvider
import app.benchmarkapp.DeviceStats
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
            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Last Benchmark Results", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = "Total Score: ${DeviceStats.getTotalScore() ?: "N/A"}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(text = "Single Threaded Score: ${DeviceStats.getSingleThreadedScore() ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Multi Threaded Score: ${DeviceStats.getMultiThreadedScore() ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "GPU Score: ${DeviceStats.getGpuScore() ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "RAM Score: ${DeviceStats.getRamScore() ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Storage Score: ${DeviceStats.getStorageScore() ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}