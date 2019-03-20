package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKPipeline
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKSampler
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKUniformBuffer

class GraphicsPipeline(
        val createInfo: CreateInfo,
        val vkPipeline: VKPipeline? = null,
        val vkUniforms: List<VKUniformBuffer> = listOf(),
        val vkSamplers: List<VKSampler> = listOf()) : Disposable {

    data class CreateInfo(
            val shaderProgram: ShaderProgram,
            val depthTest: Boolean = true,
            val cullMode: CullMode = CullMode.Back,
            val polygonMode: PolygonMode = PolygonMode.Fill)

    override fun dispose() {
        // only vulkan
        vkPipeline?.dispose()
    }

    fun bind() {
        instance.graphicsApi.bindGraphicsPipeline(this)
    }
}
