package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkSubpassDependency actual constructor(srcSubpassIndex: Int, dstSubpassIndex: Int, srcStageMask: VkPipelineStageFlagBits, dstStageMask: VkPipelineStageFlagBits, srcAccessMask: VkPipelineStageFlagBits, dstAccessMask: VkPipelineStageFlagBits) : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
