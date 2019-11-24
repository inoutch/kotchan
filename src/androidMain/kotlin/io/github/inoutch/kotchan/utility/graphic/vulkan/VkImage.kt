package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkImage : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkCreateImage(device: VkDevice, createInfo: VkImageCreateInfo): VkImage {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}

actual fun vkBindImageMemory(device: VkDevice, image: VkImage, memory: VkDeviceMemory, memoryOffset: Long) {
}
