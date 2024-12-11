package app.benchmarkapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object DeviceStats {
    var deviceInfo: DeviceInfoProvider.DeviceInfo ? = null
    var disableNavigation by  mutableStateOf(false)
    var singleThreadedScore: Long? = null
    var multiThreadedScore: Long? = null
    var ramScore: Long? = null
    var storageScore: Long? = null
    var gpuScore: Long? = null

    var cacheDirPath: String? = null
}