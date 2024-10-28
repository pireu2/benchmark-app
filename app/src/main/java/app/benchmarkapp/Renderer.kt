package app.benchmarkapp

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

object Renderer : GLSurfaceView.Renderer {
    var vendor: String = "Unknown"
    var model: String = "Unknown"
    var version: String = "Unknown"
    var shadingLanguageVersion: String = "Unknown"


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        vendor = GLES20.glGetString(GLES20.GL_VENDOR)
        model = GLES20.glGetString(GLES20.GL_RENDERER)
        version = GLES20.glGetString(GLES20.GL_VERSION)
        shadingLanguageVersion = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION) ?: "Unknown"
    }

    override fun onDrawFrame(gl: GL10?) {}
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {}
}