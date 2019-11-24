package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkDescriptorSetLayoutCreateInfo.copyToNative(
    native: vulkan.VkDescriptorSetLayoutCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.bindingCount = bindings.size.toUInt()
    native.pBindings = bindings.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkDescriptorSetLayoutCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkDescriptorSetLayoutCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
