package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkRenderPassCreateInfo actual constructor(attachments: List<VkAttachmentDescription>, subpasses: List<VkSubpassDescription>, dependencies: List<VkSubpassDependency>) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
