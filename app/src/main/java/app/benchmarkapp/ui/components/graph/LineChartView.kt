package app.benchmarkapp.ui.components.graph

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.benchmarkapp.ui.theme.backgroundColor
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.LargeValueFormatter

@Composable
fun LineChartView(context: Context, lineData: LineData) {
    AndroidView(
        factory = {
            LineChart(context).apply {
                data = lineData
                description.apply {
                    text = "X: scale factor of input, Y: time in ms"
                    textColor = android.graphics.Color.WHITE
                    textSize = 12f
                }
                xAxis.apply {
                    textColor = android.graphics.Color.WHITE
                    setDrawLabels(true)
                    setDrawAxisLine(true)
                    setDrawGridLines(false)
                }
                axisLeft.apply {
                    textColor = android.graphics.Color.WHITE
                    setDrawLabels(true)
                    setDrawAxisLine(true)
                    setDrawGridLines(true)
                    valueFormatter = LargeValueFormatter()
                }
                axisRight.isEnabled = false
                legend.textColor = android.graphics.Color.WHITE
                data.dataSets.forEach { it.valueTextColor = android.graphics.Color.WHITE }
                invalidate()
            }
        },
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(400.dp)
    )
}