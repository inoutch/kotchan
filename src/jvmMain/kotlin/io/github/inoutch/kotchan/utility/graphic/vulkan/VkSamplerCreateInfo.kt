package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.vulkan.VK10

fun VkSamplerCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkSamplerCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkSamplerCreateInfo.calloc()
                .sType(VK10.VK_STRUCTURE_TYPE_SAMPLER_CREATE_INFO)
                .pNext(VK10.VK_NULL_HANDLE)
                .flags(flags)
                .magFilter(magFilter.value)
                .minFilter(minFilter.value)
                .mipmapMode(mipmapMode.value)
                .addressModeU(addressModeU.value)
                .addressModeV(addressModeV.value)
                .addressModeW(addressModeW.value)
                .mipLodBias(mipLodBias)
                .anisotropyEnable(anisotropyEnable)
                .maxAnisotropy(maxAnisotropy)
                .compareEnable(compareEnable)
                .compareOp(compareOp.value)
                .minLod(minLod)
                .maxLod(maxLod)
                .borderColor(borderColor.value)
                .unnormalizedCoordinates(unnormalizedCoordinates))
