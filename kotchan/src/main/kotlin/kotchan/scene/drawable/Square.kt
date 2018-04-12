package kotchan.scene.drawable

import interop.graphic.GLTexture
import utility.type.*

open class Square(size: Vector2, texture: GLTexture = GLTexture.empty) : Drawable(createSquareMesh(size), texture) {
    companion object {
        fun createSquareMesh(size: Vector2) = Mesh(
                createSquarePositions(Vector2(), size),
                createSquareTexcoords(Vector2(), Vector2(1.0f, 1.0f)),
                listOf(Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f))
        )

        fun createSquarePositions(position: Vector2, size: Vector2) = listOf(
                Vector3(position.x, position.y, 0.0f),
                Vector3(position.x + size.x, position.y + size.y, 0.0f),
                Vector3(position.x, position.y + size.y, 0.0f),
                Vector3(position.x, position.y, 0.0f),
                Vector3(position.x + size.x, position.y, 0.0f),
                Vector3(position.x + size.x, position.y + size.y, 0.0f))

        fun createSquareTexcoords(position: Vector2, size: Vector2) = listOf(
                Vector2(position.x, position.y + size.y),
                Vector2(position.x + size.x, position.y),
                Vector2(position.x, position.y),
                Vector2(position.x, position.y + size.y),
                Vector2(position.x + size.x, position.y + size.y),
                Vector2(position.x + size.x, position.y))
    }
}