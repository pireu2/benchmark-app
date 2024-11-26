package app.benchmarkapp.graphics

import android.opengl.GLES20
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class Position(
    val x: Float,
    val y: Float,
    val z: Float
)

data class Normal(
    val x: Float,
    val y: Float,
    val z: Float
)

data class TexCoord(
    val u: Float,
    val v: Float
)

data class Vertex(
    val position: Position,
    val normal: Normal,
    val texCoord: TexCoord
)


class Mesh(vertices: List<Vertex>) {

    private var vertexData: FloatArray
    private var vertexBuffer: Buffer

    init {
        val data = mutableListOf<Float>()

        vertices.forEach{ vertex ->
            data.add(vertex.position.x)
            data.add(vertex.position.y)
            data.add(vertex.position.z)

            data.add(vertex.normal.x)
            data.add(vertex.normal.y)
            data.add(vertex.normal.z)

            data.add(vertex.texCoord.u)
            data.add(vertex.texCoord.v)
        }

        vertexData = data.toFloatArray()

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
            .position(0)
    }

    fun init(shader: Shader){
        val positionHandle = shader.getAttribLocation("vPosition")
        val normalHandle = shader.getAttribLocation("vNormal")
        val texCoordHandle = shader.getAttribLocation("vTexCoord")

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(normalHandle)
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 8 * 4, vertexBuffer)
        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 8 * 4, vertexBuffer)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 8 * 4, vertexBuffer)
    }


    fun draw(shader: Shader) {
        shader.use()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexData.size / 8)
    }
}