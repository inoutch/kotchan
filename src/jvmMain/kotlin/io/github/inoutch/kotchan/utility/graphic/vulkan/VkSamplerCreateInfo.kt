package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkSamplerCreateInfo.toNative(memScope: MemScope): org.lwjgl.vulkan.VkSamplerCreateInfo =
        memScope.add(org.lwjgl.vulkan.VkSamplerCreateInfo.calloc()
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
