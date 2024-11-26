package app.benchmarkapp.graphics

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class Model3D(context: Context, fileName: String) {

    private var mesh: Mesh

    fun draw(shader: Shader) {
        mesh.draw(shader)
    }

    fun init(shader: Shader) {
        mesh.init(shader)
    }

    init {
        val positionsList = mutableListOf<Position>()
        val normalList = mutableListOf<Normal>()
        val texCoordList = mutableListOf<TexCoord>()
        val vertexList = mutableListOf<Vertex>()

        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(" ")
                when (parts[0]) {
                    "v" -> {
                        val vertex = Position(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat())
                        positionsList.add(vertex)
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

                            val vertexData = Vertex(
                                position = positionsList[vertexIndex],
                                normal = if (normalIndex != -1) normalList[normalIndex] else Normal(0.0f, 0.0f, 0.0f),
                                texCoord = if (texCoordIndex != -1) texCoordList[texCoordIndex] else TexCoord(0.0f, 0.0f)
                            )
                            vertexList.add(vertexData)
                        }
                    }
                }
            }
        }

        mesh = Mesh(vertexList)
    }
}