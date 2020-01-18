package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkSemaphore
import io.github.inoutch.kotlin.vulkan.api.vk

class VKSemaphore(val logicalDevice: VKLogicalDevice, val semaphore: VkSemaphore) : Disposer() {
    init {
        add { vk.destroySemaphore(logicalDevice.device, semaphore) }
    }
}
