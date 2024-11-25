package app.benchmarkapp.graphics

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

object Renderer : GLSurfaceView.Renderer {

    var vendor: String = "Unknown"
    var model: String = "Unknown"
    var version: String = "Unknown"
    var shadingLanguageVersion: String = "Unknown"
    var fps = 0.0

    private var ratio : Float = 0.0f

    private var vertexShaderString: String? = null
    private var fragmentShaderString: String? = null

    private lateinit var shader: Shader
    private lateinit var objLoader: ObjLoader

    private var frameCount = 0
    private var startTime = 0L

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val normalMatrix = FloatArray(16)

    private lateinit var vertexBuffer: Buffer

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
        val positionHandle = shader.getAttribLocation("vPosition")
        val normalHandle = shader.getAttribLocation("vNormal")
        val texCoordHandle = shader.getAttribLocation("vTexCoord")


        vertexBuffer = ByteBuffer.allocateDirect(objLoader.getVerticesSize() * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(objLoader.getVertices())
            .position(0)


        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 32, vertexBuffer)

        GLES20.glEnableVertexAttribArray(normalHandle)
        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 32, vertexBuffer.position(3))

        GLES20.glEnableVertexAttribArray(texCoordHandle)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 32, vertexBuffer.position(6))


    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        shader.use()
        initUniforms()

        // Draw the first teapot
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 4f, 4f, 4f)
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.25f, 1f, 0.5f)
        Matrix.translateM(modelMatrix, 0, -2f, 0f, 0f) // Position the first teapot
        setUniforms()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, objLoader.getVerticesSize() / 8)

        // Draw the second teapot
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 4f, 4f, 4f)
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.25f, 1f, 0.5f)
        Matrix.translateM(modelMatrix, 0, 2f, 0f, 0f) // Position the second teapot
        setUniforms()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, objLoader.getVerticesSize() / 8)

        // Draw the third teapot
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 4f, 4f, 4f)
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.25f, 1f, 0.5f)
        Matrix.translateM(modelMatrix, 0, 0f, 2f, 0f) // Position the third teapot
        setUniforms()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, objLoader.getVerticesSize() / 8)

        // Draw the fourth teapot
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 4f, 4f, 4f)
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.25f, 1f, 0.5f)
        Matrix.translateM(modelMatrix, 0, 0f, -2f, 0f) // Position the fourth teapot
        setUniforms()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, objLoader.getVerticesSize() / 8)


        rotationAngle += 2f
        rotationAngle %= 360f

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

        ratio = width.toFloat() / height.toFloat()
    }

    fun getResources(context: Context) {
        vertexShaderString = loadShaderCode(context, "shaders/vertex_shader.glsl")
        fragmentShaderString = loadShaderCode(context, "shaders/fragment_shader.glsl")
        objLoader = ObjLoader(context, "obj/teapots/teapot50segU.obj")
        //objLoader = ObjLoader(context, "obj/teapot.obj")
    }

    private fun initUniforms() {

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.setIdentityM(normalMatrix, 0)

        Matrix.scaleM(modelMatrix, 0, 4f, 4f, 4f)
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.25f, 1f, 0.5f)


        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 150f)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -50f, 0f, 0f, 0f, 0f, 1f, 0f)
        calculateNormalMatrix(viewMatrix, modelMatrix, normalMatrix)

        shader.setUniformMatrix("uModelMatrix", modelMatrix)
        shader.setUniformMatrix("uViewMatrix", viewMatrix)
        shader.setUniformMatrix("uProjectionMatrix", projectionMatrix)
        shader.setUniformMatrix("uNormalMatrix", normalMatrix)

        val color = floatArrayOf(234.0f / 255.0f, 55.0f / 255.0f, 0f, 1.0f)
        val lightPos = floatArrayOf(1.0f, 1.0f, 0.0f)
        val lightColor = floatArrayOf(1.0f, 1.0f, 1.0f)

        shader.setUniform4f("uObjectColor", color[0], color[1], color[2], color[3])
        shader.setUniform3f("uLightColor", lightColor[0], lightColor[1], lightColor[2])
        shader.setUniform3f("uLightPos", lightPos[0], lightPos[1], lightPos[2])
    }

    private fun setUniforms(){
        calculateNormalMatrix(viewMatrix, modelMatrix, normalMatrix)
        shader.setUniformMatrix("uModelMatrix", modelMatrix)
        shader.setUniformMatrix("uViewMatrix", viewMatrix)
        shader.setUniformMatrix("uProjectionMatrix", projectionMatrix)
        shader.setUniformMatrix("uNormalMatrix", normalMatrix)
    }

    private fun calculateNormalMatrix(viewMatrix: FloatArray, modelMatrix: FloatArray, normalMatrix: FloatArray) {
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