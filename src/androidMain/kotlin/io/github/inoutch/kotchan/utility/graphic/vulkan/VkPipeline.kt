package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkPipeline : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkCreateGraphicsPipelines(device: VkDevice, pipelineCache: VkPipelineCache?, createInfos: List<VkGraphicsPipelineCreateInfo>): VkPipeline {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}
