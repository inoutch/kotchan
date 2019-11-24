package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkFramebufferCreateInfo.copyToNative(
    native: vulkan.VkFramebufferCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.renderPass = renderPass.native
    native.attachmentCount = attachments.size.toUInt()
    native.pAttachments = attachments.toNative(scope)
    native.width = width.toUInt()
    native.height = height.toUInt()
    native.layers = layers.toUInt()
}

@ExperimentalUnsignedTypes
fun VkFramebufferCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkFramebufferCreateInfo>()
                .also { copyToNative(it, scope) }.ptr
