import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.benchmarkapp.DeviceStats
import app.benchmarkapp.ui.components.graph.LineChartView
import app.benchmarkapp.ui.theme.Purple40
import app.benchmarkapp.ui.theme.backgroundColor
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.io.File

@Composable
fun GraphDialog(context: Context, onDismiss: () -> Unit) {
    val resultsFile = File(DeviceStats.cacheDirPath!! + "results.txt")
    val benchmarkData = mutableMapOf<String, MutableList<Entry>>()

    if (resultsFile.exists()) {
        resultsFile.forEachLine { line ->
            if (line.contains("Benchmark:")) {
                val parts = line.split(",")
                val benchmarkName = parts[0].split(":")[1].trim()
                val size = parts[1].split(":")[1].trim().toInt()
                val time = parts[2].split(":")[1].trim().toInt()
                val entry = Entry(size.toFloat(), time.toFloat())

                if (!benchmarkData.containsKey(benchmarkName)) {
                    benchmarkData[benchmarkName] = mutableListOf()
                }
                benchmarkData[benchmarkName]?.add(entry)
            }
        }
    }

    val colors = listOf(
        android.graphics.Color.RED,
        android.graphics.Color.BLUE,
        android.graphics.Color.GREEN,
        android.graphics.Color.YELLOW,
        android.graphics.Color.CYAN,
        android.graphics.Color.MAGENTA
    )

    val lineDataSets = benchmarkData.map { (name, entries) ->
        LineDataSet(entries, name).apply {
            color = colors[benchmarkData.keys.indexOf(name) % colors.size]
        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Results Graph") },
        text = {
            LineChartView(context, LineData(lineDataSets))
        },
        containerColor = backgroundColor,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                ) {
                Text("Close")
            }
        }
    )
}