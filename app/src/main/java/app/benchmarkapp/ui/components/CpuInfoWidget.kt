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
fun CpuInfoWidget(info : DeviceInfoProvider.CpuInfo){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "CPU Information", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = "Vendor: ${info.vendor}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Model: ${info.model}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Cores: ${info.cores}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Clock Speed: ${info.frequency} MHz", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Architecture: ${info.architecture}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}