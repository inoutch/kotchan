package kotchan.scene.drawable

import interop.graphic.GLTexture
import utility.type.*

open class Square(size: Vector2, texture: GLTexture = GLTexture.empty) : Drawable(createSquareMesh(size), texture) {
    companion object {
        fun createSquareMesh(size: Vector2) = Mesh(
                listOf(Vector3(0.0f, 0.0f, 0.0f),
                        Vector3(size.x, size.y, 0.0f),
                        Vector3(0.0f, size.y, 0.0f),
                        Vector3(0.0f, 0.0f, 0.0f),
                        Vector3(size.x, 0.0f, 0.0f),
                        Vector3(size.x, size.y, 0.0f)),
                listOf(Vector2(0.0f, 1.0f),
                        Vector2(1.0f, 0.0f),
                        Vector2(0.0f, 0.0f),
                        Vector2(0.0f, 1.0f),
                        Vector2(1.0f, 1.0f),
                        Vector2(1.0f, 0.0f)),
                listOf(Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f),
                        Vector4(1.0f, 1.0f, 1.0f, 1.0f))
        )
    }
}