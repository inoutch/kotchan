package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkShaderModule
import io.github.inoutch.kotlin.vulkan.api.vk

class VKShaderModule(
        val device: VKLogicalDevice,
        val shaderModule: VkShaderModule
) : Disposer() {
    init {
        add { vk.destroyShaderModule(device.device, shaderModule) }
    }
}