package app.benchmarkapp.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.benchmarkapp.ui.theme.backgroundColor
import app.benchmarkapp.ui.theme.textColor
import kotlinx.coroutines.launch



@Composable
fun SideMenu(drawerState: DrawerState, navController: NavController) {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()

    val items = listOf(
        "Home",
        "Device Info",
        "CPU Benchmark",
        "GPU Benchmark",
        "RAM Benchmark",
        "Storage Benchmark"
    )

    ModalDrawerSheet(
        drawerContainerColor = backgroundColor,
        drawerContentColor = backgroundColor,
    ){
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(
            modifier = Modifier.padding(start = 8.dp),
            onClick = {
                scope.launch {
                    drawerState.close()
                }
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Close",
                tint = textColor
            )
        }
        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = backgroundColor
                ),
                label = { Text(text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (index == selectedItemIndex) FontWeight.Bold else FontWeight.Normal) },
                selected = index == selectedItemIndex,
                onClick = {
                    selectedItemIndex = index
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(when (index) {
                        0 -> "home"
                        1 -> "device_info"
                        2 -> "cpu_benchmark"
                        3 -> "gpu_benchmark"
                        4 -> "ram_benchmark"
                        5 -> "storage_benchmark"
                        else -> "home"
                    })
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

