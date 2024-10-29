package app.benchmarkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import app.benchmarkapp.ui.components.SpecsWidget
import app.benchmarkapp.ui.theme.BenchmarkAppTheme
import app.benchmarkapp.ui.components.TitleBar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BenchmarkAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TitleBar() }
                )
                { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        SpecsWidget(this@MainActivity)
                    }
                }
            }
        }
    }
}

