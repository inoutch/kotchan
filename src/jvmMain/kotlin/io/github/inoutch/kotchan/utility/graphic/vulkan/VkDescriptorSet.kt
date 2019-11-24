package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.memScoped
import org.lwjgl.vulkan.VK10

actual class VkDescriptorSet : Disposable {
    var native: Long = 0
        private set

    private lateinit var device: VkDevice

    private lateinit var descriptorSetPool: VkDescriptorPool

    fun init(nativeDescriptorSet: Long, device: VkDevice, descriptorSetPool: VkDescriptorPool) {
        this.native = nativeDescriptorSet
        this.device = device
        this.descriptorSetPool = descriptorSetPool
    }

    override fun dispose() {
        VK10.vkFreeDescriptorSets(device.native, native, descriptorSetPool.native)
    }
}

actual fun vkAllocateDescriptorSets(device: VkDevice, allocateInfo: VkDescriptorSetAllocateInfo) = memScoped {
    val native = allocLong(allocateInfo.descriptorSetCount)

    checkError(VK10.vkAllocateDescriptorSets(device.native, allocateInfo.toNative(this), native))

    List(allocateInfo.descriptorSetCount) {
        VkDescriptorSet().apply { init(native.get(it), device, allocateInfo.descriptorPool) }
    }
}

actual fun vkUpdateDescriptorSets(
    device: VkDevice,
    descriptorWrites: List<VkWriteDescriptorSet>,
    descriptorCopies: List<VkCopyDescriptorSet>
) = memScoped {
    VK10.vkUpdateDescriptorSets(
            device.native,
            descriptorWrites.toNative(this),
            descriptorCopies.toNative(this))
}
