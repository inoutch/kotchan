package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.Disposable

class Material(config: Config, extraTextures: List<Texture> = listOf()) : Disposable {
    class Config(val shaderProgram: ShaderProgram, val textures: List<Texture> = listOf()) {
        constructor(shaderProgram: ShaderProgram, texture: Texture) : this(shaderProgram, listOf(texture))

        var depthTest = true
        var cullMode = CullMode.Back
        var polygonMode = PolygonMode.Fill
    }

    val textures = listOf(*config.textures.toTypedArray(), *extraTextures.toTypedArray())

    val graphicsPipeline = instance.graphicsApi.createGraphicsPipeline(
            GraphicsPipeline.CreateInfo(config.shaderProgram, config.depthTest, config.cullMode, config.polygonMode))

    override fun dispose() {
        graphicsPipeline.dispose()
    }
}
