package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

actual class VkQueue {
    lateinit var native: vulkan.VkQueue

    fun init(native: vulkan.VkQueue) {
        this.native = native
    }
}

@ExperimentalUnsignedTypes
actual fun vkGetDeviceQueue(device: VkDevice, queueFamilyIndex: Int, queueIndex: Int) = memScoped {
    val native = alloc<vulkan.VkQueueVar>()

    vulkan.vkGetDeviceQueue(device.native, queueFamilyIndex.toUInt(), queueIndex.toUInt(), native.ptr)

    VkQueue().apply { init(native.value ?: throw VkNullError("queue")) }
}

@ExperimentalUnsignedTypes
actual fun vkQueueSubmit(queue: VkQueue, submitInfos: List<VkSubmitInfo>, fence: VkFence?) = memScoped {
    checkError(vulkan.vkQueueSubmit(
            queue.native,
            submitInfos.size.toUInt(),
            submitInfos.toNative(this),
            fence?.native))
}

@ExperimentalUnsignedTypes
actual fun vkQueuePresentKHR(queue: VkQueue, presentInfo: VkPresentInfoKHR) = memScoped {
    checkError(vulkan.vkQueuePresentKHR(queue.native, presentInfo.toNative(this)))
}

actual fun vkQueueWaitIdle(queue: VkQueue) {
    checkError(vulkan.vkQueueWaitIdle(queue.native))
}
