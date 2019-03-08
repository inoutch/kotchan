package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

actual class VkDescriptorSetLayout : Disposable {
    lateinit var native: vulkan.VkDescriptorSetLayout
        private set

    private lateinit var device: VkDevice

    fun init(nativeDescriptorSetLayout: vulkan.VkDescriptorSetLayout, device: VkDevice) {
        this.native = nativeDescriptorSetLayout
        this.device = device
    }

    override fun dispose() {
        vulkan.vkDestroyDescriptorSetLayout(device.native, native, null)
    }
}

fun List<VkDescriptorSetLayout>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkDescriptorSetLayoutVar>(size)
                .also { forEachIndexed { index, x -> it[index] = x.native } }

@ExperimentalUnsignedTypes
actual fun vkCreateDescriptorSetLayout(device: VkDevice, createInfo: VkDescriptorSetLayoutCreateInfo) = memScoped {
    val native = alloc<vulkan.VkDescriptorSetLayoutVar>()

    checkError(vulkan.vkCreateDescriptorSetLayout(device.native, createInfo.toNative(this), null, native.ptr))

    VkDescriptorSetLayout().apply {
        init(native.value ?: throw VkNullError("descriptorSetLayout"), device)
    }
}
