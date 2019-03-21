package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkSwapchainKHR : Disposable

expect fun vkCreateSwapchainKHR(device: VkDevice, createInfo: VkSwapchainCreateInfoKHR): VkSwapchainKHR

expect fun vkGetSwapchainImagesKHR(device: VkDevice, swapchain: VkSwapchainKHR): List<VkImage>

expect fun vkAcquireNextImageKHR(
        device: VkDevice,
        swapchain: VkSwapchainKHR,
        timeout: Long,
        semaphore: VkSemaphore,
        fence: VkFence?): Int
