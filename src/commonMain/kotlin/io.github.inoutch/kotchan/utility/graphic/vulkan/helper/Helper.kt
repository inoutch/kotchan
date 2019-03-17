package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.core.graphic.DeviceQueueFamilyIndices
import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector4

class Helper {
    companion object {
        fun createDevice(
                physicalDevice: VkPhysicalDevice,
                deviceQueueFamilyIndices: DeviceQueueFamilyIndices,
                deviceLayerNames: List<String>,
                deviceExtensionNames: List<String>): VkDevice {
            val graphicsQueueCreateInfo = VkDeviceQueueCreateInfo(0, deviceQueueFamilyIndices.graphicsQueueFamilyIndex)
            val presentQueueCreateInfo = VkDeviceQueueCreateInfo(0, deviceQueueFamilyIndices.presentQueueFamilyIndex)

            val deviceCreateInfo = VkDeviceCreateInfo(
                    0,
                    listOf(graphicsQueueCreateInfo, presentQueueCreateInfo),
                    deviceLayerNames,
                    deviceExtensionNames,
                    null)

            return vkCreateDevice(physicalDevice, deviceCreateInfo)
        }

        fun createRenderPass(
                device: VkDevice,
                surfaceFormat: VkSurfaceFormatKHR,
                depthFormat: VkFormat): VkRenderPass {
            val colorAttachment = VkAttachmentDescription(
                    0,
                    surfaceFormat.format,
                    listOf(VkSampleFlagBits.VK_SAMPLE_COUNT_1_BIT),
                    VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_CLEAR,
                    VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_STORE,
                    VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_DONT_CARE,
                    VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_DONT_CARE,
                    initialLayout = VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                    finalLayout = VkImageLayout.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR)

            val depthAttachment = VkAttachmentDescription(
                    0,
                    depthFormat,
                    listOf(VkSampleFlagBits.VK_SAMPLE_COUNT_1_BIT),
                    VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_CLEAR,
                    VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_STORE,
                    VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_DONT_CARE,
                    VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_DONT_CARE,
                    initialLayout = VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                    finalLayout = VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_ATTACHMENT_STENCIL_READ_ONLY_OPTIMAL)

            val colorReference = VkAttachmentReference(0, VkImageLayout.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL)

            val depthAttachmentRef = VkAttachmentReference(
                    1,
                    VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL)

            val subpass = VkSubpassDescription(
                    0,
                    VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                    listOf(),
                    listOf(colorReference),
                    listOf(),
                    depthAttachmentRef,
                    listOf())

            val dependency = VkSubpassDependency(
                    0.inv(),
                    0,
                    listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                    listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                    listOf(),
                    listOf(VkAccessFlagBits.VK_ACCESS_COLOR_ATTACHMENT_READ_BIT,
                            VkAccessFlagBits.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT),
                    listOf())

            val renderCreateInfo = VkRenderPassCreateInfo(
                    0,
                    listOf(colorAttachment, depthAttachment),
                    listOf(subpass),
                    listOf(dependency),
                    listOf())

            return vkCreateRenderPass(device, renderCreateInfo)
        }

        fun createPipelineLayout(device: VkDevice): VkPipelineLayout {
            val pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo(
                    0,
                    listOf(),
                    listOf())

            return vkCreatePipelineLayout(device, pipelineLayoutCreateInfo)
        }

        fun createGraphicsPipeline(
                device: VkDevice,
                renderPass: VkRenderPass,
                pipelineLayout: VkPipelineLayout,
                vertShaderModule: VkShaderModule,
                fragShaderModule: VkShaderModule): VkPipeline {

            val shaderStages = createShaderStages(vertShaderModule, fragShaderModule)

            val vertexInputState = create2DVertexInputState()

            val inputAssemblyStateCreateInfo = VkPipelineInputAssemblyStateCreateInfo(
                    0,
                    VkPrimitiveTopology.VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST,
                    false)

            val viewportStateCreateInfo = VkPipelineViewportStateCreateInfo(
                    0,
                    1,
                    listOf(),
                    1,
                    listOf())

            val rasterizationStateCreateInfo = VkPipelineRasterizationStateCreateInfo(
                    0,
                    VkPolygonMode.VK_POLYGON_MODE_FILL,
                    VkCullMode.VK_CULL_MODE_NONE,
                    VkFrontFace.VK_FRONT_FACE_COUNTER_CLOCKWISE,
                    false,
                    false,
                    false,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f)

            val multisampleStencilOpState = VkPipelineMultisampleStateCreateInfo(
                    0,
                    listOf(VkSampleCountFlagBits.VK_SAMPLE_COUNT_1_BIT),
                    false,
                    0.0f,
                    null,
                    alphaToCoverageEnable = false,
                    alphaToOneEnable = false)

            val stencilOpState = VkStencilOpState(
                    VkStencilOp.VK_STENCIL_OP_KEEP,
                    VkStencilOp.VK_STENCIL_OP_KEEP,
                    VkStencilOp.VK_STENCIL_OP_KEEP,
                    VkCompareOp.VK_COMPARE_OP_ALWAYS,
                    0,
                    0,
                    0)
            val depthStencilStateCreateInfo = VkPipelineDepthStencilStateCreateInfo(
                    0,
                    true,
                    true,
                    VkCompareOp.VK_COMPARE_OP_LESS,
                    false,
                    false,
                    stencilOpState,
                    stencilOpState,
                    0.0f,
                    1.0f)

            val colorWriteMask = VkPipelineColorBlendAttachmentState(
                    true,
                    VkBlendFactor.VK_BLEND_FACTOR_SRC_ALPHA,
                    VkBlendFactor.VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA,
                    VkBlendOp.VK_BLEND_OP_ADD,
                    VkBlendFactor.VK_BLEND_FACTOR_ONE,
                    VkBlendFactor.VK_BLEND_FACTOR_ZERO,
                    VkBlendOp.VK_BLEND_OP_ADD,
                    listOf(VkColorComponentFlagBits.VK_COLOR_COMPONENT_R_BIT,
                            VkColorComponentFlagBits.VK_COLOR_COMPONENT_G_BIT,
                            VkColorComponentFlagBits.VK_COLOR_COMPONENT_B_BIT,
                            VkColorComponentFlagBits.VK_COLOR_COMPONENT_A_BIT))
            val colorBlendStateCreateInfo = VkPipelineColorBlendStateCreateInfo(
                    0,
                    false,
                    VkLogicOp.VK_LOGIC_OP_CLEAR,
                    listOf(colorWriteMask),
                    Vector4.Zero)

            val dynamicStateCreateInfo = VkPipelineDynamicStateCreateInfo(
                    0,
                    listOf(VkDynamicState.VK_DYNAMIC_STATE_VIEWPORT,
                            VkDynamicState.VK_DYNAMIC_STATE_SCISSOR))

            val pipelineCreateInfo = VkGraphicsPipelineCreateInfo(
                    0,
                    shaderStages,
                    vertexInputState,
                    inputAssemblyStateCreateInfo,
                    null,
                    viewportStateCreateInfo,
                    rasterizationStateCreateInfo,
                    multisampleStencilOpState,
                    depthStencilStateCreateInfo,
                    colorBlendStateCreateInfo,
                    dynamicStateCreateInfo,
                    pipelineLayout,
                    renderPass,
                    0,
                    null,
                    null)

            return vkCreateGraphicsPipelines(device, null, listOf(pipelineCreateInfo))
        }

        fun createImage(device: VkDevice,
                        size: Point,
                        format: VkFormat,
                        tiling: VkImageTiling,
                        usage: List<VkImageUsageFlagBits>): VkImage {

            val createInfo = VkImageCreateInfo(
                    0,
                    VkImageType.VK_IMAGE_TYPE_2D,
                    VkExtent3D(size.x, size.y, 1),
                    format,
                    1,
                    1,
                    VkSampleCountFlagBits.VK_SAMPLE_COUNT_1_BIT,
                    tiling,
                    usage,
                    VkSharingMode.VK_SHARING_MODE_EXCLUSIVE,
                    listOf(),
                    VkImageLayout.VK_IMAGE_LAYOUT_PREINITIALIZED)

            return vkCreateImage(device, createInfo)
        }

        fun createImageView(device: VkDevice, image: VkImage, format: VkFormat): VkImageView {
            val createInfo = VkImageViewCreateInfo(
                    0,
                    image,
                    VkImageViewType.VK_IMAGE_VIEW_TYPE_2D,
                    format,
                    VkComponentMapping(
                            VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                            VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                            VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                            VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY),
                    VkImageSubresourceRange(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT),
                            0, 1, 0, 1))
            return vkCreateImageView(device, createInfo)
        }

        fun createSampler(device: VkDevice, magFilter: VkFilter, minFilter: VkFilter): VkSampler {
            val createInfo = VkSamplerCreateInfo(
                    0, magFilter, minFilter,
                    VkSamplerMipmapMode.VK_SAMPLER_MIPMAP_MODE_LINEAR,
                    VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_REPEAT,
                    VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_REPEAT,
                    VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_REPEAT,
                    0.0f, false, 1.0f,
                    false, VkCompareOp.VK_COMPARE_OP_ALWAYS,
                    0.0f, 0.0f, VkBorderColor.VK_BORDER_COLOR_FLOAT_OPAQUE_BLACK, false)
            return vkCreateSampler(device, createInfo)
        }
    }
}
