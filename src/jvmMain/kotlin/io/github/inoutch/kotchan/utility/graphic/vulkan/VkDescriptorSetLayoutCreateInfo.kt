package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkDescriptorSetLayoutCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo,
        memScope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pBindings(bindings.toNative(memScope))
}

fun VkDescriptorSetLayoutCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo =
        org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo.calloc().also { copyToNative(it, memScope) }
