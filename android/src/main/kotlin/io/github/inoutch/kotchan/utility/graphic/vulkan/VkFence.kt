package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkFence : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkCreateFence(device: VkDevice, createInfo: VkFenceCreateInfo): VkFence {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
