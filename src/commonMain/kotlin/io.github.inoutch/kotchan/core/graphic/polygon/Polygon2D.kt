package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Mesh
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

open class Polygon2D(initMesh: Mesh, material: Material?, size: Vector2) : Polygon(initMesh, material) {

    open var size = size
        protected set

    open var anchorPoint = Vector2(0.5f, 0.5f)
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    override fun transform() = super.transform() * Matrix4.createTranslation(
            Vector3(size * anchorPoint * -1.0f, 0.0f))
}
