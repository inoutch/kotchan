package kotchan.scene.map

import kotchan.Engine
import kotchan.scene.drawable.Square
import utility.type.Vector2
import utility.type.Vector3
import utility.type.flatten

class TileLayer(private val tileLayerInfo: TileLayerInfo) {
    var width: Int = 0
        private set

    var height: Int = 0
        private set

    var mapId: MutableList<MutableList<Int>> = tileLayerInfo.mapId.map { it.toMutableList() }.toMutableList()
        private set

    fun bind() {
        val list: MutableList<Vector3> = mutableListOf()
        mapId.forEachIndexed { y, cols ->
            cols.forEachIndexed { x, _ ->
                val position = Vector2(x.toFloat(), y.toFloat()) * tileLayerInfo.mapInfo.tileSize
                val vertices = Square.createSquarePositions(position, tileLayerInfo.mapInfo.tileSize)
                list.addAll(vertices)
            }
        }
        val vbo = Engine.getInstance().gl.createVBO(list.flatten())
    }
}