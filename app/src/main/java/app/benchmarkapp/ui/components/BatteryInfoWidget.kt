package app.benchmarkapp.ui.components

import CircularProgress
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
import app.benchmarkapp.ui.theme.Green40

@Composable
fun BatteryInfoWidget(info : DeviceInfoProvider.BatteryInfo){
    val status = when(info.status){
        1 -> "Charging"
        2 -> "Discharging"
        3 -> "Not Charging"
        4 -> "Full"
        else -> "Unknown"
    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Battery Information",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Health: ${info.health}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Total Capacity: ${info.totalCapacity}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Current Capacity: ${info.remainingCapacity}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Voltage: ${info.voltage} mV",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = "Status: $status", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Temperature: ${info.temperature}°C",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            CircularProgress(
                total = info.totalCapacity.toFloat(),
                available = info.remainingCapacity.toFloat(),
                color = Green40,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
            )
        }
    }
}