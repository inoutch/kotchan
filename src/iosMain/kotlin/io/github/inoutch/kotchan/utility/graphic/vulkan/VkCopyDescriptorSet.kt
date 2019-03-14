package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import kotlinx.cinterop.allocArray
import vulkan.VK_STRUCTURE_TYPE_COPY_DESCRIPTOR_SET

@ExperimentalUnsignedTypes
fun VkCopyDescriptorSet.copyToNative(native: vulkan.VkCopyDescriptorSet) {
    native.sType = VK_STRUCTURE_TYPE_COPY_DESCRIPTOR_SET
    native.pNext = null
    native.srcSet = srcSet.native
    native.srcBinding = srcBinding.toUInt()
    native.srcArrayElement = srcArrayElement.toUInt()
    native.dstSet = dstSet.native
    native.dstBinding = dstBinding.toUInt()
    native.dstArrayElement = dstArrayElement.toUInt()
    native.descriptorCount = descriptorCount.toUInt()
}

@ExperimentalUnsignedTypes
fun List<VkCopyDescriptorSet>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkCopyDescriptorSet>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
