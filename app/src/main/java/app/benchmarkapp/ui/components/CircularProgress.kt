import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.benchmarkapp.ui.theme.Purple40

@Composable
fun CircularProgress(
    total: Float,
    available: Float,
    modifier: Modifier = Modifier,
    color: Color = Purple40,
    backgroundColor: Color = Color.LightGray,
    strokeWidth: Float = 16f
) {
    val progress = available / total
    val percentage = (progress * 100).toInt()

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(100.dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = backgroundColor,
                radius = size.minDimension / 2,
                style = Stroke(width = strokeWidth)
            )

            // Draw progress arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(text = "$percentage%", fontSize = 16.sp, color = color, fontWeight = FontWeight.Bold)
    }
}