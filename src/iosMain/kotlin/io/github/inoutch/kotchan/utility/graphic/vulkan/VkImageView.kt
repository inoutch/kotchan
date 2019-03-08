package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*
import vulkan.VkImageViewVar

actual class VkImageView : Disposable {
    lateinit var native: vulkan.VkImageView
        private set

    private lateinit var device: VkDevice

    fun init(nativeImageView: vulkan.VkImageView, device: VkDevice) {
        this.native = nativeImageView
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyImageView(device.native, native, null)
    }
}

fun List<VkImageView>.toNative(scope: MemScope) =
        scope.allocArray<VkImageViewVar>(size)
                .also { forEachIndexed { index, x -> it[index] = x.native } }

@ExperimentalUnsignedTypes
actual fun vkCreateImageView(device: VkDevice, createInfo: VkImageViewCreateInfo) = memScoped {
    val native = alloc<VkImageViewVar>()

    checkError(vulkan.vkCreateImageView(device.native, createInfo.toNative(this), null, native.ptr))

    VkImageView().apply { init(native.value ?: throw VkNullError("imageView"), device) }
}
