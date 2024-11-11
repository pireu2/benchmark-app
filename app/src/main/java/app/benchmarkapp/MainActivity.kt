package app.benchmarkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.benchmarkapp.ui.components.CpuBenchmarkWidget
import app.benchmarkapp.ui.components.HomeWidget
import app.benchmarkapp.ui.components.SideMenu
import app.benchmarkapp.ui.components.SpecsWidget
import app.benchmarkapp.ui.theme.BenchmarkAppTheme
import app.benchmarkapp.ui.components.TitleBar


class MainActivity : ComponentActivity() {
    companion object
    {
        init{
            System.loadLibrary("benchmarkapp")
        }
        external fun cpuBenchmark(): Long
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BenchmarkAppTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        SideMenu(drawerState = drawerState, navController)
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TitleBar(
                                drawerState = drawerState
                            )
                        }
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            NavHost(navController = navController, startDestination = "home") {
                                composable("home") { HomeWidget(context = this@MainActivity) }
                                composable("device_info") { SpecsWidget(context = this@MainActivity) }
                                composable("cpu_benchmark") { CpuBenchmarkWidget { cpuBenchmark() } }
                                composable("gpu_benchmark") { /* TODO */ }
                                composable("ram_benchmark") { /* TODO */ }
                                composable("storage_benchmark") { /* TODO */ }
                            }
                        }
                    }
                }
            }
        }
    }
}