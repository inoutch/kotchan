package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.VKSampler

class Sampler(binding: Int, descriptorName: String) : Descriptor(binding, descriptorName) {

    var vkSampler: VKSampler? = null
    var glSampler: Int? = null

    fun set(texture: Texture) {
        instance.graphicsApi.setSampler(this, texture)
    }

    override fun dispose() {
        // ignore
    }
}
