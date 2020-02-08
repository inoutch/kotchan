package io.github.inoutch.kotchan.core.graphic.compatible.vk

data class VKRenderContext(
        val commandBuffer: VKCommandBuffer,
        val framebuffer: VKFramebuffer,
        val swapchainImage: VKImage,
        val depthResource: VKSwapchainRecreatorDepthResources
)