package io.github.inoutch.kotchan.utility.graphic.vulkan

import kotlinx.cinterop.*

fun VkPhysicalDeviceFeatures.copyToNative(native: vulkan.VkPhysicalDeviceFeatures) {
    native.robustBufferAccess
    native.fullDrawIndexUint32
    native.imageCubeArray
    native.independentBlend
    native.geometryShader
    native.tessellationShader
    native.sampleRateShading
    native.dualSrcBlend
    native.logicOp
    native.multiDrawIndirect
    native.drawIndirectFirstInstance
    native.depthClamp
    native.depthBiasClamp
    native.fillModeNonSolid
    native.depthBounds
    native.wideLines
    native.largePoints
    native.alphaToOne
    native.multiViewport
    native.samplerAnisotropy
    native.textureCompressionETC2
    native.textureCompressionASTC_LDR
    native.textureCompressionBC
    native.occlusionQueryPrecise
    native.pipelineStatisticsQuery
    native.vertexPipelineStoresAndAtomics
    native.fragmentStoresAndAtomics
    native.shaderTessellationAndGeometryPointSize
    native.shaderImageGatherExtended
    native.shaderStorageImageExtendedFormats
    native.shaderStorageImageMultisample
    native.shaderStorageImageReadWithoutFormat
    native.shaderStorageImageWriteWithoutFormat
    native.shaderUniformBufferArrayDynamicIndexing
    native.shaderSampledImageArrayDynamicIndexing
    native.shaderStorageBufferArrayDynamicIndexing
    native.shaderStorageImageArrayDynamicIndexing
    native.shaderClipDistance
    native.shaderCullDistance
    native.shaderFloat64
    native.shaderInt64
    native.shaderInt16
    native.shaderResourceResidency
    native.shaderResourceMinLod
    native.sparseBinding
    native.sparseResidencyBuffer
    native.sparseResidencyImage2D
    native.sparseResidencyImage3D
    native.sparseResidency2Samples
    native.sparseResidency4Samples
    native.sparseResidency8Samples
    native.sparseResidency16Samples
    native.sparseResidencyAliased
    native.variableMultisampleRate
    native.inheritedQueries
}

fun VkPhysicalDeviceFeatures.toNative(scope: MemScope) =
        scope.alloc<vulkan.VkPhysicalDeviceFeatures>().also { copyToNative(it) }.ptr
