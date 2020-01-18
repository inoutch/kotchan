package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkFramebuffer
import io.github.inoutch.kotlin.vulkan.api.vk

class VKFramebuffer(val renderPass: VKRenderPass, val framebuffer: VkFramebuffer): Disposer() {
    init {
        add { vk.destroyFramebuffer(renderPass.logicalDevice.device, framebuffer) }
    }
}
