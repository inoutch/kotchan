package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotlin.vulkan.api.VkDevice
import io.github.inoutch.kotlin.vulkan.api.VkShaderModule
import io.github.inoutch.kotlin.vulkan.api.VkShaderModuleCreateInfo

class VKDevice private constructor(val device: VkDevice) {
    open class Info {

    }

    companion object {
        fun create(info: Info): VKDevice? {

        }
    }

    fun createShaderModule(shaderModuleCreateInfo: VkShaderModuleCreateInfo): VkShaderModule {

    }
}
