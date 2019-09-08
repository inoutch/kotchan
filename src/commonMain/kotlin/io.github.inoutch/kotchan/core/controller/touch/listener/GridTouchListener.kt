package io.github.inoutch.kotchan.core.controller.touch.listener

import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4
import kotlin.math.floor

open class GridTouchListener(
        camera: Camera, // Use centered camera
        private val tileSize: Point,
        private val callback: (pointInTile: Point, type: TouchType) -> Boolean,
        private val positionCallback: () -> Vector3 = { Vector3.Zero }) : TouchListener(camera) {

    override fun callback(index: Int, normalizedPoint: Vector2, type: TouchType, check: Boolean, chain: Boolean): Boolean {
        if (check && chain) {
            return callback(convertToTilePoint(normalizedPoint), type)
        }
        return chain
    }

    private fun convertToTilePoint(normalizedPoint: Vector2): Point {
        val sizeInView = (camera.projectionMatrix * tileSize.toVector4(0.0f, 1.0f)).toVector2()
        val intervalInView = (camera.projectionMatrix * Vector4(camera.position - positionCallback(), 1.0f)).toVector2()
        val pointInTileMap = (intervalInView + normalizedPoint) / sizeInView * tileSize
        return Point(floor(pointInTileMap.x / tileSize.x), floor(pointInTileMap.y / tileSize.y))
    }
}

