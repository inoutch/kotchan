package kotchan.scene.drawable

import utility.type.*

open class Square(size: Size) : Drawable(createSquareMesh(size)) {
    companion object {
        fun createSquareMesh(size: Size) = Mesh(
                listOf(Vector3(0.0f, 0.0f, 0.0f),
                        Vector3(size.width, size.height, 0.0f),
                        Vector3(0.0f, size.height, 0.0f),
                        Vector3(0.0f, 0.0f, 0.0f),
                        Vector3(size.width, 0.0f, 0.0f),
                        Vector3(size.width, size.height, 0.0f)),
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