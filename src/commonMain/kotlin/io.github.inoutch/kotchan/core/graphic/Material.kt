package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.Disposable

class Material(config: Config, extraTextures: List<Texture> = listOf()) : Disposable {
    class Config(val shaderProgram: ShaderProgram, val textures: List<Texture> = listOf()) {
        constructor(shaderProgram: ShaderProgram, texture: Texture) : this(shaderProgram, listOf(texture))

        var depthTest = true
        var cullMode = CullMode.Back
        var polygonMode = PolygonMode.Fill
        var srcBlendFactor = BlendFactor.SrcAlpha
        var dstBlendFactor = BlendFactor.OneMinusSrcAlpha
    }

    val textures = listOf(*config.textures.toTypedArray(), *extraTextures.toTypedArray())

    val texture: Texture
        get() = textures.first()

    val graphicsPipeline = core.graphicsApi.createGraphicsPipeline(
            config.shaderProgram,
            GraphicsPipeline.Config(
                    config.depthTest,
                    config.cullMode,
                    config.polygonMode,
                    config.srcBlendFactor,
                    config.dstBlendFactor))

    var textureAutoRelease = false

    override fun dispose() {
        graphicsPipeline.dispose()
        if (textureAutoRelease) {
            textures.forEach { it.dispose() }
        }
    }
}
