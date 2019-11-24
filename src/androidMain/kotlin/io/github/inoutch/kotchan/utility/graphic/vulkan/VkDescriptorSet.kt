package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkDescriptorSet : Disposable {
    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkAllocateDescriptorSets(device: VkDevice, allocateInfo: VkDescriptorSetAllocateInfo): List<VkDescriptorSet> {
    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
}
