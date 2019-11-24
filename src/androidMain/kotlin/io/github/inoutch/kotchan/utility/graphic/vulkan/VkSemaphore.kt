package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkSemaphore : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkCreateSemaphore(device: VkDevice, createInfo: VkSemaphoreCreateInfo): VkSemaphore {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}
