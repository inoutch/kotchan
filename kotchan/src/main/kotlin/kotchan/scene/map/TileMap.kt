package kotchan.scene.map

import interop.graphic.GLCamera
import kotchan.controller.TouchType
import kotchan.controller.touchable.RectTouchable
import kotchan.controller.touchable.Touchable
import kotchan.scene.shader.NoColorsShaderProgram
import utility.type.Rect
import utility.type.Vector2
import utility.type.Vector3
import utility.type.Vector4
import kotlin.math.floor

class TileMap(private val mapInfo: TileMapInfo) {
    val size = mapInfo.mapSize * mapInfo.tileSize
    var position = Vector3()

    private val shaderProgram = NoColorsShaderProgram()
    private val layers: List<TileLayer> = mapInfo.layersInfo.map { TileLayer(mapInfo, it) }
    var touchable: Touchable? = null
        private set

    fun draw(delta: Float, camera: GLCamera, layerRange: IntRange = IntRange(0, layers.size - 1)) {
        mapInfo.texture.use()
        shaderProgram.use()
        shaderProgram.prepare(delta, camera)
        for (i in layerRange) {
            val l = layer(i) ?: continue
            if (l.visible) {
                l.draw()
            }
        }
    }

    fun layer(index: Int) = layers.getOrNull(index)

    fun destroy() {
        layers.forEach { it.destroy() }
        shaderProgram.destroy()
    }

    fun enableTouch(camera: GLCamera, callback: (point: Vector2, tilePoint: Vector2, type: TouchType) -> Unit): Touchable {
        val touchable = RectTouchable({ Rect(Vector2(position.x, position.y), size) }, camera) { _, point, type, check ->
            if (check) {
                val pointInView = camera.combine * Vector4(position, 1.0f)
                val sizeInView = camera.combine * Vector4(Vector3(mapInfo.tileSize, 0.0f), 1.0f)
                val pointInTileMap = (point - Vector2(pointInView.x, pointInView.y)) / Vector2(1.0f + sizeInView.x, 1.0f + sizeInView.y) * mapInfo.tileSize
                val tileX = floor(pointInTileMap.x / mapInfo.tileSize.x)
                val tileY = floor(pointInTileMap.y / mapInfo.tileSize.y)
                callback(pointInTileMap, Vector2(tileX, tileY), type)
            }
        }
        this.touchable = touchable
        return touchable
    }
}