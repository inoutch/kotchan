package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.MemScope
import kotlinx.cinterop.*
import vulkan.VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO

@ExperimentalUnsignedTypes
fun VkPipelineShaderStageCreateInfo.copyToNative(
        native: vulkan.VkPipelineShaderStageCreateInfo,
        scope: MemScope) {
    native.sType = VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO
    native.pNext = null
    native.flags = flags.toUInt()
    native.stage = stage.sumBy { it.value }.toUInt()
    native.module = module.native
    native.pName = name.cstr.getPointer(scope)
    native.pSpecializationInfo = specializationInfo?.toNative(scope)
}

@ExperimentalUnsignedTypes
fun List<VkPipelineShaderStageCreateInfo>.toNative(scope: MemScope) =
        if (isEmpty()) null else scope.allocArray<vulkan.VkPipelineShaderStageCreateInfo>(size)
                .also { forEachIndexed { index, x -> x.copyToNative(it[index], scope) } }
