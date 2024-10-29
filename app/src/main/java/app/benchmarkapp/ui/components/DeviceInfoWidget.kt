package app.benchmarkapp.ui.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceInfoProvider

@Composable
fun DeviceInfoWidget(info : DeviceInfoProvider.DeviceInfo){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Device Information", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = "Model: ${info.model}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Brand: ${info.brand}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Sdk: ${info.sdk}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Display: ${info.display}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}