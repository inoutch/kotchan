package kotchan.view.map

import interop.graphic.GLAttribLocation
import interop.graphic.GLVBO
import kotchan.Engine
import kotchan.logger.logger
import kotchan.view.drawable.Square
import utility.type.Vector2
import utility.type.Vector3
import utility.type.flatten
import kotlin.math.abs

class ChunkTileLayer(
        private val chunkTileMapInfo: ChunkTileMapInfo,
        chunkTileLayerInfo: ChunkTileLayerInfo,
        private val mapIdGetter: (x: Int, y: Int) -> Int) : TileLayerBase(chunkTileMapInfo) {
    private val gl = Engine.getInstance().gl
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
        for (sy in 0 until chunkTileMapInfo.chunkNumber.y.toInt()) {
            for (sx in 0 until chunkTileMapInfo.chunkNumber.x.toInt()) {
                setBufferPoint(sx, sy, offset++)
                val ret = createChunkVertices(sx * chunkSize.x.toInt(), sy * chunkSize.y.toInt())
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
        for (nx in 0 until chunkTileMapInfo.chunkSize.y.toInt()) {
            for (ny in 0 until chunkTileMapInfo.chunkSize.x.toInt()) {
                val x = sx + nx
                val y = sy + ny

                val id = mapIdGetter(x, y)
                val p = Vector2(x.toFloat(), y.toFloat()) * chunkTileMapInfo.tileSize
                pBuffer.addAll(Square.createSquarePositions(p, chunkTileMapInfo.tileSize))
                tBuffer.addAll(calcTexcoords(id))
            }
        }
        return Pair(pBuffer, tBuffer)
    }

    override fun mapId(x: Int, y: Int, mapId: Int) {
        val map = changes ?: mutableMapOf()
        val set = map[y] ?: mutableMapOf()
        set[x] = mapId
        map[y] = set

        changes = map
    }

    override fun mapId(x: Int, y: Int): Int? = mapIdGetter(x, y)

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
        val (cx, cy) = chunkTileMapInfo.chunkNumber.x.toInt() to chunkTileMapInfo.chunkNumber.y.toInt()
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
        val chunkOffset = getChunkOffset(pointInChunk.first, pointInChunk.second) ?: return
        val (cx, cy) = chunkTileMapInfo.chunkSize.x.toInt() to chunkTileMapInfo.chunkSize.y.toInt()
        val tileOffset = calcOffset(x - pointInChunk.first * cx, y - pointInChunk.second * cy, 2 * 6)
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
        val baseTileX = sx * chunkTileMapInfo.chunkSize.x
        val baseTileY = sy * chunkTileMapInfo.chunkSize.y
        val base = Vector2(baseTileX, baseTileY) * chunkTileMapInfo.tileSize
        for (y in 0 until chunkTileMapInfo.chunkSize.y.toInt()) {
            for (x in 0 until chunkTileMapInfo.chunkSize.x.toInt()) {
                val p = Vector2(x.toFloat(), y.toFloat()) * chunkTileMapInfo.tileSize
                positions.addAll(Square.createSquarePositions(base + p, chunkTileMapInfo.tileSize))
                texcoords.addAll(calcTexcoords(mapIdGetter(baseTileX.toInt() + x, baseTileY.toInt() + y)))
            }
        }
        gl.updateVBO(positionsVbo, offset * 3 * 6 * chunkCount.toInt(), positions.flatten())
        gl.updateVBO(texcoordsVbo, offset * 2 * 6 * chunkCount.toInt(), texcoords.flatten())
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