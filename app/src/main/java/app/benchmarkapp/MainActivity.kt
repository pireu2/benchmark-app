package app.benchmarkapp

import android.annotation.SuppressLint
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
import app.benchmarkapp.graphics.Renderer
import app.benchmarkapp.ui.components.pages.CpuBenchmarkWidget
import app.benchmarkapp.ui.components.pages.GpuBenchmarkWidget
import app.benchmarkapp.ui.components.pages.HomeWidget
import app.benchmarkapp.ui.components.pages.RamBenchmarkWidget
import app.benchmarkapp.ui.components.SideMenu
import app.benchmarkapp.ui.components.pages.SpecsWidget
import app.benchmarkapp.ui.theme.BenchmarkAppTheme
import app.benchmarkapp.ui.components.TitleBar
import app.benchmarkapp.ui.components.pages.StorageBenckmarkWidget
import com.github.mikephil.charting.utils.Utils
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    companion object
    {
        init{
            System.loadLibrary("benchmarkapp")
        }
        external fun singleThreadedBenchmark(): Long
        external fun getSingleThreadedProgress(): Float

        external fun multiThreadedBenchmark(numThreads: Int): Long
        external fun getMultiThreadedProgress(numThreads: Int): Float

        external fun ramBenchmark(): Long
        external fun getRamProgress(): Float

        external fun storageBenchmark(): Long
        external fun getStorageProgress(): Float

        external fun setCacheDirPath(path: String)
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        auth.signInAnonymously().addOnCompleteListener(this){
            if(it.isSuccessful){
                DeviceStats.isLoggedIn = true
            }
        }
        DeviceStats.init(this)
        Utils.init(this)
        enableEdgeToEdge()



        CoroutineScope(Dispatchers.IO).launch {
            Renderer.getResources(this@MainActivity)
        }

        DeviceStats.cacheDirPath = this.cacheDir.absolutePath
        setCacheDirPath(this.cacheDir.absolutePath)


        setContent {
            BenchmarkAppTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                DeviceInfoProvider(this).updateDeviceInfo()

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
                                composable("device_info") { SpecsWidget() }
                                composable("cpu_benchmark") { CpuBenchmarkWidget(context = this@MainActivity) }
                                composable("gpu_benchmark") { GpuBenchmarkWidget() }
                                composable("ram_benchmark") { RamBenchmarkWidget(context = this@MainActivity) }
                                composable("storage_benchmark") { StorageBenckmarkWidget(context = this@MainActivity) }
                            }
                        }
                    }
                }
            }
        }
    }
}