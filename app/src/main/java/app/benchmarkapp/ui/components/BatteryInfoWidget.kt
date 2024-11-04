package app.benchmarkapp.ui.components

import CircularProgress
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BatteryInfoWidget(info : DeviceInfoProvider.BatteryInfo){
    val chargingStatus = DeviceInfoProvider.getChargingStatus(info.status)
    val healthStatus = DeviceInfoProvider.getBatteryHealthStatus(info.health)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Battery Information",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(modifier = Modifier
                    .padding(8.dp)
                ) {
                    Text(text = "Health: $healthStatus", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Total Capacity: ${info.totalCapacity} mAh",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Current Capacity: ${info.remainingCapacity} mAh",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Voltage: ${info.voltage} mV",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(text = "Status: $chargingStatus", style = MaterialTheme.typography.bodyMedium)
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
                        .padding(top = 8.dp)
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                )
            }
        }

    }
}