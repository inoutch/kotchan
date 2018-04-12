package kotchan.scene.map

import interop.graphic.GLAttribLocation
import interop.graphic.GLVBO
import kotchan.Engine
import kotchan.scene.drawable.Square
import utility.type.Vector2
import utility.type.Vector3
import utility.type.flatten
import kotlin.math.round

class TileLayer(private val mapInfo: TileMapInfo, private val tileLayerInfo: TileLayerInfo) {
    var mapId: MutableList<MutableList<Int>> = tileLayerInfo.mapId.map { it.toMutableList() }.toMutableList()
        private set

    private val positionsVbo: GLVBO
    private val texcoordsVbo: GLVBO
    private val size: Int
    private val gl = Engine.getInstance().gl

    init {
        val pBuffer: MutableList<Vector3> = mutableListOf()
        val tBuffer: MutableList<Vector2> = mutableListOf()
        mapId.forEachIndexed { y, cols ->
            cols.forEachIndexed { x, id ->
                val pi = Vector2((id % mapInfo.tileNumber.x.toInt()).toFloat(),
                        round(id / mapInfo.tileNumber.x))

                val p1 = Vector2(x.toFloat(), y.toFloat()) * mapInfo.tileSize
                val p2 = pi * mapInfo.tileTexcoordSize
                val positions = Square.createSquarePositions(p1, mapInfo.tileSize)
                val texcoords: List<Vector2> = Square.createSquareTexcoords(p2, mapInfo.tileTexcoordSize)
                pBuffer.addAll(positions)
                tBuffer.addAll(texcoords)
            }
        }
        size = pBuffer.size
        positionsVbo = gl.createVBO(pBuffer.flatten())
        texcoordsVbo = gl.createVBO(tBuffer.flatten())
    }

    fun draw() {
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0, positionsVbo)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0, texcoordsVbo)
        gl.drawTriangleArrays(0, size)
    }

    fun destroy() {
        positionsVbo.destroy()
        texcoordsVbo.destroy()
    }
}