package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkAttachmentReference.copyToNative(native: vulkan.VkAttachmentReference) {
    native.attachment = attachment.toUInt()
    native.layout = layout.value.toUInt()
}

@ExperimentalUnsignedTypes
fun VkAttachmentReference.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkAttachmentReference>().also { copyToNative(it) }.ptr

@ExperimentalUnsignedTypes
fun List<VkAttachmentReference>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkAttachmentReference>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index]) } }
