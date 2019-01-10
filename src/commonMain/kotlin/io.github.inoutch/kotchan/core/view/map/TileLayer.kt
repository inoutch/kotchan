package io.github.inoutch.kotchan.core.view.map

import io.github.inoutch.kotchan.utility.graphic.GLAttribLocation
import io.github.inoutch.kotchan.utility.graphic.GLVBO
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.*
import io.github.inoutch.kotchan.core.view.drawable.Square

class TileLayer(
        mapInfo: TileMapInfo,
        private val tileLayerInfo: TileLayerInfo) : TileLayerBase(mapInfo) {

    var mapId: MutableList<MutableList<Int>> = List(mapInfo.mapSize.y) { List(mapInfo.mapSize.x) { 0 } }
            .mapIndexed { y, list -> list.mapIndexed { x, _ -> tileLayerInfo.mapId(x, y) }.toMutableList() }
            .toMutableList()
        private set

    var visible = tileLayerInfo.visible

    private val gl = KotchanCore.instance.gl

    private val positionsVbo: GLVBO

    private val texcoordsVbo: GLVBO

    private val size: Int

    private var changes: MutableMap<Int, HashSet<Int>>? = null

    init {
        val pBuffer: MutableList<Vector3> = mutableListOf()
        val tBuffer: MutableList<Vector2> = mutableListOf()
        mapId.forEachIndexed { y, cols ->
            cols.forEachIndexed { x, id ->
                val positions = Square.createSquarePositions(
                        Vector2(x, y) * mapInfo.tileSize,
                        mapInfo.tileSize.toVector2())
                val texcoords: List<Vector2> = calcTexcoords(id)
                pBuffer.addAll(positions)
                tBuffer.addAll(texcoords)
            }
        }
        size = pBuffer.size
        positionsVbo = gl.createVBO(pBuffer.flatten())
        texcoordsVbo = gl.createVBO(tBuffer.flatten())
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

    override fun mapId(point: Point): Int? {
        if (point.y < 0 || mapInfo.mapSize.y <= point.y || point.x < 0 || mapInfo.mapSize.x <= point.x) {
            return null
        }
        return mapId[point.y][point.x]
    }

    override fun mapId(point: Point, id: Int) {
        if (point.y < 0 || mapInfo.mapSize.y <= point.y || point.x < 0 || mapInfo.mapSize.x <= point.x) {
            return
        }
        val map = changes ?: mutableMapOf()
        val set = map[point.y] ?: hashSetOf()
        set.add(point.x)
        map[point.y] = set

        changes = map
        this.mapId[point.y][point.x] = id
    }

    override fun fillAll(id: Int) {
        for (y in 0 until mapInfo.mapSize.y) {
            for (x in 0 until mapInfo.mapSize.x) {
                mapId(Point(x, y), id)
            }
        }
    }

    override fun draw(delta: Float) {
        if (!visible) {
            return
        }
        updateMapIds()
        gl.bindVBO(positionsVbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
        gl.bindVBO(texcoordsVbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
        gl.drawTriangleArrays(0, size)
    }

    override fun destroy() {
        super.destroy()
        positionsVbo.destroy()
        texcoordsVbo.destroy()
    }
}