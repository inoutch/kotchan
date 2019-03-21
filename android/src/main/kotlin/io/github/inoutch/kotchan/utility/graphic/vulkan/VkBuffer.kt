package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkBuffer : Disposable {
    override fun dispose() {}
}

actual fun vkCreateBuffer(device: VkDevice, createInfo: VkBufferCreateInfo): VkBuffer {
    // not implementation
    return VkBuffer()
}
