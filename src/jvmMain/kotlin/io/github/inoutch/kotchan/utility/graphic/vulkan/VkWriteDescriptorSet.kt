package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkWriteDescriptorSet.copyToNative(
        native: org.lwjgl.vulkan.VkWriteDescriptorSet,
        scope: MemScope) {
    val texelBufferViews = texelBufferView.map { it.native }.toLongArray().toNative(scope)
    native.sType(VK10.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
            .pNext(VK10.VK_NULL_HANDLE)
            .dstSet(dstSet.native)
            .dstBinding(dstBinding)
            .dstArrayElement(dstArrayElement)
            .descriptorType(descriptorType.value)
            .pImageInfo(imageInfo.toNative(scope))
            .pBufferInfo(bufferInfo.toNative(scope))
            .pTexelBufferView(if (texelBufferView.isEmpty()) null else texelBufferViews)
}

fun List<VkWriteDescriptorSet>.toNative(scope: MemScope): org.lwjgl.vulkan.VkWriteDescriptorSet.Buffer =
        scope.add(org.lwjgl.vulkan.VkWriteDescriptorSet.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })

