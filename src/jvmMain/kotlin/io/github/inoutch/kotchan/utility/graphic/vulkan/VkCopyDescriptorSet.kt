package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkCopyDescriptorSet.copyToNative(native: org.lwjgl.vulkan.VkCopyDescriptorSet) {
    native.sType(VK10.VK_STRUCTURE_TYPE_COPY_DESCRIPTOR_SET)
            .pNext(VK10.VK_NULL_HANDLE)
            .srcSet(srcSet.native)
            .srcBinding(srcBinding)
            .srcArrayElement(srcArrayElement)
            .dstSet(dstSet.native)
            .dstBinding(dstBinding)
            .dstArrayElement(dstArrayElement)
            .descriptorCount(descriptorCount)
}

fun List<VkCopyDescriptorSet>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.add(org.lwjgl.vulkan.VkCopyDescriptorSet.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } })
