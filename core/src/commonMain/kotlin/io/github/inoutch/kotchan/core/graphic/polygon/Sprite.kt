package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.math.RectF
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

open class Sprite(size: Vector2F) : Polygon2D(createSquareMesh(size), size) {
    companion object {
        fun createSquareMesh(size: Vector2F) = Mesh(
                createSquarePositions(Vector2F.Zero, size),
                createSquareTexcoords(Vector2F.Zero, Vector2F(1.0f, 1.0f)),
                List(6) { Vector4F(1.0f, 1.0f, 1.0f, 1.0f) }
        )

        fun createSquarePositions(position: Vector2F, size: Vector2F) = listOf(
                Vector3F(position.x, position.y, 0.0f),
                Vector3F(position.x + size.x, position.y + size.y, 0.0f),
                Vector3F(position.x, position.y + size.y, 0.0f),
                Vector3F(position.x, position.y, 0.0f),
                Vector3F(position.x + size.x, position.y, 0.0f),
                Vector3F(position.x + size.x, position.y + size.y, 0.0f))

        fun createSquarePositions(rect: RectF) = createSquarePositions(rect.origin, rect.size)

        fun createSquareTexcoords(position: Vector2F, size: Vector2F) = listOf(
                Vector2F(position.x, position.y + size.y),
                Vector2F(position.x + size.x, position.y),
                Vector2F(position.x, position.y),
                Vector2F(position.x, position.y + size.y),
                Vector2F(position.x + size.x, position.y + size.y),
                Vector2F(position.x + size.x, position.y))

        fun createSquareTexcoords(rect: RectF) = createSquareTexcoords(rect.origin, rect.size)
    }
}
