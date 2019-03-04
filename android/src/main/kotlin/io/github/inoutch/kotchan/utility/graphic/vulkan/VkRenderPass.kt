package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkRenderPass : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkCreateRenderPass(device: VkDevice, createInfo: VkRenderPassCreateInfo): VkRenderPass {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual fun vkCmdBeginRenderPass(commandBuffer: VkCommandBuffer, beginInfo: VkRenderPassBeginInfo) {
}
