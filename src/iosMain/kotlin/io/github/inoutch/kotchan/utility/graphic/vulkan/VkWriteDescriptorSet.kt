package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import kotlin.math.max
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET

@ExperimentalUnsignedTypes
fun VkWriteDescriptorSet.copyToNative(
    native: vulkan.VkWriteDescriptorSet,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET
    native.pNext = null
    native.dstSet = dstSet.native
    native.dstBinding = dstBinding.toUInt()
    native.dstArrayElement = dstArrayElement.toUInt()
    native.descriptorType = descriptorType.value.toUInt()
    native.descriptorCount = max(imageInfo.size, max(bufferInfo.size, texelBufferView.size)).toUInt()
    native.pImageInfo = imageInfo.toNative(scope)
    native.pBufferInfo = bufferInfo.toNative(scope)
    native.pTexelBufferView = texelBufferView.map { it.native }.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkWriteDescriptorSet.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkWriteDescriptorSet>()
                .also { copyToNative(it, scope) }.ptr

@ExperimentalUnsignedTypes
fun List<VkWriteDescriptorSet>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkWriteDescriptorSet>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } }
