package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun vulkan.VkPhysicalDeviceSparseProperties.toOrigin(): VkPhysicalDeviceSparseProperties {
    return VkPhysicalDeviceSparseProperties(
            residencyStandard2DBlockShape.toBoolean(),
            residencyStandard2DBlockShape.toBoolean(),
            residencyStandard3DBlockShape.toBoolean(),
            residencyAlignedMipSize.toBoolean(),
            residencyNonResidentStrict.toBoolean())
}
