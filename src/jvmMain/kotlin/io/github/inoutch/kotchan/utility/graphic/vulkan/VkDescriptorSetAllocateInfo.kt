package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkDescriptorSetAllocateInfo.copyToNative(
    native: org.lwjgl.vulkan.VkDescriptorSetAllocateInfo,
    scope: MemScope
) {
    native.sType(VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .descriptorPool(descriptorPool.native)
            .pSetLayouts(setLayouts.map { it.native }.toLongArray().toNative(scope))
}

fun VkDescriptorSetAllocateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkDescriptorSetAllocateInfo =
        scope.add(org.lwjgl.vulkan.VkDescriptorSetAllocateInfo.calloc()
                .also { copyToNative(it, scope) })
