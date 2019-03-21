package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPhysicalDeviceSparseProperties(
        val residencyStandard2DBlockShape: Boolean,
        val residencyStandard2DMultisampleBlockShape: Boolean,
        val residencyStandard3DBlockShape: Boolean,
        val residencyAlignedMipSize: Boolean,
        val residencyNonResidentStrict: Boolean)
