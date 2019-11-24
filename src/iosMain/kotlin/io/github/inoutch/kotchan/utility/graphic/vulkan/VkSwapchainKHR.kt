package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkSwapchainKHR : Disposable {
    lateinit var native: vulkan.VkSwapchainKHR
        private set

    private lateinit var device: VkDevice

    fun init(nativeSwapchain: vulkan.VkSwapchainKHR, device: VkDevice) {
        this.native = nativeSwapchain
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroySwapchainKHR(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateSwapchainKHR(
    device: VkDevice,
    createInfo: VkSwapchainCreateInfoKHR
) = memScoped {
    val native = alloc<vulkan.VkSwapchainKHRVar>()

    checkError(vulkan.vkCreateSwapchainKHR(device.native, createInfo.toNative(this), null, native.ptr))

    VkSwapchainKHR().apply { init(native.value ?: throw VkNullError("swapchain"), device) }
}

@ExperimentalUnsignedTypes
actual fun vkGetSwapchainImagesKHR(device: VkDevice, swapchain: VkSwapchainKHR) = memScoped {
    val count = alloc<UIntVar>()

    checkError(vulkan.vkGetSwapchainImagesKHR(device.native, swapchain.native, count.ptr, null))

    val natives = allocArray<vulkan.VkImageVar>(count.value.toInt())

    checkError(vulkan.vkGetSwapchainImagesKHR(device.native, swapchain.native, count.ptr, natives))

    List(count.value.toInt()) {
        VkImage().apply {
            init(natives[it] ?: throw VkNullError("swapchain.image"))
        }
    }
}

@ExperimentalUnsignedTypes
actual fun vkAcquireNextImageKHR(
    device: VkDevice,
    swapchain: VkSwapchainKHR,
    timeout: Long,
    semaphore: VkSemaphore,
    fence: VkFence?
) = memScoped {
    val imageCount = alloc<UIntVar>()

    checkError(vulkan.vkAcquireNextImageKHR(
            device.native,
            swapchain.native,
            timeout.toULong(),
            semaphore.native,
            fence?.native,
            imageCount.ptr))
    imageCount.value.toInt()
}
