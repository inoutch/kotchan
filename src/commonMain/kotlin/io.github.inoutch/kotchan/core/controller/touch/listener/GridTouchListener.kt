package io.github.inoutch.kotchan.core.controller.touch.listener

import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4
import kotlin.math.floor

open class GridTouchListener(camera: Camera, private val tileSize: Point,
                             private val callback: (pointInTile: Point, type: TouchType) -> Boolean,
                             private val positionCallback: () -> Vector3 = { Vector3.Zero }) : TouchListener(camera) {

    override fun callback(index: Int, normalizedPoint: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        if (check && chain) {
            return callback(convertToTilePoint(normalizedPoint, camera), type)
        }
        return chain
    }

    private fun convertToTilePoint(normalizedPoint: Vector2, camera: Camera): Point {
        val pointInView = camera.combine * Vector4(positionCallback(), 1.0f)
        val cameraInView = camera.combine * Vector4(camera.position, 1.0f)
        val aInView = camera.combine * Vector4(Vector3(), 1.0f)
        val bInView = camera.combine * tileSize.toVector4(0.0f, 1.0f)
        val sizeInView = Vector2(bInView.x - aInView.x, bInView.y - aInView.y)
        val intervalInView = Vector2(cameraInView.x - pointInView.x, cameraInView.y - pointInView.y)
        val pointInTileMap = (intervalInView + normalizedPoint + 1.0f) / sizeInView * tileSize
        return Point(floor(pointInTileMap.x / tileSize.x), floor(pointInTileMap.y / tileSize.y))
    }
}
