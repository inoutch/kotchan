package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.Disposable
import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
actual class VkDescriptorSet : Disposable {
    lateinit var native: vulkan.VkDescriptorSet
        private set

    private lateinit var device: VkDevice

    private lateinit var descriptorSetPool: VkDescriptorPool

    fun init(nativeDescriptorSet: vulkan.VkDescriptorSet, device: VkDevice, descriptorSetPool: VkDescriptorPool) {
        this.native = nativeDescriptorSet
        this.device = device
        this.descriptorSetPool = descriptorSetPool
    }

    override fun dispose() = memScoped {
        checkError(vulkan.vkFreeDescriptorSets(
                device.native,
                descriptorSetPool.native,
                1,
                listOf(native).toNative(this)))
    }
}

@ExperimentalUnsignedTypes
actual fun vkAllocateDescriptorSets(device: VkDevice, allocateInfo: VkDescriptorSetAllocateInfo) = memScoped {
    val native = allocArray<vulkan.VkDescriptorSetVar>(allocateInfo.descriptorSetCount)

    checkError(vulkan.vkAllocateDescriptorSets(device.native, allocateInfo.toNative(this), native))

    List(allocateInfo.descriptorSetCount) {
        VkDescriptorSet().apply {
            init(native[it] ?: throw VkNullError("descriptorSet"),
                    device, allocateInfo.descriptorPool)
        }
    }
}

@ExperimentalUnsignedTypes
actual fun vkUpdateDescriptorSets(
        device: VkDevice,
        descriptorWrites: List<VkWriteDescriptorSet>,
        descriptorCopies: List<VkCopyDescriptorSet>) = memScoped {
    vulkan.vkUpdateDescriptorSets(
            device.native,
            descriptorWrites.size.toUInt(),
            descriptorWrites.toNative(this),
            descriptorCopies.size.toUInt(),
            descriptorCopies.toNative(this))
}
