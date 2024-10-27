package app.benchmarkapp

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.WindowManager
import android.widget.FrameLayout
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.egl.EGLConfig


/**
 * Provides device information
 * @param context Application context
 */
class DeviceInfoProvider(private val context: Context) {

    /**
     * @param model Device model
     * @param brand Device brand
     * @param version Android version
     * @param sdk Android SDK version
     * @param cpu CPU info
     * @param memory Memory info
     * @param gpu GPU info
     */
    data class DeviceInfo(
        val model: String,
        val brand: String,
        val version: String,
        val sdk: Int,
        val cpu: CpuInfo,
        val memory: MemoryInfo,
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
     * @param frequency GPU frequency in MHz
     * @param architecture GPU architecture
     * @param memorySize GPU memory size in MB
     */
    data class GpuInfo(
        val vendor: String,
        val model: String,
        val frequency: Double,
        val memorySize: Int,
        val version: String
    )

    private val deviceInfo: DeviceInfo

    init {
        if(supportsGpu()){
            deviceInfo = DeviceInfo(
                Build.MODEL,
                Build.BRAND,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT,
                getCpuInfo(),
                getMemoryInfo(),
                getGpuInfo()
            )
        } else {
            deviceInfo = DeviceInfo(
                Build.MODEL,
                Build.BRAND,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT,
                getCpuInfo(),
                getMemoryInfo(),
            )
        }
    }

    fun getDeviceInfo() : DeviceInfo{
        return deviceInfo
    }

    private fun supportsGpu() : Boolean{
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.deviceConfigurationInfo.reqGlEsVersion >= 0x20000
    }

    private fun getGpuInfo() : GpuInfo{
        val glSurfaceView = GLSurfaceView(context)
        glSurfaceView.setEGLContextClientVersion(2)
        val renderer = object : GLSurfaceView.Renderer {
            var vendor: String = "Unknown"
            var model: String = "Unknown"
            var version: String = "Unknown"

            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                vendor = GLES20.glGetString(GLES20.GL_VENDOR)
                model = GLES20.glGetString(GLES20.GL_RENDERER)
                version = GLES20.glGetString(GLES20.GL_VERSION)
            }

            override fun onDrawFrame(gl: GL10?) {}
            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {}
        }

        glSurfaceView.setRenderer(renderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        // Add GLSurfaceView to a FrameLayout and attach it to the window manager
        val frameLayout = FrameLayout(context)
        frameLayout.addView(glSurfaceView)
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams()
        windowManager.addView(frameLayout, layoutParams)

        // Trigger rendering to initialize the OpenGL context
        glSurfaceView.requestRender()

        // Remove the GLSurfaceView from the window manager
        windowManager.removeView(frameLayout)

        return GpuInfo(
            renderer.vendor,
            renderer.model,
            0.0, // Placeholder for actual GPU frequency retrieval logic
            0, // Placeholder for actual GPU memory size retrieval logic
            renderer.version
        )
    }

    private fun getCpuInfo() : CpuInfo{
        val cpuInfoString : String = readCpuInfo()
        val regex = Regex(
            """processor\s*:\s*\d+\s*vendor_id\s*:\s*(\w+)\s*cpu family\s*:\s*\d+\s*model\s*:\s*\d+\s*model name\s*:\s*([\w\s]+)\s*stepping\s*:\s*\d+\s*microcode\s*:\s*\S+\s*cpu MHz\s*:\s*(\d+\.\d+)\s*cache size\s*:\s*(\d+)\s\w+\s*physical id\s*:\s*\d+\s*siblings\s*:\s*(\d+)\s*core id\s*:\s*\d+\s*cpu cores\s*:\s*(\d+)\s*apicid\s*:\s*\d+\s*initial apicid\s*:\s*\d+\s*fpu\s*:\s*\w+\s*fpu_exception\s*:\s*\w+\s*cpuid level\s*:\s*\d+\s*wp\s*:\s*\w+\s*flags\s*:\s*[\w\s]+\s*bugs\s*:\s*[\w\s]+\s*bogomips\s*:\s*\d+\.\d+\s*TLB size\s*:\s*\d+\s\w+\s\w+\s*clflush size\s*:\s*\d+\s*cache_alignment\s*:\s*\d+\s*address sizes\s*:\s*[\d\s\w,]+\s*power management\s*:"""
        )

        val matchResult = regex.find(cpuInfoString)
        if (matchResult != null){
            val (vendor, model, frequency, cacheSize, siblings, cores) = matchResult.destructured
            return CpuInfo(
                vendor,
                model,
                cores.toInt(),
                siblings.toInt(),
                frequency.toDouble(),
                Build.SUPPORTED_ABIS[0],
                cacheSize.toInt()
            )
        } else {
            return CpuInfo(
                "Unknown",
                "Unknown",
                0,
                0,
                0.0,
                "Unknown",
                0
            )
        }
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
            reader.close()
            cpuInfo
        } catch (e: IOException){
            "Unavailable"
        }
    }

}