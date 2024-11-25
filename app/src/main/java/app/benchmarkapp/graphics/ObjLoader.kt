package app.benchmarkapp.graphics

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class ObjLoader(context: Context, fileName: String) {

    private val vertices: FloatArray
    data class Position(val x: Float, val y: Float, val z: Float)
    data class Normal(val x: Float, val y: Float, val z: Float)
    data class TexCoord(val u: Float, val v: Float)

    data class VertexData(val position: Position, val normal: Normal, val texCoord: TexCoord)

    init {
        val vertexList = mutableListOf<Position>()
        val normalList = mutableListOf<Normal>()
        val texCoordList = mutableListOf<TexCoord>()
        val vertexDataList = mutableListOf<VertexData>()
        val finalVertexData = mutableListOf<Float>()

        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(" ")
                when (parts[0]) {
                    "v" -> {
                        val vertex = Position(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat())
                        vertexList.add(vertex)
                    }
                    "vn" -> {
                        val normal = Normal(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat())
                        normalList.add(normal)
                    }
                    "vt" -> {
                        val texCoord = TexCoord(parts[1].toFloat(), parts[2].toFloat())
                        texCoordList.add(texCoord)
                    }
                    "f" -> {
                        parts.drop(1).forEach { part ->
                            val indices = part.split("/")

                            val vertexIndex = indices[0].toInt() - 1
                            val texCoordIndex = if (indices[1].isNotEmpty()) indices[1].toInt() - 1 else -1
                            val normalIndex = if (indices[2].isNotEmpty()) indices[2].toInt() - 1 else -1

                            val vertexData = VertexData(
                                position = vertexList[vertexIndex],
                                normal = if (normalIndex != -1) normalList[normalIndex] else Normal(0.0f, 0.0f, 0.0f),
                                texCoord = if (texCoordIndex != -1) texCoordList[texCoordIndex] else TexCoord(0.0f, 0.0f)
                            )
                            vertexDataList.add(vertexData)
                        }
                    }
                }
            }
        }

        vertexDataList.forEach { vertexData ->
            finalVertexData.add(vertexData.position.x)
            finalVertexData.add(vertexData.position.y)
            finalVertexData.add(vertexData.position.z)

            finalVertexData.add(vertexData.normal.x)
            finalVertexData.add(vertexData.normal.y)
            finalVertexData.add(vertexData.normal.z)

            finalVertexData.add(vertexData.texCoord.u)
            finalVertexData.add(vertexData.texCoord.v)
        }

        vertices = finalVertexData.toFloatArray()
    }

    fun getVertices(): FloatArray {
        return vertices
    }

    fun getVerticesSize(): Int {
        return vertices.size
    }
}