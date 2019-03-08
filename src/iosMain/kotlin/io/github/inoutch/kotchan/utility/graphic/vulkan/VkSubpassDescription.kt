package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

@ExperimentalUnsignedTypes
fun VkSubpassDescription.copyToNative(
        native: vulkan.VkSubpassDescription,
        scope: MemScope) {
    native.flags = flags.toUInt()
    native.pipelineBindPoint = pipelineBindPoint.value.toUInt()
    native.inputAttachmentCount = inputAttachments.size.toUInt()
    native.pInputAttachments = inputAttachments.toNative(scope)
    native.colorAttachmentCount = colorAttachments.size.toUInt()
    native.pColorAttachments = colorAttachments.toNative(scope)
    native.pResolveAttachments = resolveAttachments.toNative(scope)
    native.pDepthStencilAttachment = depthStencilAttachment?.toNative(scope)
}

@ExperimentalUnsignedTypes
fun List<VkSubpassDescription>.toNative(scope: MemScope) =
        scope.allocArray<vulkan.VkSubpassDescription>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } }
