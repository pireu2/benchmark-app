package app.benchmarkapp.graphics

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ObjLoader(context: Context, fileName: String) {

    val vertices: FloatBuffer
    val normals: FloatBuffer
    val indices: IntBuffer

    init {
        val vertexList = mutableListOf<Float>()
        val normalList = mutableListOf<Float>()
        val indexList = mutableListOf<Int>()

        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(" ")
                when (parts[0]) {
                    "v" -> {
                        // Vertex coordinates
                        vertexList.add(parts[1].toFloat())
                        vertexList.add(parts[2].toFloat())
                        vertexList.add(parts[3].toFloat())
                    }
                    "vn" -> {
                        // Normal coordinates
                        normalList.add(parts[1].toFloat())
                        normalList.add(parts[2].toFloat())
                        normalList.add(parts[3].toFloat())
                    }
                    "f" -> {
                        // Face indices
                        parts.drop(1).forEach { part ->
                            val indices = part.split("//")
                            indexList.add(indices[0].toInt() - 1)
                        }
                    }
                }
            }
        }

        vertices = ByteBuffer.allocateDirect(vertexList.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexList.toFloatArray())
            .apply { position(0) }

        normals = ByteBuffer.allocateDirect(normalList.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(normalList.toFloatArray())
            .apply { position(0) }

        indices = ByteBuffer.allocateDirect(indexList.size * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
            .put(indexList.toIntArray())
            .apply { position(0) }
    }
}