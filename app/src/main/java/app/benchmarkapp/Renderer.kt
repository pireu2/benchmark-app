package app.benchmarkapp

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


object Renderer : GLSurfaceView.Renderer {

    var vendor: String = "Unknown"
    var model: String = "Unknown"
    var version: String = "Unknown"
    var shadingLanguageVersion: String = "Unknown"
    var fps = 0.0

    private var vertexShaderString : String? = null
    private var fragmentShaderString : String? = null


    private var frameCount = 0
    private var startTime = 0L
    private var program = 0
    private var vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(0).asFloatBuffer()

    private val triangleCoords = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)



    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        vendor = GLES20.glGetString(GLES20.GL_VENDOR)
        model = GLES20.glGetString(GLES20.GL_RENDERER)
        version = GLES20.glGetString(GLES20.GL_VERSION)
        shadingLanguageVersion = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION) ?: "Unknown"

        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f)
        startTime = System.currentTimeMillis()
        initializeShaders()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        GLES20.glUseProgram(program)

        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer)

        val colorHandle = GLES20.glGetUniformLocation(program, "vColor")
        GLES20.glUniform4fv(colorHandle, 1, color, 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)

        GLES20.glDisableVertexAttribArray(positionHandle)

        frameCount++
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - startTime
        if (elapsedTime >= 1000) {
            fps = frameCount * 1000.0 / elapsedTime
            frameCount = 0
            startTime = currentTime
        }
    }
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    fun getResources(context: Context) {
        vertexShaderString = loadShaderCode(context, R.raw.vertex_shader)
        fragmentShaderString = loadShaderCode(context, R.raw.fragment_shader)
    }

    private fun initializeShaders() {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderString?: "")
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderString?: "")

        program = GLES20.glCreateProgram().apply {
            GLES20.glAttachShader(this, vertexShader)
            GLES20.glAttachShader(this, fragmentShader)
            GLES20.glLinkProgram(this)
        }

        val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer().apply {
            put(triangleCoords)
            position(0)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    private fun loadShaderCode(context: Context, resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use{it.readText()}
    }
}