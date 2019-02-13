package io.github.inoutch.kotchan.core.graphic.ui

import io.github.inoutch.kotchan.core.controller.touch.listener.TouchListener
import io.github.inoutch.kotchan.core.graphic.view.View2D
import io.github.inoutch.kotchan.utility.graphic.GLTexture
import io.github.inoutch.kotchan.utility.type.Mesh

abstract class UI(initMesh: Mesh, texture: GLTexture = GLTexture.empty) : View2D(initMesh, texture) {
    abstract val touchListener: TouchListener
}
