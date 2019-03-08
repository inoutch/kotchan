package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkAttachmentDescription.copyToNative(native: vulkan.VkAttachmentDescription) {
    native.flags = flags.toUInt()
    native.format = format.value.toUInt()
    native.samples = samples.sumBy { it.value }.toUInt()
    native.loadOp = loadOp.value.toUInt()
    native.storeOp = storeOp.value.toUInt()
    native.stencilLoadOp = stencilLoadOp.value.toUInt()
    native.stencilStoreOp = stencilStoreOp.value.toUInt()
    native.initialLayout = initialLayout.value.toUInt()
    native.finalLayout = finalLayout.value.toUInt()
}

@ExperimentalUnsignedTypes
fun VkAttachmentDescription.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkAttachmentDescription>().also { copyToNative(it) }

@ExperimentalUnsignedTypes
fun List<VkAttachmentDescription>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkAttachmentDescription>(size).also {
            forEachIndexed { index, x -> x.copyToNative(it[index]) }
        }
