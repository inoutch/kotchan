package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkDescriptorPoolCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkDescriptorPoolCreateInfo,
        scope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .maxSets(maxSets)
            .pPoolSizes(poolSizes.toNative(scope))
}

fun VkDescriptorPoolCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkDescriptorPoolCreateInfo =
        scope.add(org.lwjgl.vulkan.VkDescriptorPoolCreateInfo.calloc()
                .also { copyToNative(it, scope) })
