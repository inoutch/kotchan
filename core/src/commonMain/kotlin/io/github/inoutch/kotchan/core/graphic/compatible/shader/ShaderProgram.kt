package io.github.inoutch.kotchan.core.graphic.compatible.shader

import io.github.inoutch.kotchan.core.Disposable
import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic

abstract class ShaderProgram(val shaderSource: ShaderSource) : Disposable {
//    private val shader = graphic.createShader(shaderSource)

    override fun dispose() {
//        shader.dispose()
    }
}
