package app.benchmarkapp.ui.components


import CircularProgress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column (modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                ) {
                    Text(
                        text = "Total RAM:\n ${info.totalRam / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Available RAM:\n ${info.availableRam / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Box (modifier = Modifier.align(Alignment.CenterHorizontally)){
                        CircularProgress(
                            total = info.totalRam.toFloat(),
                            available = info.availableRam.toFloat(),
                            modifier = Modifier
                                .padding(top = 16.dp)
                        )
                    }
                }
                Column (modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()

                ) {
                    Text(
                        text = "Total Storage:\n ${info.totalStorage / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Available Storage:\n ${info.availableStorage / 1000000} MB",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        CircularProgress(
                            total = info.totalStorage.toFloat(),
                            available = info.availableStorage.toFloat(),
                            modifier = Modifier
                                .padding(top = 16.dp)
                        )
                    }
                }
            }
        }

    }
}