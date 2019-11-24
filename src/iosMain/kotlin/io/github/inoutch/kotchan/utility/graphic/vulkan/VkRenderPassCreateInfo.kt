package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkRenderPassCreateInfo.copyToNative(
    native: vulkan.VkRenderPassCreateInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.attachmentCount = attachments.size.toUInt()
    native.pAttachments = attachments.toNative(scope)
    native.subpassCount = subpasses.size.toUInt()
    native.pSubpasses = subpasses.toNative(scope)
    native.dependencyCount = dependencies.size.toUInt()
    native.pDependencies = dependencies.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkRenderPassCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkRenderPassCreateInfo>().also { copyToNative(it, scope) }.ptr
