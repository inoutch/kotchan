package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkAttachmentDescription actual constructor(format: VkFormat, samples: List<VkSampleFlagBits>, loadOp: VkAttachmentLoadOp, storeOp: VkAttachmentStoreOp, stencilLoadOp: VkAttachmentLoadOp, stencilStoreOp: VkAttachmentStoreOp, initialLayout: VkImageLayout, finalLayout: VkImageLayout) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
