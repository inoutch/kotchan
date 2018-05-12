package kotchan.view.map

import kotchan.controller.touch.TouchType
import kotchan.controller.touch.listener.ArgTouchListener
import kotchan.controller.touch.listener.TouchListener
import kotchan.view.camera.Camera
import kotchan.view.shader.NoColorsShaderProgram
import utility.type.*
import kotlin.math.floor

abstract class TileMapBase(val mapInfo: TileMapInfo) {
    private val shaderProgram = NoColorsShaderProgram()

    val size = mapInfo.mapSize * mapInfo.tileSize
    var position = Vector3()
    var touchListener: TouchListener? = null
        private set

    abstract fun layer(index: Int): TileLayerBase?
    abstract fun layerSize(): Int

    open fun destroy() {
        for (i in 0..layerSize()) {
            layer(i)?.destroy()
        }
        shaderProgram.destroy()
    }

    open fun draw(delta: Float, camera: Camera, layerRange: IntRange = IntRange(0, layerSize() - 1)) {
        mapInfo.texture.use()
        shaderProgram.use()
        shaderProgram.modelMatrix4 = Matrix4.createTranslation(position)
        shaderProgram.prepare(delta, camera.combine)
        for (i in layerRange) {
            val l = layer(i) ?: continue
            l.draw(delta)
        }
    }

    fun enableTouch(camera: Camera, callback: (pointInTile: Pair<Int, Int>, type: TouchType) -> Boolean): TouchListener {
        val touchListener = ArgTouchListener(camera) { _, point, type, check, chain ->
            if (check && chain) {
                return@ArgTouchListener callback(convertToTilePoint(point, camera), type)
            }
            return@ArgTouchListener chain
        }
        this.touchListener = touchListener
        return touchListener
    }

    fun convertToTilePoint(point: Vector2, camera: Camera): Pair<Int, Int> {
        val pointInView = camera.combine * Vector4(position, 1.0f)
        val cameraInView = camera.combine * Vector4(camera.position, 1.0f)
        val aInView = camera.combine * Vector4(Vector3(), 1.0f)
        val bInView = camera.combine * Vector4(Vector3(mapInfo.tileSize, 0.0f), 1.0f)
        val sizeInView = Vector2(bInView.x - aInView.x, bInView.y - aInView.y)
        val intervalInView = Vector2(cameraInView.x - pointInView.x, cameraInView.y - pointInView.y)
        val pointInTileMap = (intervalInView + point + 1.0f) / sizeInView * mapInfo.tileSize
        val tileX = floor(pointInTileMap.x / mapInfo.tileSize.x)
        val tileY = floor(pointInTileMap.y / mapInfo.tileSize.y)
        return tileX.toInt() to tileY.toInt()
    }
}