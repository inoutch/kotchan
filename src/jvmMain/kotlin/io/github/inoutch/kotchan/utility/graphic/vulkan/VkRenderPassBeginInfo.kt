package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkRenderPassBeginInfo.copyToNative(
    native: org.lwjgl.vulkan.VkRenderPassBeginInfo,
    scope: MemScope
) {
    native.sType(VK10.VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .renderPass(renderPass.native)
            .framebuffer(framebuffer.native)
            .renderArea(renderArea.toNative(scope))
            .pClearValues(clearValues.toNative(scope))
}

fun VkRenderPassBeginInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkRenderPassBeginInfo =
        scope.add(org.lwjgl.vulkan.VkRenderPassBeginInfo.calloc()
                .also { copyToNative(it, scope) })
