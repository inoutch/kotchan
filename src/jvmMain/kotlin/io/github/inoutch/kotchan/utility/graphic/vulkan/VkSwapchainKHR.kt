package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.KHRSwapchain
import org.lwjgl.vulkan.VK10

actual class VkSwapchainKHR : Disposable {
    var native: Long = 0

    private lateinit var device: VkDevice

    fun init(nativeSwapchain: Long, device: VkDevice) {
        this.native = nativeSwapchain
        this.device = device
    }

    override fun dispose() {
        KHRSwapchain.vkDestroySwapchainKHR(device.native, native, null)
    }
}

actual fun vkCreateSwapchainKHR(device: VkDevice, createInfo: VkSwapchainCreateInfoKHR) = memScoped {
    val native = allocLong()

    checkError(KHRSwapchain.vkCreateSwapchainKHR(
            device.native,
            createInfo.toNative(this),
            null,
            native))

    VkSwapchainKHR().apply { init(native.get(0), device) }
}

actual fun vkGetSwapchainImagesKHR(device: VkDevice, swapchain: VkSwapchainKHR) = memScoped {
    val imageCount = allocInt()

    checkError(KHRSwapchain.vkGetSwapchainImagesKHR(
            device.native,
            swapchain.native,
            imageCount,
            null))

    val native = allocLong(imageCount.get(0))
    checkError(KHRSwapchain.vkGetSwapchainImagesKHR(
            device.native,
            swapchain.native,
            imageCount,
            native))

    // not need destroy the image
    List(imageCount.get(0)) { VkImage().apply { init(native.get(it)) } }
}

actual fun vkAcquireNextImageKHR(
    device: VkDevice,
    swapchain: VkSwapchainKHR,
    timeout: Long,
    semaphore: VkSemaphore,
    fence: VkFence?
) = memScoped {
    val index = allocInt()
    KHRSwapchain.vkAcquireNextImageKHR(device.native, swapchain.native, timeout, semaphore.native, fence?.native
            ?: VK10.VK_NULL_HANDLE, index)
    index.get(0)
}
