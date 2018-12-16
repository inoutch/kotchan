package io.github.inoutch.kotchan.core.view.map

import io.github.inoutch.kotchan.utility.graphic.GLAttribLocation
import io.github.inoutch.kotchan.utility.graphic.GLVBO
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.logger.logger
import io.github.inoutch.kotchan.utility.type.*
import io.github.inoutch.kotchan.core.view.drawable.Square
import kotlin.math.abs

class ChunkTileLayer(
        private val chunkTileMapInfo: ChunkTileMapInfo,
        private val mapIdGetter: (x: Int, y: Int) -> Int) : TileLayerBase(chunkTileMapInfo) {
    private val gl = KotchanCore.instance.gl
    private val bufferPointMap: MutableMap<Int, MutableMap<Int, Int>> = mutableMapOf()
    private val positionsVbo: GLVBO
    private val texcoordsVbo: GLVBO
    private val size: Int

    private var changes: MutableMap<Int, MutableMap<Int, Int>>? = null

    init {
        val pBuffer: MutableList<Vector3> = mutableListOf()
        val tBuffer: MutableList<Vector2> = mutableListOf()

        val chunkSize = chunkTileMapInfo.chunkSize
        var offset = 0
        for (sy in 0 until chunkTileMapInfo.chunkNumber.y) {
            for (sx in 0 until chunkTileMapInfo.chunkNumber.x) {
                setBufferPoint(sx, sy, offset++)
                val ret = createChunkVertices(sx * chunkSize.x, sy * chunkSize.y)
                pBuffer.addAll(ret.first)
                tBuffer.addAll(ret.second)
            }
        }
        size = pBuffer.size
        positionsVbo = gl.createVBO(pBuffer.flatten())
        texcoordsVbo = gl.createVBO(tBuffer.flatten())
    }

    private fun createChunkVertices(sx: Int, sy: Int): Pair<List<Vector3>, List<Vector2>> {
        val pBuffer: MutableList<Vector3> = mutableListOf()
        val tBuffer: MutableList<Vector2> = mutableListOf()
        for (nx in 0 until chunkTileMapInfo.chunkSize.y) {
            for (ny in 0 until chunkTileMapInfo.chunkSize.x) {
                val x = sx + nx
                val y = sy + ny

                val id = mapIdGetter(x, y)
                val p = Vector2(x.toFloat(), y.toFloat()) * chunkTileMapInfo.tileSize
                pBuffer.addAll(Square.createSquarePositions(p, chunkTileMapInfo.tileSize.toVector2()))
                tBuffer.addAll(calcTexcoords(id))
            }
        }
        return Pair(pBuffer, tBuffer)
    }

    override fun mapId(point: Point, id: Int) {
        val map = changes ?: mutableMapOf()
        val set = map[point.y] ?: mutableMapOf()
        set[point.x] = id
        map[point.y] = set

        changes = map
    }

    override fun mapId(point: Point): Int? = mapIdGetter(point.x, point.y)

    override fun fillAll(id: Int) {}

    override fun draw(delta: Float) {
        updateMapIds()
        gl.bindVBO(positionsVbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_POSITION, 3, 0, 0)
        gl.bindVBO(texcoordsVbo.id)
        gl.vertexPointer(GLAttribLocation.ATTRIBUTE_TEXCOORD, 2, 0, 0)
        gl.drawTriangleArrays(0, size)
    }

    override fun destroy() {
        positionsVbo.destroy()
        texcoordsVbo.destroy()
    }

    fun updateVertices(sx: Int, sy: Int, vx: Int, vy: Int) {
        val cx = chunkTileMapInfo.chunkNumber.x
        val cy = chunkTileMapInfo.chunkNumber.y
        if (abs(vx) > cx || abs(vy) > cy) {
            shiftChunk(sx, sy, vx, vy, cx, cy)
            return
        }
        when {
            vx > 0 -> when {
                vy > 0 -> {
                    shiftChunk(sx, sy, cx, cy, vx, vy)// lb to rt
                    shiftChunk(sx, sy + vy, cx, 0, vx, cy - vy)// shift x
                    shiftChunk(sx + vx, sy, 0, cy, cx - vx, vy)// shift y
                }
                vy < 0 -> {
                    shiftChunk(sx, sy + cy + vy, cx, -cy, vx, -vy)// lt to rb
                    shiftChunk(sx, sy, cx, 0, vx, cy + vy)// shift x
                    shiftChunk(sx + vx, sy + cy + vy, 0, -cy, cx - vx, -vy)// shift y
                }
                else -> shiftChunk(sx, sy, cx, 0, vx, cy)// shift x
            }
            vx < 0 -> when {
                vy > 0 -> {
                    shiftChunk(sx + cx + vx, sy, -cx, cy, -vx, vy)// rb to lt
                    shiftChunk(sx + cx + vx, sy + vy, -cx, 0, -vx, cy - vy)// shift x
                    shiftChunk(sx, sy, 0, cy, cx + vx, vy)// shift y
                }
                vy < 0 -> {
                    shiftChunk(sx + cx + vx, sy + cy + vy, -cx, -cy, -vx, -vy)// rt to lb
                    shiftChunk(sx + cx + vx, sy, -cx, 0, -vx, cy + vy)// shift x
                    shiftChunk(sx, sy + cy + vy, 0, -cy, cx + vx, -vy)// shift y
                }
                else -> shiftChunk(sx + cx + vx, sy, -cx, 0, -vx, cy)// shift x
            }
            else -> {
                if (vy > 0) {
                    shiftChunk(sx, sy, 0, cy, cx, vy)// shift y
                } else if (vy < 0) {
                    shiftChunk(sx, sy + cy + vy, 0, -cy, cx, -vy)// shift y
                }
            }
        }
    }

    private fun updateTexcoords(x: Int, y: Int, list: List<Vector2>) {
        val center = Vector2(x.toFloat(), y.toFloat()) * mapInfo.tileSize
        val pointInChunk = ChunkTileMap.calcMapPosition(center, chunkTileMapInfo)
        val chunkOffset = getChunkOffset(pointInChunk.x, pointInChunk.y) ?: return
        val cx = chunkTileMapInfo.chunkSize.x
        val cy = chunkTileMapInfo.chunkSize.y
        val tileOffset = calcOffset(x - pointInChunk.x * cx, y - pointInChunk.y * cy, 2 * 6)
        gl.updateVBO(texcoordsVbo, chunkOffset * 2 * 6 + tileOffset, list.flatten())
    }

    private fun getChunkOffset(x: Int, y: Int): Int? {
        return bufferPointMap[y]?.get(x)
    }

    private fun shiftChunk(sx: Int, sy: Int, vx: Int, vy: Int, w: Int, h: Int) {
        for (y in 0 until h) {
            for (x in 0 until w) {
                val offset = replaceBufferPoint(sx + x, sy + y, sx + x + vx, sy + y + vy)
                if (offset != null) {
                    updateChunk(sx + x + vx, sy + y + vy, offset)
                }
            }
        }
    }

    private fun updateChunk(sx: Int, sy: Int, offset: Int) {
        val positions = mutableListOf<Vector3>()
        val texcoords = mutableListOf<Vector2>()
        val chunkCount = chunkTileMapInfo.chunkSize.x * chunkTileMapInfo.chunkSize.y
        val baseTile = Point(sx * chunkTileMapInfo.chunkSize.x, sy * chunkTileMapInfo.chunkSize.y)
        val base = (baseTile * chunkTileMapInfo.tileSize).toVector2()
        for (y in 0 until chunkTileMapInfo.chunkSize.y) {
            for (x in 0 until chunkTileMapInfo.chunkSize.x) {
                val p = Vector2(x.toFloat(), y.toFloat()) * chunkTileMapInfo.tileSize
                positions.addAll(Square.createSquarePositions(base + p, chunkTileMapInfo.tileSize.toVector2()))
                texcoords.addAll(calcTexcoords(mapIdGetter(baseTile.x + x, baseTile.y + y)))
            }
        }
        gl.updateVBO(positionsVbo, offset * 3 * 6 * chunkCount, positions.flatten())
        gl.updateVBO(texcoordsVbo, offset * 2 * 6 * chunkCount, texcoords.flatten())
    }

    private fun updateMapIds() {
        changes?.forEach { rows ->
            val y = rows.key
            rows.value.forEach { cols ->
                val x = cols.key
                val v = cols.value
                updateTexcoords(x, y, calcTexcoords(v))
            }
        }
        changes = null
    }

    private fun setBufferPoint(x: Int, y: Int, v: Int) {
        bufferPointMap.getOrPut(y, { mutableMapOf() })[x] = v
    }

    private fun replaceBufferPoint(bx: Int, by: Int, ax: Int, ay: Int): Int? {
        val index = bufferPointMap[by]?.get(bx)
        if (index == null || bufferPointMap[ay]?.get(ax) != null) {
            logger.error("chunk layer is invalid[bx=$bx by=$by ax=$ax ay=$ay].")
            return null
        }
        bufferPointMap[by]?.let {
            it.remove(bx)
            if (it.isEmpty()) {
                bufferPointMap.remove(by)
            }
        }

        setBufferPoint(ax, ay, index)
        return index
    }
}