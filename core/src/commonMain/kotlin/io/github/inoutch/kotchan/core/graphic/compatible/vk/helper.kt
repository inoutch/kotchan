package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.BlendFactor
import io.github.inoutch.kotchan.core.graphic.CullMode
import io.github.inoutch.kotchan.core.graphic.PolygonMode
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.DescriptorSet
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTexture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform
import io.github.inoutch.kotlin.vulkan.api.VkBlendFactor
import io.github.inoutch.kotlin.vulkan.api.VkCullModeFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorSetLayoutBinding
import io.github.inoutch.kotlin.vulkan.api.VkDescriptorType
import io.github.inoutch.kotlin.vulkan.api.VkFormat
import io.github.inoutch.kotlin.vulkan.api.VkPolygonMode
import io.github.inoutch.kotlin.vulkan.api.VkShaderStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkVertexInputAttributeDescription
import io.github.inoutch.kotlin.vulkan.api.VkVertexInputBindingDescription
import io.github.inoutch.kotlin.vulkan.api.VkVertexInputRate
import io.github.inoutch.kotlin.vulkan.constant.FLOAT_SIZE

fun convertToDescriptorSetLayoutBinding(descriptorSet: DescriptorSet): VkDescriptorSetLayoutBinding {
    val type = findDescriptorType(descriptorSet)
    val shaderStageFlagBits = findShaderStageFlag(type)
    return VkDescriptorSetLayoutBinding(descriptorSet.binding, type, 1, listOf(shaderStageFlagBits), null)
}

fun findDescriptorType(descriptorSet: DescriptorSet): VkDescriptorType {
    return when (descriptorSet) {
        is Uniform -> VkDescriptorType.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER
        is UniformTexture -> VkDescriptorType.VK_DESCRIPTOR_TYPE_SAMPLER
        else -> throw IllegalStateException("Invalid descriptor type")
    }
}

fun findShaderStageFlag(type: VkDescriptorType): VkShaderStageFlagBits {
    return when (type) {
        VkDescriptorType.VK_DESCRIPTOR_TYPE_SAMPLER -> VkShaderStageFlagBits.VK_SHADER_STAGE_FRAGMENT_BIT
        else -> VkShaderStageFlagBits.VK_SHADER_STAGE_ALL_GRAPHICS
    }
}

fun CullMode.toVkCullModeFlagBits(): VkCullModeFlagBits {
    return when (this) {
        CullMode.None -> VkCullModeFlagBits.VK_CULL_MODE_NONE
        CullMode.Front -> VkCullModeFlagBits.VK_CULL_MODE_FRONT_BIT
        CullMode.Back -> VkCullModeFlagBits.VK_CULL_MODE_BACK_BIT
        CullMode.FrontAndBack -> VkCullModeFlagBits.VK_CULL_MODE_FRONT_AND_BACK
    }
}

fun PolygonMode.toVkPolygonMode(): VkPolygonMode {
    return when(this) {
        PolygonMode.Fill -> VkPolygonMode.VK_POLYGON_MODE_FILL
        PolygonMode.Line -> VkPolygonMode.VK_POLYGON_MODE_LINE
        PolygonMode.Point -> VkPolygonMode.VK_POLYGON_MODE_POINT
    }
}

fun BlendFactor.toVkBlendFactor(): VkBlendFactor {
    return when (this) {
        BlendFactor.One -> VkBlendFactor.VK_BLEND_FACTOR_ONE
        BlendFactor.Zero -> VkBlendFactor.VK_BLEND_FACTOR_ZERO
        BlendFactor.SrcColor -> VkBlendFactor.VK_BLEND_FACTOR_SRC_COLOR
        BlendFactor.OneMinusSrcColor -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_SRC_COLOR
        BlendFactor.DstColor -> VkBlendFactor.VK_BLEND_FACTOR_DST_COLOR
        BlendFactor.OneMinusDstColor -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_DST_COLOR
        BlendFactor.SrcAlpha -> VkBlendFactor.VK_BLEND_FACTOR_SRC_ALPHA
        BlendFactor.OneMinusSrcAlpha -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA
        BlendFactor.DstAlpha -> VkBlendFactor.VK_BLEND_FACTOR_DST_ALPHA
        BlendFactor.OneMinusDstAlpha -> VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_DST_ALPHA
    }
}

fun create2DVertexBindingDescriptions(): List<VkVertexInputBindingDescription> {
    val inputRate = VkVertexInputRate.VK_VERTEX_INPUT_RATE_VERTEX
    return listOf(
            VkVertexInputBindingDescription(0, 3 * FLOAT_SIZE.toInt(), inputRate), // pos
            VkVertexInputBindingDescription(1, 4 * FLOAT_SIZE.toInt(), inputRate), // col
            VkVertexInputBindingDescription(2, 2 * FLOAT_SIZE.toInt(), inputRate)) // tex
}

fun create2DVertexAttributeDescriptions(): List<VkVertexInputAttributeDescription> {
    return listOf(
            VkVertexInputAttributeDescription(0, 0, VkFormat.VK_FORMAT_R32G32B32_SFLOAT, 0),
            VkVertexInputAttributeDescription(1, 1, VkFormat.VK_FORMAT_R32G32B32A32_SFLOAT, 0),
            VkVertexInputAttributeDescription(2, 2, VkFormat.VK_FORMAT_R32G32_SFLOAT, 0))
}

fun create3DVertexBindingDescriptions(): List<VkVertexInputBindingDescription> {
    val inputRate = VkVertexInputRate.VK_VERTEX_INPUT_RATE_VERTEX
    return listOf(
            VkVertexInputBindingDescription(0, 3 * FLOAT_SIZE.toInt(), inputRate), // pos
            VkVertexInputBindingDescription(1, 4 * FLOAT_SIZE.toInt(), inputRate), // col
            VkVertexInputBindingDescription(2, 2 * FLOAT_SIZE.toInt(), inputRate), // tex
            VkVertexInputBindingDescription(3, 3 * FLOAT_SIZE.toInt(), inputRate)) // nom
}

fun create3DVertexAttributeDescriptions(): List<VkVertexInputAttributeDescription> {
    return listOf(
            VkVertexInputAttributeDescription(0, 0, VkFormat.VK_FORMAT_R32G32B32_SFLOAT, 0),
            VkVertexInputAttributeDescription(1, 1, VkFormat.VK_FORMAT_R32G32B32A32_SFLOAT, 0),
            VkVertexInputAttributeDescription(2, 2, VkFormat.VK_FORMAT_R32G32_SFLOAT, 0),
            VkVertexInputAttributeDescription(3, 3, VkFormat.VK_FORMAT_R32G32B32_SFLOAT, 0))
}
