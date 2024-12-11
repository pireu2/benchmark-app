package app.benchmarkapp.ui.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.benchmarkapp.DeviceInfoProvider
import app.benchmarkapp.ui.theme.backgroundColor
import java.io.File

@SuppressLint("NewApi")
@Composable
fun HomeWidget(context: Context, modifier: Modifier = Modifier) {
    val deviceInfoProvider = DeviceInfoProvider(context)
    deviceInfoProvider.updateDeviceInfo()

    val resultsFilePath = context.filesDir.absolutePath + "/results.txt"

    val file = File(resultsFilePath)
    if (!file.exists()) {
        file.createNewFile()
    }

    val results = file.readLines()


    Surface(
        color = backgroundColor,
        modifier = modifier.fillMaxWidth().fillMaxHeight()
    ) {
        LazyColumn(modifier = modifier.padding(8.dp)) {
            for (result in results) {
                item{
                    Text(text = result)
                }
            }
        }
    }
}