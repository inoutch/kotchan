package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.core
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKPipeline

class GraphicsPipeline(
    val shaderProgram: ShaderProgram,
    val config: Config,
    val vkPipeline: VKPipeline? = null
) : Disposable {

    data class Config(
        val depthTest: Boolean = true,
        val cullMode: CullMode = CullMode.Back,
        val polygonMode: PolygonMode = PolygonMode.Fill,
        val srcBlendFactor: BlendFactor = BlendFactor.SrcAlpha,
        val dstBlendFactor: BlendFactor = BlendFactor.OneMinusSrcAlpha
    )

    fun bind() {
        core.graphicsApi.bindGraphicsPipeline(this)
    }

    override fun dispose() {
        // only vulkan
        vkPipeline?.dispose()
    }
}
