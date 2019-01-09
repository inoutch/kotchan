package io.github.inoutch.kotchan.core.view.map

import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.controller.touch.listener.TouchListener
import io.github.inoutch.kotchan.core.view.shader.NoColorsShaderProgram
import io.github.inoutch.kotchan.utility.type.*
import io.github.inoutch.kotchan.core.controller.touch.listener.ArgTouchListener
import io.github.inoutch.kotchan.core.destruction.StrictDestruction
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.utility.graphic.GLShaderProgram
import kotlin.math.floor

abstract class TileMapBase(
        val mapInfo: TileMapInfo,
        private val shaderProgram: NoColorsShaderProgram = NoColorsShaderProgram()) : StrictDestruction() {

    val size = mapInfo.mapSize * mapInfo.tileSize

    var position = Vector3()

    var touchListener: TouchListener? = null
        private set

    abstract fun layer(index: Int): TileLayerBase?

    abstract fun layerSize(): Int

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

    fun enableTouch(camera: Camera, callback: (pointInTile: Point, type: TouchType) -> Boolean): TouchListener {
        val touchListener = ArgTouchListener(camera) { _, normalizedPoint, type, check, chain ->
            if (check && chain) {
                return@ArgTouchListener callback(convertToTilePoint(normalizedPoint, camera), type)
            }
            return@ArgTouchListener chain
        }
        this.touchListener = touchListener
        return touchListener
    }

    fun convertToTilePoint(normalizedPoint: Vector2, camera: Camera): Point {
        val pointInView = camera.combine * Vector4(position, 1.0f)
        val cameraInView = camera.combine * Vector4(camera.position, 1.0f)
        val aInView = camera.combine * Vector4(Vector3(), 1.0f)
        val bInView = camera.combine * mapInfo.tileSize.toVector4(0.0f, 1.0f)
        val sizeInView = Vector2(bInView.x - aInView.x, bInView.y - aInView.y)
        val intervalInView = Vector2(cameraInView.x - pointInView.x, cameraInView.y - pointInView.y)
        val pointInTileMap = (intervalInView + normalizedPoint + 1.0f) / sizeInView * mapInfo.tileSize
        return Point(floor(pointInTileMap.x / mapInfo.tileSize.x), floor(pointInTileMap.y / mapInfo.tileSize.y))
    }

    override fun destroy() {
        super.destroy()
        for (i in 0..layerSize()) {
            layer(i)?.destroy()
        }
    }
}