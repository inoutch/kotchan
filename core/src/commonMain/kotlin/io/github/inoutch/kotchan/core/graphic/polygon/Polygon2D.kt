package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotchan.math.RectF
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F

open class Polygon2D(
    initMesh: Mesh,
    var size: Vector2F
) : Polygon(initMesh) {

    open var anchorPoint = Vector2F(0.5f, 0.5f)
        set(value) {
            if (field != value) {
                updatePositionAll()
            }
            field = value
        }

    override fun transform() = super.transform() * Matrix4F.createTranslation(
            Vector3F(size * anchorPoint * -1.0f, 0.0f))

    override fun childrenTransform() = transform() * Matrix4F.createTranslation(
            Vector3F(size * 0.5f, 0.0f))

    fun rect() = RectF(Vector2F(position.x, position.y) - anchorPoint * size, size)
}
