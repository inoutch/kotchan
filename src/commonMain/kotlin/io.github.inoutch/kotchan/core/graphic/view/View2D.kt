package io.github.inoutch.kotchan.core.graphic.view

import io.github.inoutch.kotchan.utility.graphic.GLTexture
import io.github.inoutch.kotchan.utility.type.Matrix4
import io.github.inoutch.kotchan.utility.type.Mesh
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3

open class View2D(initMesh: Mesh, texture: GLTexture = GLTexture.empty) : View2DBase, View(initMesh, texture) {

    final override var size = Vector2()
        protected set

    override var anchorPoint = Vector2(0.5f, 0.5f)
        set(value) {
            if (field != value)
                isPositionsDirty = true
            field = value
        }

    override fun transform() = super.transform() * Matrix4.createTranslation(Vector3(size * anchorPoint * -1.0f, 0.0f))
}
