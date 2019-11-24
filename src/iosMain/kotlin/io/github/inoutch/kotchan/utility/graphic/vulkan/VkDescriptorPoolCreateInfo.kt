package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkDescriptorPoolCreateInfo.copyToNative(
    native: vulkan.VkDescriptorPoolCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO
    native.pNext = null
    native.flags = flags.sumBy { it.value }.toUInt()
    native.maxSets = maxSets.toUInt()
    native.poolSizeCount = poolSizes.size.toUInt()
    native.pPoolSizes = poolSizes.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkDescriptorPoolCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkDescriptorPoolCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
