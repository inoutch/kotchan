package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkSubpassDescription actual constructor(pipelineBindPoint: VkPipelineBindPoint, inputAttachments: List<VkAttachmentReference>, colorAttachments: List<VkAttachmentReference>, resolveAttachments: List<VkAttachmentReference>, depthStencilAttachment: VkAttachmentReference?, preserveAttachments: List<Int>) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
