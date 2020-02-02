package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperties
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkImage
import io.github.inoutch.kotlin.vulkan.api.VkSwapchainKHR
import io.github.inoutch.kotlin.vulkan.api.vk

class VKSwapchain(
    val logicalDevice: VKLogicalDevice,
    val swapchain: VkSwapchainKHR
) : Disposer() {
    init {
        add { vk.destroySwapchainKHR(logicalDevice.device, swapchain) }
    }

    fun getSwapchainImages(): List<VKImage> {
        return getProperties<VkImage> { vk.getSwapchainImagesKHR(logicalDevice.device, swapchain, it).value }
                .map { p -> VKImage(logicalDevice, p, false).also { add(it) } }
    }

    fun acquireNextImageKHR(semaphore: VKSemaphore?, fence: VKFence?, timeout: Long = Long.MAX_VALUE): Int {
        return getProperty {
            vk.acquireNextImageKHR(logicalDevice.device, swapchain, timeout, semaphore?.semaphore, fence?.fence, it).value
        }
    }
}
