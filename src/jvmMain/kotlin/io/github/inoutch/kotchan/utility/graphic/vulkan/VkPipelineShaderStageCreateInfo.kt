package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope
import org.lwjgl.system.MemoryUtil
import org.lwjgl.vulkan.VK10

fun VkPipelineShaderStageCreateInfo.copyToNative(
        native: org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo,
        memScope: MemScope) {
    native.sType(VK10.VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO)
            .pNext(VK10.VK_NULL_HANDLE)
            .flags(flags)
            .stage(stage.sumBy { it.value })
            .module(module.native)
            .pName(MemoryUtil.memUTF8(name))
            .pSpecializationInfo(specializationInfo?.toNative(memScope))
}

fun VkPipelineShaderStageCreateInfo.toNative(scope: MemScope): org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo =
        scope.add(org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo.calloc()
                .also { copyToNative(it, scope) })

fun List<VkPipelineShaderStageCreateInfo>.toNative(scope: MemScope)
        : org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo.Buffer =
        scope.add(org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo.calloc(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } })
