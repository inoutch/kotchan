package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO

@ExperimentalUnsignedTypes
fun VkDescriptorSetAllocateInfo.copyToNative(
        native: vulkan.VkDescriptorSetAllocateInfo,
        scope: MemScope) {
    native.sType = VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO
    native.pNext = null
    native.descriptorPool = descriptorPool.native
    native.descriptorSetCount = descriptorSetCount.toUInt()
    native.pSetLayouts = setLayouts.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkDescriptorSetAllocateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkDescriptorSetAllocateInfo>()
                .also { copyToNative(it, scope) }.ptr
