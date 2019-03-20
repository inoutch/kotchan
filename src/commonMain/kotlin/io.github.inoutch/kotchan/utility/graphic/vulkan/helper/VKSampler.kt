package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

class VKSampler(
        private val descriptorSetProvider: DescriptorSetProvider,
        private val binding: Int) {

    fun setTexture(texture: VKTexture) {
        descriptorSetProvider.updateSampler(binding, texture)
    }
}
