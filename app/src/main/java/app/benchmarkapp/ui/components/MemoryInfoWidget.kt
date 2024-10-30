package app.benchmarkapp.ui.components


import CircularProgress
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceInfoProvider

@Composable
fun MemoryInfoWidget(info: DeviceInfoProvider.MemoryInfo) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Memory Information", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Row {
                Column (modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Total RAM: ${info.totalRam / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Available RAM: ${info.availableRam / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    CircularProgress(
                        total = info.totalRam.toFloat(),
                        available = info.availableRam.toFloat(),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Column (modifier = Modifier.padding(top=8.dp, bottom = 8.dp, start = 8.dp)) {
                    Text(
                        text = "Total Storage: ${info.totalStorage / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Available Storage: ${info.availableStorage / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    CircularProgress(
                        total = info.totalStorage.toFloat(),
                        available = info.availableStorage.toFloat(),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

    }
}