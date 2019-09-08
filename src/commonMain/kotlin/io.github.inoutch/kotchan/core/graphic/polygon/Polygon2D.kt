package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.utility.type.*

open class Polygon2D(initMesh: Mesh, material: Material?, var size: Vector2) : Polygon(initMesh, material) {

    open var anchorPoint = Vector2(0.5f, 0.5f)
        set(value) {
            if (field != value)
                setDirtyPosition()
            field = value
        }

    override fun transform() = super.transform() * Matrix4.createTranslation(
            Vector3(size * anchorPoint * -1.0f, 0.0f))

    override fun childrenTransform() = transform() * Matrix4.createTranslation(
            Vector3(size * 0.5f, 0.0f))

    fun rect() = Rect(Vector2(position.x, position.y) - anchorPoint * size, size)
}
