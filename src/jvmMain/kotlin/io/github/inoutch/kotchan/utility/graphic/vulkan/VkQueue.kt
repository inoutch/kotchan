package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.KHRSwapchain
import org.lwjgl.vulkan.VK10

actual class VkQueue {
    lateinit var native: org.lwjgl.vulkan.VkQueue
        private set

    fun init(nativeQueue: org.lwjgl.vulkan.VkQueue) {
        native = nativeQueue
    }
}

actual fun vkGetDeviceQueue(device: VkDevice, queueFamilyIndex: Int, queueIndex: Int): VkQueue {
    return memScoped {
        val native = allocPointer()
        VK10.vkGetDeviceQueue(device.native, queueFamilyIndex, queueIndex, native)

        VkQueue().apply { init(org.lwjgl.vulkan.VkQueue(native.get(0), device.native)) }
    }
}

actual fun vkQueueSubmit(queue: VkQueue, submitInfos: List<VkSubmitInfo>, fence: VkFence?) = memScoped {
    checkError(VK10.vkQueueSubmit(
            queue.native,
            submitInfos.toNative(this),
            fence?.native ?: VK10.VK_NULL_HANDLE))
}

actual fun vkQueuePresentKHR(queue: VkQueue, presentInfo: VkPresentInfoKHR) = memScoped {
    checkError(KHRSwapchain.vkQueuePresentKHR(queue.native, presentInfo.toNative(this)))
}

actual fun vkQueueWaitIdle(queue: VkQueue) {
    checkError(VK10.vkQueueWaitIdle(queue.native))
}
