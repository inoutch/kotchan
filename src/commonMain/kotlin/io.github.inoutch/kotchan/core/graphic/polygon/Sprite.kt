package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.utility.type.*

open class Sprite(size: Vector2) : Polygon2D(createSquareMesh(size), size) {
    companion object {
        fun createSquareMesh(size: Vector2) = Mesh(
                createSquarePositions(Vector2(), size),
                createSquareTexcoords(Vector2(), Vector2(1.0f, 1.0f)),
                List(6) { Vector4(1.0f, 1.0f, 1.0f, 1.0f) }
        )

        fun createSquarePositions(position: Vector2, size: Vector2) = listOf(
                Vector3(position.x, position.y, 0.0f),
                Vector3(position.x + size.x, position.y + size.y, 0.0f),
                Vector3(position.x, position.y + size.y, 0.0f),
                Vector3(position.x, position.y, 0.0f),
                Vector3(position.x + size.x, position.y, 0.0f),
                Vector3(position.x + size.x, position.y + size.y, 0.0f))

        fun createSquarePositions(rect: Rect) = createSquarePositions(rect.origin, rect.size)

        fun createSquareTexcoords(position: Vector2, size: Vector2) = listOf(
                Vector2(position.x, position.y + size.y),
                Vector2(position.x + size.x, position.y),
                Vector2(position.x, position.y),
                Vector2(position.x, position.y + size.y),
                Vector2(position.x + size.x, position.y + size.y),
                Vector2(position.x + size.x, position.y))

        fun createSquareTexcoords(rect: Rect) = createSquareTexcoords(rect.origin, rect.size)
    }
}
