package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkSwapchainKHR : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkCreateSwapchainKHR(device: VkDevice, createInfo: VkSwapchainCreateInfoKHR): VkSwapchainKHR {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}

actual fun vkGetSwapchainImagesKHR(device: VkDevice, swapchain: VkSwapchainKHR): List<VkImage> {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}

actual fun vkAcquireNextImageKHR(device: VkDevice, swapchain: VkSwapchainKHR, timeout: Long, semaphore: VkSemaphore, fence: VkFence?): Int {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}
