package io.github.inoutch.kotchan.core

import io.github.inoutch.kotchan.core.graphic.DeviceQueueFamilyIndices
import io.github.inoutch.kotchan.core.graphic.SwapchainRecreator
import io.github.inoutch.kotchan.core.graphic.SwapchainSupportDetails
import io.github.inoutch.kotchan.core.shader.triangleFragCode
import io.github.inoutch.kotchan.core.shader.triangleVertCode
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector4
import io.github.inoutch.kotchan.utility.type.Version

class KotchanVk(
        private val config: KotchanEngine.Config,
        private val actualWindowSize: Point,
        private val physicalDeviceLayerNames: List<String>,
        private val physicalDeviceExtensionNames: List<String>,
        private val deviceLayerNames: List<String>,
        private val deviceExtensionNames: List<String>,
        surfaceCreateCallback: (instance: VkInstance) -> VkSurface) : Disposable {

    val instance: VkInstance

    val physicalDevice: VkPhysicalDevice

    val physicalDeviceMemoryProperties: VkPhysicalDeviceMemoryProperties

    val device: VkDevice

    val swapchainSupportDetails: SwapchainSupportDetails

    val swapchainRecreator: SwapchainRecreator

    val surface: VkSurface

    val queue: VkQueue

    val renderPass: VkRenderPass

    val graphicsPipeline: VkPipeline

    private val physicalDevices: List<VkPhysicalDevice>

    private val deviceQueueFamilyIndices: DeviceQueueFamilyIndices

    private val surfaceFormat: VkSurfaceFormatKHR

    // Temporary
    private val vertShaderModule: VkShaderModule

    private val fragShaderModule: VkShaderModule

    private val pipelineLayout: VkPipelineLayout

    private val imageAvailableSemaphore: VkSemaphore

    private val renderCompleteSemaphore: VkSemaphore

    init {
        val applicationInfo = VkApplicationInfo(
                config.appName,
                Version(0, 0, 1),
                config.appName,
                Version(0, 0, 1),
                Version(1, 0, 3))

        val instanceCreateInfo = VkInstanceCreateInfo(
                0,
                applicationInfo,
                physicalDeviceLayerNames,
                physicalDeviceExtensionNames)
        instance = vkCreateInstance(instanceCreateInfo)

        surface = surfaceCreateCallback(instance)

        // Physical Device Configurations ==============================================================================
        physicalDevices = vkEnumeratePhysicalDevices(instance)
        physicalDevice = physicalDevices.first()

        physicalDeviceMemoryProperties = vkGetPhysicalDeviceMemoryProperties(physicalDevice)

        deviceQueueFamilyIndices = DeviceQueueFamilyIndices.find(physicalDevice, surface)

        // Logical Device Configurations ===============================================================================
        device = createDevice(physicalDevice, deviceQueueFamilyIndices)
        // TODO: check supporting swapchain

        swapchainSupportDetails = SwapchainSupportDetails.querySwapchainSupport(physicalDevice, surface)
        surfaceFormat = swapchainSupportDetails.chooseSwapSurfaceFormat()

        //
        vertShaderModule = createShaderModule(triangleVertCode, device)
        fragShaderModule = createShaderModule(triangleFragCode, device)

        // Command Pool Configurations =================================================================================

        queue = createDeviceQueue(device, deviceQueueFamilyIndices.graphicsQueueFamilyIndex)

        renderPass = createRenderPass(device)

        pipelineLayout = createPipelineLayout()

        graphicsPipeline = createGraphicsPipeline(device, renderPass, pipelineLayout, vertShaderModule, fragShaderModule)

        swapchainRecreator = SwapchainRecreator(this, swapchainSupportDetails, deviceQueueFamilyIndices)

        imageAvailableSemaphore = createSemaphore()
        renderCompleteSemaphore = createSemaphore()
    }

    fun draw() {
        if (swapchainRecreator.mustBeRecreate) {
            swapchainRecreator.recreate(actualWindowSize)
        }

        val swapchain = swapchainRecreator.currentSwapchain ?: return
        val commandBuffers = swapchainRecreator.commandBuffers ?: return

        val currentImageIndex = vkAcquireNextImageKHR(device, swapchain, Long.MAX_VALUE, imageAvailableSemaphore, null)

        val submitInfo = VkSubmitInfo(
                listOf(imageAvailableSemaphore),
                listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                listOf(commandBuffers[currentImageIndex]),
                listOf(renderCompleteSemaphore))

        vkQueueSubmit(queue, listOf(submitInfo), null)

        val presentInfo = VkPresentInfoKHR(
                listOf(renderCompleteSemaphore),
                listOf(swapchain),
                listOf(currentImageIndex),
                null)

        vkQueuePresentKHR(queue, presentInfo)

        vkQueueWaitIdle(queue)
    }

    fun getMemoryTypeIndex(typeFilter: Int, memoryTypes: List<VkMemoryPropertyFlagBits>): Int {
        val properties = memoryTypes.sumBy { it.value }

        for (i in 0 until physicalDeviceMemoryProperties.memoryTypes.size) {
            val type = physicalDeviceMemoryProperties.memoryTypes[i]
            if (typeFilter and (1 shl i) != 0 && (type.propertyFlags and properties) == properties) {
                return i
            }
        }
        throw VkInvalidStateError("memoryTypes")
    }

    override fun dispose() {
        graphicsPipeline.dispose()
        pipelineLayout.dispose()

        vertShaderModule.dispose()
        fragShaderModule.dispose()

        device.dispose()

        instance.dispose()
    }

    private fun createDevice(physicalDevice: VkPhysicalDevice,
                             deviceQueueFamilyIndices: DeviceQueueFamilyIndices): VkDevice {
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

    private fun createDeviceQueue(device: VkDevice, queueFamilyIndex: Int): VkQueue {
        return vkGetDeviceQueue(device, queueFamilyIndex, 0)
    }

    private fun createRenderPass(device: VkDevice): VkRenderPass {
        val attachment = VkAttachmentDescription(
                0,
                surfaceFormat.format,
                listOf(VkSampleFlagBits.VK_SAMPLE_COUNT_1_BIT),
                VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_CLEAR,
                VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_STORE,
                VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_DONT_CARE,
                VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_DONT_CARE,
                initialLayout = VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                finalLayout = VkImageLayout.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR)

        val colorReference = VkAttachmentReference(0, VkImageLayout.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL)

        val subpass = VkSubpassDescription(
                0,
                VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                listOf(),
                listOf(colorReference),
                listOf(),
                null,
                listOf())

        val renderCreateInfo = VkRenderPassCreateInfo(
                0,
                listOf(attachment),
                listOf(subpass),
                listOf(),
                listOf())

        return vkCreateRenderPass(device, renderCreateInfo)
    }

    private fun createGraphicsPipeline(
            device: VkDevice,
            renderPass: VkRenderPass,
            pipelineLayout: VkPipelineLayout,
            vertShaderModule: VkShaderModule,
            fragShaderModule: VkShaderModule): VkPipeline {

        // for shaders
        val vertShaderState = VkPipelineShaderStageCreateInfo(
                0,
                listOf(VkShaderStageFlagBits.VK_SHADER_STAGE_VERTEX_BIT),
                vertShaderModule,
                "main",
                null)
        val fragShaderStage = VkPipelineShaderStageCreateInfo(
                0,
                listOf(VkShaderStageFlagBits.VK_SHADER_STAGE_FRAGMENT_BIT),
                fragShaderModule,
                "main",
                null)
        val shaderStages = listOf(vertShaderState, fragShaderStage)

        // bindings and attribute descriptions
        val bindingDescription = VkVertexInputBindingDescription(
                0,
                2 * 4,
                VkVertexInputRate.VK_VERTEX_INPUT_RATE_VERTEX)

        val attributeDescription = VkVertexInputAttributeDescription(
                0,
                0,
                VkFormat.VK_FORMAT_R32G32_SFLOAT, // vec2
                0)

        val vertexInputState = VkPipelineVertexInputStateCreateInfo(
                0,
                listOf(bindingDescription),
                listOf(attributeDescription))

        //
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
                false,
                false,
                VkCompareOp.VK_COMPARE_OP_ALWAYS,
                false,
                false,
                stencilOpState,
                stencilOpState,
                0.0f,
                0.0f)

        val colorWriteMask = VkPipelineColorBlendAttachmentState(
                false,
                VkBlendFactor.VK_BLEND_FACTOR_ZERO,
                VkBlendFactor.VK_BLEND_FACTOR_ZERO,
                VkBlendOp.VK_BLEND_OP_ADD,
                VkBlendFactor.VK_BLEND_FACTOR_ZERO,
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
                listOf(VkDynamicState.VK_DYNAMIC_STATE_VIEWPORT, VkDynamicState.VK_DYNAMIC_STATE_SCISSOR))

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

    private fun createPipelineLayout(): VkPipelineLayout {
        val pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo(
                0,
                listOf(),
                listOf())

        return vkCreatePipelineLayout(device, pipelineLayoutCreateInfo)
    }

    private fun createShaderModule(code: ByteArray, device: VkDevice): VkShaderModule {
        val createInfo = VkShaderModuleCreateInfo(0, code)

        return vkCreateShaderModule(device, createInfo)
    }

    private fun createSemaphore() = vkCreateSemaphore(device, VkSemaphoreCreateInfo(0))
}
