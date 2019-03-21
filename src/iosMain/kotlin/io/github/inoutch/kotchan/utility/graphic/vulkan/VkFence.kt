package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkFence : Disposable {
    lateinit var native: vulkan.VkFence
        private set

    private lateinit var device: VkDevice

    fun init(nativeFence: vulkan.VkFence, device: VkDevice) {
        this.native = nativeFence
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyFence(device.native, native, null)
    }
}

@ExperimentalUnsignedTypes
actual fun vkCreateFence(device: VkDevice, createInfo: VkFenceCreateInfo) = memScoped {
    val native = alloc<vulkan.VkFenceVar>()

    checkError(vulkan.vkCreateFence(device.native, createInfo.toNative(this), null, native.ptr))

    VkFence().apply { init(native.value ?: throw VkNullError("fence"), device) }
}

@ExperimentalUnsignedTypes
actual fun vkWaitForFences(device: VkDevice, fences: List<VkFence>, waitAll: Boolean, timeout: Long) = memScoped {
    checkError(vulkan.vkWaitForFences(
            device.native,
            fences.size.toUInt(),
            fences.map { it.native }.toNative(this),
            waitAll.toVkBool32(),
            timeout.toULong()))
}

@ExperimentalUnsignedTypes
actual fun vkResetFences(device: VkDevice, fences: List<VkFence>) = memScoped {
    checkError(vulkan.vkResetFences(
            device.native, fences.size.toUInt(), fences.map { it.native }.toNative(this)))
}
