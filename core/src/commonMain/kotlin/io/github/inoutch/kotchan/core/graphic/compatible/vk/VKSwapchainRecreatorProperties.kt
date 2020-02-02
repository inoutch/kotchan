package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkExtent2D

class VKSwapchainRecreatorProperties(
    val extent: VkExtent2D,
    val swapchain: VKSwapchain,
    val swapchainImages: List<VKImage>,
    val swapchainImageViews: List<VKImageView>,
    val renderPass: VKRenderPass,
    val depthResources: List<VKSwapchainRecreatorDepthResources>,
    val framebuffers: List<VKFramebuffer>,
    val commandBuffers: List<VKCommandBuffer>
) : Disposer() {
    init {
        add(swapchain)
//        swapchainImages.forEach { add(it) }
//        swapchainImageViews.forEach { add(it) }
    }
}
