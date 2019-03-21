package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkRenderPassCreateInfo.copyToNative(native: org.lwjgl.vulkan.VkRenderPassCreateInfo, memScope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .pAttachments(attachments.toNative(memScope))
            .pSubpasses(subpasses.toNative(memScope))
            .pDependencies(dependencies.toNative(memScope))
}

fun VkRenderPassCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkRenderPassCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkRenderPassCreateInfo.calloc()
                .also { copyToNative(it, memScope) })
