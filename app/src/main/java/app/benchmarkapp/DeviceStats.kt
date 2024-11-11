package app.benchmarkapp

object DeviceStats {
    var deviceInfo: DeviceInfoProvider.DeviceInfo ? = null
    var singleThreadedScore: Long? = null
    var multiThreadedScore: Long? = null
}