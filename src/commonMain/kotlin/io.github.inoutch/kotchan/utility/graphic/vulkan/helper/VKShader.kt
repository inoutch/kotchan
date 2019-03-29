package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.VK
import io.github.inoutch.kotchan.utility.graphic.vulkan.VkShaderModule

class VKShader(val vk: VK, val vert: VkShaderModule, val frag: VkShaderModule) : Disposable {
    override fun dispose() {
        vk.waitQueue {
            vert.dispose()
            frag.dispose()
        }
    }
}
