package kotchan.scene.map

import kotchan.camera.Camera
import kotchan.controller.TouchType
import kotchan.controller.touchable.RectTouchable
import kotchan.controller.touchable.Touchable
import kotchan.scene.shader.NoColorsShaderProgram
import utility.type.*
import kotlin.math.floor

class TileMap(private val mapInfo: TileMapInfo) {
    val size = mapInfo.mapSize * mapInfo.tileSize
    var position = Vector3()

    private val shaderProgram = NoColorsShaderProgram()
    private val layers: List<TileLayer> = mapInfo.layersInfo.map { TileLayer(mapInfo, it) }
    var touchable: Touchable? = null
        private set

    fun draw(delta: Float, camera: Camera, layerRange: IntRange = IntRange(0, layers.size - 1)) {
        mapInfo.texture.use()
        shaderProgram.use()
        shaderProgram.modelMatrix4 = Matrix4.createTranslation(position)
        shaderProgram.prepare(delta, camera.combine)
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

    fun enableTouch(camera: Camera, callback: (point: Vector2, tilePoint: Vector2, type: TouchType) -> Unit): Touchable {
        val touchable = RectTouchable({ Rect(Vector2(position.x, position.y), size) }, camera) { _, point, type, check, chain ->
            if (check && chain) {
                val pointInView = camera.combine * Vector4(position, 1.0f)
                val cameraInView = camera.combine * Vector4(camera.position, 1.0f)
                val aInView = camera.combine * Vector4(Vector3(), 1.0f)
                val bInView = camera.combine * Vector4(Vector3(mapInfo.tileSize, 0.0f), 1.0f)
                val sizeInView = Vector2(bInView.x - aInView.x, bInView.y - aInView.y)
                val intervalInView = Vector2(cameraInView.x - pointInView.x, cameraInView.y - pointInView.y)
                val pointInTileMap = (intervalInView + point + 1.0f) / sizeInView * mapInfo.tileSize
                val tileX = floor(pointInTileMap.x / mapInfo.tileSize.x)
                val tileY = floor(pointInTileMap.y / mapInfo.tileSize.y)
                callback(pointInTileMap, Vector2(tileX, tileY), type)
            }
            return@RectTouchable true
        }
        this.touchable = touchable
        return touchable
    }
}