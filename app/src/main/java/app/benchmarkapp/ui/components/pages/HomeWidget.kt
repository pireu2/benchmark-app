package app.benchmarkapp.ui.components.pages

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.benchmarkapp.BenchmarkScore
import app.benchmarkapp.DeviceInfoProvider
import app.benchmarkapp.DeviceStats
import app.benchmarkapp.FirebaseHelper
import app.benchmarkapp.ui.theme.Purple40
import app.benchmarkapp.ui.theme.backgroundColor

@SuppressLint("NewApi")
@Composable
fun HomeWidget(context: Context, modifier: Modifier = Modifier) {
    val deviceInfoProvider = DeviceInfoProvider(context)
    deviceInfoProvider.updateDeviceInfo()

    val allScoresAvailable = DeviceStats.getSingleThreadedScore() != null &&
            DeviceStats.getMultiThreadedScore() != null &&
            DeviceStats.getGpuScore() != null &&
            DeviceStats.getRamScore() != null &&
            DeviceStats.getStorageScore() != null &&
            DeviceStats.getTotalScore() != null

    val scores = remember { mutableStateListOf<BenchmarkScore>() }

    LaunchedEffect(Unit) {
        FirebaseHelper.getScores { fetchedScores ->
            scores.clear()
            scores.addAll(fetchedScores)
        }
    }

    Surface(
        color = backgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = modifier.padding(8.dp)) {
            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    {
                        Text(
                            text = "Last Benchmark Results",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "Total Score: ${DeviceStats.getTotalScore() ?: "N/A"}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "Single Threaded Score: ${DeviceStats.getSingleThreadedScore() ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Multi Threaded Score: ${DeviceStats.getMultiThreadedScore() ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "GPU Score: ${DeviceStats.getGpuScore() ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "RAM Score: ${DeviceStats.getRamScore() ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Storage Score: ${DeviceStats.getStorageScore() ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                                onClick = { FirebaseHelper.submitCurrentScore() },
                                enabled = allScoresAvailable && DeviceStats.isLoggedIn,
                            ) {
                                Text(text = "Submit Scores")
                            }
                        }
                    }
                }
            }
            if (DeviceStats.isLoggedIn) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                        {
                            Text(
                                text = "Online Submitted Scores",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                items(scores) { score ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Device: ${score.device}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Total Score: ${score.totalScore}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Single Threaded Score: ${score.singleThreadedScore}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Multi Threaded Score: ${score.multiThreadedScore}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "GPU Score: ${score.gpuScore}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "RAM Score: ${score.ramScore}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Storage Score: ${score.storageScore}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                item {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                        {
                            Text(
                                text = "Could not connect to the server",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}