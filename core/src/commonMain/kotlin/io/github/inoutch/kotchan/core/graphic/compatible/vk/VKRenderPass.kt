package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkExtent2D
import io.github.inoutch.kotlin.vulkan.api.VkFramebufferCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkRenderPass
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.vk

class VKRenderPass(
        val logicalDevice: VKLogicalDevice,
        val renderPass: VkRenderPass
): Disposer() {
    init {
        add { vk.destroyRenderPass(logicalDevice.device, renderPass) }
    }

    fun createFramebuffer(
            imageView: VKImageView,
            depthImageView: VKImageView,
            extent: VkExtent2D
    ): VKFramebuffer {
        val createInfo = VkFramebufferCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO,
                emptyList(),
                renderPass,
                listOf(imageView.imageView, depthImageView.imageView),
                extent.width,
                extent.height,
                1 // For VR
        )

        return VKFramebuffer(this, getProperty { vk.createFramebuffer(logicalDevice.device, createInfo, it).value })
                .also { add(it) }
    }
}
