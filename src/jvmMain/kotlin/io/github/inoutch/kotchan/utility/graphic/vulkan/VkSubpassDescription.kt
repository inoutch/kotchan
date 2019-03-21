package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.MemScope

fun VkSubpassDescription.copyToNative(native: org.lwjgl.vulkan.VkSubpassDescription, scope: MemScope) {
    native.flags(flags)
            .pipelineBindPoint(pipelineBindPoint.value)
            .pInputAttachments(inputAttachments.toNative(scope))
            .colorAttachmentCount(colorAttachments.size)
            .pColorAttachments(colorAttachments.toNative(scope))
            .pResolveAttachments(resolveAttachments.toNative(scope))
            .pDepthStencilAttachment(depthStencilAttachment?.toNative(scope))
            .pPreserveAttachments(preserveAttachments.toIntArray().toNative(scope))
}

fun List<VkSubpassDescription>.toNative(memScope: MemScope): org.lwjgl.vulkan.VkSubpassDescription.Buffer =
        memScope.add(org.lwjgl.vulkan.VkSubpassDescription.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], memScope) } })
