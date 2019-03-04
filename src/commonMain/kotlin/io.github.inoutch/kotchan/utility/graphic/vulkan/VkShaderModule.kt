package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkShaderModule : Disposable

expect fun vkCreateShaderModule(device: VkDevice, shaderModuleCreateInfo: VkShaderModuleCreateInfo): VkShaderModule
