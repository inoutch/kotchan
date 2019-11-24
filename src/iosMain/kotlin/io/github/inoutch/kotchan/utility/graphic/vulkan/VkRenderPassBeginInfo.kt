package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO

@ExperimentalUnsignedTypes
fun VkRenderPassBeginInfo.copyToNative(
    native: vulkan.VkRenderPassBeginInfo,
    scope: MemScope
) {
    native.sType = VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO
    native.pNext = null
    native.renderPass = renderPass.native
    native.framebuffer = framebuffer.native
    native.renderArea.offset.x = renderArea.offset.x
    native.renderArea.offset.y = renderArea.offset.y
    native.renderArea.extent.width = renderArea.extent.x.toUInt()
    native.renderArea.extent.height = renderArea.extent.y.toUInt()
    native.clearValueCount = clearValues.size.toUInt()
    native.pClearValues = clearValues.toNative(scope)
}

@ExperimentalUnsignedTypes
fun VkRenderPassBeginInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkRenderPassBeginInfo>().also { copyToNative(it, scope) }.ptr
