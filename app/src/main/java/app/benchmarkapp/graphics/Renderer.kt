package app.benchmarkapp.graphics

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

object Renderer : GLSurfaceView.Renderer {

    var vendor: String = "Unknown"
    var model: String = "Unknown"
    var version: String = "Unknown"
    var shadingLanguageVersion: String = "Unknown"

    var loaded: Boolean = false

    const val BENCHMARK_DURATION = 20 // seconds
    private var enable: Boolean = false

    private var ratio : Float = 0.0f

    private var vertexShaderString: String? = null
    private var fragmentShaderString: String? = null

    private lateinit var shader: Shader
    private var teapot: Model3D? = null

    private var frameCount = 0L
    private var startTime = 0L
    private var intervalStartTime = 0L
    val frameCounts = mutableListOf<Int>()

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val normalMatrix = FloatArray(16)


    private var rotationAngle = 0f



    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        vendor = GLES20.glGetString(GLES20.GL_VENDOR)
        model = GLES20.glGetString(GLES20.GL_RENDERER)
        version = GLES20.glGetString(GLES20.GL_VERSION)
        shadingLanguageVersion = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION) ?: "Unknown"

        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        startTime = System.currentTimeMillis()

        shader = Shader(
            vertexShaderString ?: "",
            fragmentShaderString ?: ""
        )

        teapot?.init(shader)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        if (enable) {
            drawObjects()
            frameCount++
            val currentTime = System.currentTimeMillis()
            val elapsedTime = (currentTime - startTime) / 1000.0
            val intervalElapsedTime = (currentTime - intervalStartTime) / 1000.0

            if (intervalElapsedTime >= 5) {
                frameCounts.add(frameCount.toInt())
                frameCount = 0L
                intervalStartTime = currentTime
            }

            if (elapsedTime >= BENCHMARK_DURATION) {
                enable = false
            }
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat() / height.toFloat()
    }

    fun getElapsedTime(): Long {
        return (System.currentTimeMillis() - startTime) / 1000
    }

    fun startBenchmark(){
        if (loaded) {
            startTime = System.currentTimeMillis()
            intervalStartTime = startTime
            frameCount = 0L
            frameCounts.clear()
            enable = true
            rotationAngle = 0f
        }
    }


    fun getResources(context: Context) {
        vertexShaderString = loadShaderCode(context, "shaders/vertex_shader.glsl")
        fragmentShaderString = loadShaderCode(context, "shaders/fragment_shader.glsl")

        teapot = Model3D(context, "obj/teapot.obj")
        loaded = true
    }

    private fun drawObjects(){
        initUniforms()

        // Draw multiple teapots
        for (i in 0..63) {
            Matrix.setIdentityM(modelMatrix, 0)
            Matrix.scaleM(modelMatrix, 0, 4f, 4f, 4f)
            Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.25f, 1f, 0.5f)
            Matrix.translateM(modelMatrix, 0, (i % 4 - 1.5f) * 2f, ((i / 4) % 4 - 1.5f) * 2f, (i / 16 - 1.5f) * 2f)
            setUniforms()
            teapot?.draw(shader)
        }

        rotationAngle += 2f
        rotationAngle %= 360f
    }

    private fun initUniforms() {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.setIdentityM(normalMatrix, 0)

        Matrix.scaleM(modelMatrix, 0, 4f, 4f, 4f)
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.25f, 1f, 0.5f)


        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 150f)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -75f, 0f, 0f, 0f, 0f, 1f, 0f)
        calculateNormalMatrix()

        shader.setUniformMatrix("uModelMatrix", modelMatrix)
        shader.setUniformMatrix("uViewMatrix", viewMatrix)
        shader.setUniformMatrix("uProjectionMatrix", projectionMatrix)
        shader.setUniformMatrix("uNormalMatrix", normalMatrix)

        val color = floatArrayOf(234.0f / 255.0f, 55.0f / 255.0f, 15.0f /255.0f, 1.0f)
        shader.setUniform4f("uObjectColor", color[0], color[1], color[2], color[3])

        val lightPositions = arrayOf(
            floatArrayOf(10.0f, 10.0f, 10.0f),
            floatArrayOf(-10.0f, 10.0f, 10.0f),
            floatArrayOf(10.0f, -10.0f, 10.0f),
            floatArrayOf(-10.0f, -10.0f, 10.0f),
            floatArrayOf(10.0f, 10.0f, -10.0f),
            floatArrayOf(-10.0f, 10.0f, -10.0f),
            floatArrayOf(10.0f, -10.0f, -10.0f),
            floatArrayOf(-10.0f, -10.0f, -10.0f),
            floatArrayOf(0.0f, 10.0f, 0.0f),
            floatArrayOf(0.0f, -10.0f, 0.0f)
        )

        val lightColors = arrayOf(
            floatArrayOf(1.0f, 0.0f, 0.0f), // Red light
            floatArrayOf(0.0f, 1.0f, 0.0f), // Green light
            floatArrayOf(1.0f, 1.0f, 0.0f), // Yellow light
            floatArrayOf(0.0f, 0.0f, 1.0f), // Blue light
            floatArrayOf(1.0f, 0.0f, 1.0f), // Magenta light
            floatArrayOf(0.0f, 1.0f, 1.0f), // Cyan light
            floatArrayOf(1.0f, 0.5f, 0.0f), // Orange light
            floatArrayOf(0.5f, 0.0f, 1.0f), // Purple light
            floatArrayOf(0.5f, 0.5f, 0.5f), // Gray light
            floatArrayOf(1.0f, 1.0f, 1.0f)  // White light
        )
        for (i in lightPositions.indices) {
            shader.setUniform3f("uLightPos[$i]", lightPositions[i][0], lightPositions[i][1], lightPositions[i][2])
            shader.setUniform3f("uLightColor[$i]", lightColors[i][0], lightColors[i][1], lightColors[i][2])
        }

    }

    private fun setUniforms(){
        calculateNormalMatrix()
        shader.setUniformMatrix("uModelMatrix", modelMatrix)
        shader.setUniformMatrix("uViewMatrix", viewMatrix)
        shader.setUniformMatrix("uProjectionMatrix", projectionMatrix)
        shader.setUniformMatrix("uNormalMatrix", normalMatrix)
    }

    private fun calculateNormalMatrix() {
        val modelViewMatrix = FloatArray(16)
        val invModelViewMatrix = FloatArray(16)

        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)

        Matrix.invertM(invModelViewMatrix, 0, modelViewMatrix, 0)

        Matrix.transposeM(normalMatrix, 0, invModelViewMatrix, 0)

    }

    private fun loadShaderCode(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}