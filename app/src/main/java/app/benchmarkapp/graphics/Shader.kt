package app.benchmarkapp.graphics

import android.opengl.GLES20

class Shader(vertexShaderCode: String, fragmentShaderCode: String) {
    private val program: Int

    init {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
    }

    fun use() {
        GLES20.glUseProgram(program)
    }

    fun getAttribLocation(name: String?): Int {
        return GLES20.glGetAttribLocation(program, name)
    }

    fun getUniformLocation(name: String?): Int {
        return GLES20.glGetUniformLocation(program, name)
    }

    fun setUniformMatrix(name: String, matrix: FloatArray) {
        val location = getUniformLocation(name)
        GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0)
    }


    fun setUniform3f(name: String, x: Float, y: Float, z: Float) {
        val location = getUniformLocation(name)
        GLES20.glUniform3f(location, x, y, z)
    }

    fun setUniform4f(name: String, x: Float, y: Float, z: Float, w: Float) {
        val location = getUniformLocation(name)
        GLES20.glUniform4f(location, x, y, z, w)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

        if (compileStatus[0] == 0) {
            val errorLog = GLES20.glGetShaderInfoLog(shader)
            GLES20.glDeleteShader(shader)
            val shaderType = if (type == GLES20.GL_VERTEX_SHADER) "vertex" else "fragment"
            throw RuntimeException("Error compiling $shaderType shader: $errorLog")
        }

        return shader
    }
}