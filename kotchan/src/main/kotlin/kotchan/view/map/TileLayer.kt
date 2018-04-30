package kotchan.view.map

import interop.graphic.GLAttribLocation
import interop.graphic.GLVBO
import kotchan.Engine
import kotchan.view.drawable.Square
import utility.type.Vector2
import utility.type.Vector3
import utility.type.flatten
import kotlin.math.floor

class TileLayer(private val mapInfo: TileMapInfo, private val tileLayerInfo: TileLayerInfo) {
    var mapId: MutableList<MutableList<Int>> = List(mapInfo.mapSize.y.toInt(), {
        List(mapInfo.mapSize.x.toInt(), { 0 })
    }).mapIndexed { y, list ->
        list.mapIndexed { x, _ -> tileLayerInfo.mapId(x, y) }.toMutableList()
    }.toMutableList()
        private set

    var visible = tileLayerInfo.visible

    private val gl = Engine.getInstance().gl
    private val positionsVbo: GLVBO
    private val texcoordsVbo: GLVBO
    private val size: Int
    private var changes: MutableMap<Int, HashSet<Int>>? = null
    private val bias = Vector2(1.0f, 1.0f) / mapInfo.tileNumber * 0.01f

    init {
        val pBuffer: MutableList<Vector3> = mutableListOf()
        val tBuffer: MutableList<Vector2> = mutableListOf()
        mapId.forEachIndexed { y, cols ->
            cols.forEachIndexed { x, id ->
                val p = Vector2(x.toFloat(), y.toFloat()) * mapInfo.tileSize
                val positions = Square.createSquarePositions(p, mapInfo.tileSize)
                val texcoords: List<Vector2> = calcTexcoords(id)
                pBuffer.addAll(positions)
                tBuffer.addAll(texcoords)
            }
        }
        size = pBuffer.size
        positionsVbo = gl.createVBO(pBuffer.flatten())
        texcoordsVbo = gl.createVBO(tBuffer.flatten())
    }

    private fun calcTexcoord(id: Int): Vector2 {
        val u = (id % mapInfo.tileNumber.x.toInt()).toFloat()
        val v = floor(id / mapInfo.tileNumber.x)
        return Vector2(u, v) * mapInfo.tileTexcoordSize
    }

    private fun calcTexcoords(id: Int): List<Vector2> {
        return Square.createSquareTexcoords(calcTexcoord(id) + bias, mapInfo.tileTexcoordSize - bias * 2.0f)
    }

    private fun calcOffset(x: Int, y: Int, stride: Int): Int {
        return (y * mapInfo.mapSize.x.toInt() + x) * stride
    }

    private fun updateTexcoords(x: Int, y: Int, list: List<Vector2>) {
        // square vertex number * vec2 = 6 * 2
        val offset = calcOffset(x, y, 6 * 2)
        gl.updateVBO(texcoordsVbo, offset, list.flatten())
    }

    private fun updateMapIds() {
        val map = changes?.toMap() ?: return
        map.forEach {
            // sorted hashset (like 1,2,3,5,6,7,11,12...)
            val list = it.value.sorted()
            val y = it.key
            // using by grouping ex. 1,2,3,5,6,7,11,12 -> (1,2,3), (5,6,7), (11,12)
            var begin = list.first()
            var end = begin
            // texcoord exists at least 1
            val texcoord = calcTexcoords(mapId[y][begin])
            val texcoords = texcoord.toMutableList()
            for (i in 1 until list.size) {
                val x = list[i]
                // next number or not
                if (x == end + 1) {
                    end++
                } else {
                    updateTexcoords(begin, y, texcoords)
                    texcoords.clear()
                    end = x
                    begin = x
                }
                texcoords.addAll(calcTexcoords(mapId[y][x]))
            }
            updateTexcoords(begin, y, texcoords)
        }
        changes?.clear()
        changes = null
    }

    fun mapId(x: Int, y: Int): Int? {
        if (y < 0 || mapInfo.mapSize.y <= y || x < 0 || mapInfo.mapSize.x <= x) {
            return null
        }
        return mapId[y][x]
    }

    fun mapId(x: Int, y: Int, id: Int) {
        if (y < 0 || mapInfo.mapSize.y <= y || x < 0 || mapInfo.mapSize.x <= x) {
            return
        }
        val map = changes ?: mutableMapOf()
        val set = map[y] ?: hashSetOf()
        set.add(x)
        map[y] = set

        changes = map
        mapId[y][x] = id
    }

    fun fillAll(id: Int) {
        val (mx, my) = mapInfo.mapSize.x.toInt() to mapInfo.mapSize.y.toInt()
        for (y in 0 until my) {
            for (x in 0 until mx) {
                mapId(x, y, id)
            }
        }
    }

    fun draw() {
        updateMapIds()
        gl.bindVBO(positionsVbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
        gl.bindVBO(texcoordsVbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
        gl.drawTriangleArrays(0, size)
    }

    fun destroy() {
        positionsVbo.destroy()
        texcoordsVbo.destroy()
    }
}