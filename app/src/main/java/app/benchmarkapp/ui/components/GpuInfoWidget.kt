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
fun GpuInfoWidget(info : DeviceInfoProvider.GpuInfo){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "GPU Information", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = "Model: ${info.model}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Version: ${info.version}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Vendor: ${info.vendor}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Shading Language Version: ${info.shadingLanguageVersion} GHz", style = MaterialTheme.typography.bodyMedium)
        }
    }
}