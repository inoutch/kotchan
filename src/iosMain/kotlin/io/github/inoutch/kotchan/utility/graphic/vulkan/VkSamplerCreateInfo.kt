package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_SAMPLER_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkSamplerCreateInfo.copyToNative(native: vulkan.VkSamplerCreateInfo) {
    native.sType = VK_STRUCTURE_TYPE_SAMPLER_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.magFilter = magFilter.value.toUInt()
    native.minFilter = minFilter.value.toUInt()
    native.mipmapMode = mipmapMode.value.toUInt()
    native.addressModeU = addressModeU.value.toUInt()
    native.addressModeV = addressModeV.value.toUInt()
    native.addressModeW = addressModeW.value.toUInt()
    native.mipLodBias = mipLodBias
    native.anisotropyEnable = anisotropyEnable.toVkBool32()
    native.maxAnisotropy = maxAnisotropy
    native.compareEnable = compareEnable.toVkBool32()
    native.compareOp = compareOp.value.toUInt()
    native.minLod = minLod
    native.maxLod = maxLod
    native.borderColor = borderColor.value.toUInt()
    native.unnormalizedCoordinates = unnormalizedCoordinates.toVkBool32()
}

@ExperimentalUnsignedTypes
fun VkSamplerCreateInfo.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkSamplerCreateInfo>()
                .also { copyToNative(it) }.ptr
