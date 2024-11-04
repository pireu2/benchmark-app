package app.benchmarkapp

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.opengl.GLSurfaceView
import android.os.BatteryManager
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException



/**
 * Provides device information
 * @param context Application context
 */
class DeviceInfoProvider(private val context: Context) {

    /**
     * @param model Device model
     * @param brand Device brand
     * @param display Device display
     * @param sdk Android SDK version
     * @param cpu CPU info
     * @param memory Memory info
     * @param battery Battery info
     * @param gpu GPU info
     */
    data class DeviceInfo(
        val model: String,
        val brand: String,
        val display: String,
        val sdk: Int,
        val cpu: CpuInfo,
        val memory: MemoryInfo,
        val battery: BatteryInfo,
        val gpu: GpuInfo? = null
    )

    /**
     * @param vendor CPU vendor
     * @param model CPU model
     * @param physicalCores Number of physical cores
     * @param totalCores Total number of cores
     * @param frequency CPU frequency in MHz
     * @param architecture CPU architecture
     * @param cacheSize CPU cache size in KB
     */
    data class CpuInfo(
        val vendor: String,
        val model: String,
        val physicalCores: Int,
        val totalCores: Int,
        val frequency: Double,
        val architecture: String,
        val cacheSize: Int
    )

    /**
     * @param totalRam in bytes
     * @param availableRam in bytes
     * @param totalStorage in bytes
     * @param availableStorage in bytes
     */
    data class MemoryInfo(
        val totalRam: Long,
        val availableRam: Long,
        val totalStorage: Long,
        val availableStorage: Long
    )

    /**
     * @param vendor GPU vendor
     * @param model GPU model
     * @param shadingLanguageVersion Supported shading language version
     * @param version OpenGL version
     */
    data class GpuInfo(
        val vendor: String,
        val model: String,
        val shadingLanguageVersion: String,
        val version: String
    )

    /**
     * @param totalCapacity Battery total capacity in mAh
     * @param remainingCapacity Battery remaining capacity in mAh
     * @param health Battery health
     * @param status Battery status
     * @param voltage Battery voltage in mV
     * @param temperature Battery temperature in °C
     */
    data class BatteryInfo(
        val totalCapacity: Long,
        val remainingCapacity: Int,
        val health: Int,
        val status: Int,
        val voltage: Int,
        val temperature: Int
    )

    private var deviceInfo: DeviceInfo

    init {
        deviceInfo = createDeviceInfo()
    }

    fun getDeviceInfo() : DeviceInfo{
        deviceInfo = createDeviceInfo()
        return deviceInfo
    }


    private fun createDeviceInfo() : DeviceInfo{
        return if(supportsGpu()){
            DeviceInfo(
                Build.MODEL,
                Build.BRAND,
                Build.DISPLAY,
                Build.VERSION.SDK_INT,
                getCpuInfo(),
                getMemoryInfo(),
                getBatteryInfo(),
                getGpuInfo()
            )
        } else {
            DeviceInfo(
                Build.MODEL,
                Build.BRAND,
                Build.DISPLAY,
                Build.VERSION.SDK_INT,
                getCpuInfo(),
                getMemoryInfo(),
                getBatteryInfo()
            )
        }
    }

    private fun getBatteryInfo() : BatteryInfo{
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val totalCapacity =  batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val remainingCapacity = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val batteryHealth = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val voltage = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) ?: -1
        val temperature = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ?: -1

        return BatteryInfo(
            totalCapacity,
            remainingCapacity,
            batteryHealth,
            status,
            voltage,
            temperature,
        )
    }

    private fun supportsGpu() : Boolean{
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.deviceConfigurationInfo.reqGlEsVersion >= 0x20000
    }

    private fun getGpuInfo() : GpuInfo{
        val glSurfaceView = GLSurfaceView(context)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(Renderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        val frameLayout = FrameLayout(context)
        frameLayout.addView(glSurfaceView)
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams()
        windowManager.addView(frameLayout, layoutParams)

        glSurfaceView.requestRender()

        windowManager.removeView(frameLayout)

        return GpuInfo(
            Renderer.vendor,
            Renderer.model,
            Renderer.shadingLanguageVersion,
            Renderer.version
        )
    }

    private fun getCpuInfo() : CpuInfo{
        val cpuInfoString : String = readCpuInfo()

        val vendor = Util.extractRegexFromString("""vendor_id\s*:\s*(\w+)""", cpuInfoString) ?: "Unknown"
        val model = Util.extractRegexFromString("""model name\s*:\s*([^\n]+)""", cpuInfoString) ?: "Unknown"
        val frequency = Util.extractRegexFromString("""cpu MHz\s*:\s*(\d+\.\d+)""", cpuInfoString)?.toDouble() ?: 0.0
        val cacheSize = Util.extractRegexFromString("""cache size\s*:\s*(\d+)\s\w+""", cpuInfoString)?.toInt() ?: 0
        val siblings = Util.extractRegexFromString("""siblings\s*:\s*(\d+)""", cpuInfoString)?.toInt() ?: 0
        val cores = Util.extractRegexFromString("""cpu cores\s*:\s*(\d+)""", cpuInfoString)?.toInt() ?: 0

        return CpuInfo(
            vendor,
            model,
            cores,
            siblings,
            frequency,
            Build.SUPPORTED_ABIS[0],
            cacheSize
        )
    }

    private fun getMemoryInfo() : MemoryInfo{
        val statFs = StatFs(Environment.getDataDirectory().path)
        val totalBytes = statFs.blockSizeLong * statFs.blockCountLong
        val availableBytes = statFs.blockSizeLong * statFs.availableBlocksLong

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        return MemoryInfo(
            memoryInfo.totalMem,
            memoryInfo.availMem,
            totalBytes,
            availableBytes
        )
    }

    private fun readCpuInfo() : String {
        return  try{
            val reader = BufferedReader(FileReader("/proc/cpuinfo"))
            val cpuInfo = reader.readText()
            Log.d("CpuInfo", cpuInfo)
            reader.close()
            cpuInfo
        } catch (e: IOException){
            Log.e("CpuInfo", "Error reading /proc/cpuinfo", e)
            "Unavailable"
        }
    }

}