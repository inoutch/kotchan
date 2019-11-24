package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.*
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.DeviceQueueFamilyIndices
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.SwapchainSupportDetails
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Version

class VK(
    appName: String,
    private val actualWindowSize: Point,
    physicalDeviceLayerNames: List<String>,
    physicalDeviceExtensionNames: List<String>,
    deviceLayerNames: List<String>,
    deviceExtensionNames: List<String>,
    surfaceCreateCallback: (instance: VkInstance) -> VkSurface
) : Disposable {

    companion object {
        const val MAX_FRAMES_IN_FLIGHT = 2
    }

    val physicalDevice: VkPhysicalDevice

    val device: VkDevice

    val physicalDeviceMemoryProperties: VkPhysicalDeviceMemoryProperties

    val swapchainSupportDetails: SwapchainSupportDetails

    val commandPool: VkCommandPool

    val swapchainRecreator: SwapchainRecreator

    val surface: VkSurface

    val queue: VkQueue

    val renderPass: VkRenderPass

    var currentCommandBuffer: VkCommandBuffer
        private set

    var currentFrameBuffer: VkFramebuffer
        private set

    val commandBuffers: List<VkCommandBuffer>
        get() = swapchainRecreator.commandBuffers

    var currentImageIndex = 0
        private set

    private val instance: VkInstance

    private val physicalDevices: List<VkPhysicalDevice>

    private val deviceQueueFamilyIndices: DeviceQueueFamilyIndices

    private val surfaceFormat: VkSurfaceFormatKHR

    private val imageAvailableSemaphores: List<VkSemaphore>

    private val renderCompleteSemaphores: List<VkSemaphore>

    private var currentFrame = 0

    private val inFlightFences: List<VkFence>

    init {
        val applicationInfo = VkApplicationInfo(
                appName,
                Version(0, 0, 1),
                appName,
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
        device = Helper.createDevice(physicalDevice, deviceQueueFamilyIndices, deviceLayerNames, deviceExtensionNames)

        swapchainSupportDetails = SwapchainSupportDetails.querySwapchainSupport(physicalDevice, surface)
        surfaceFormat = swapchainSupportDetails.chooseSwapSurfaceFormat()

        queue = vkGetDeviceQueue(device, deviceQueueFamilyIndices.graphicsQueueFamilyIndex, 0)

        renderPass = Helper.createRenderPass(device, surfaceFormat, findDepthFormat())

        commandPool = createCommandPool(device, deviceQueueFamilyIndices.graphicsQueueFamilyIndex)
        swapchainRecreator = SwapchainRecreator(this, actualWindowSize, swapchainSupportDetails, deviceQueueFamilyIndices)
        currentCommandBuffer = swapchainRecreator.commandBuffers.first()
        currentFrameBuffer = swapchainRecreator.framebuffers.first()

        imageAvailableSemaphores = List(MAX_FRAMES_IN_FLIGHT) { vkCreateSemaphore(device, VkSemaphoreCreateInfo(0)) }
        renderCompleteSemaphores = List(MAX_FRAMES_IN_FLIGHT) { vkCreateSemaphore(device, VkSemaphoreCreateInfo(0)) }
        inFlightFences = List(MAX_FRAMES_IN_FLIGHT) {
            vkCreateFence(device, VkFenceCreateInfo(listOf(VkFenceCreateFlagBits.VK_FENCE_CREATE_SIGNALED_BIT)))
        }
    }

    fun begin() {
        if (swapchainRecreator.mustBeRecreate) {
            vkQueueWaitIdle(queue)
            swapchainRecreator.recreate(actualWindowSize)
        }

        vkWaitForFences(device, listOf(inFlightFences[currentFrame]), true, Long.MAX_VALUE)
        vkResetFences(device, listOf(inFlightFences[currentFrame]))

        currentImageIndex = vkAcquireNextImageKHR(
                device,
                swapchainRecreator.currentSwapchain,
                Long.MAX_VALUE,
                imageAvailableSemaphores[currentFrame],
                null)

        currentCommandBuffer = swapchainRecreator.commandBuffers[currentImageIndex]
        currentFrameBuffer = swapchainRecreator.framebuffers[currentImageIndex]

        // compatibility for OpenGL
        vkResetCommandBuffer(currentCommandBuffer, listOf())

        val usage = listOf(VkCommandBufferUsageFlagBits.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT)
        vkBeginCommandBuffer(currentCommandBuffer, VkCommandBufferBeginInfo(usage, null))
    }

    fun end() {
        vkEndCommandBuffer(currentCommandBuffer)

        val submitInfo = VkSubmitInfo(
                listOf(imageAvailableSemaphores[currentFrame]),
                listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                listOf(commandBuffers[currentImageIndex]),
                listOf(renderCompleteSemaphores[currentFrame]))

        vkQueueSubmit(queue, listOf(submitInfo), inFlightFences[currentFrame])

        val presentInfo = VkPresentInfoKHR(
                listOf(renderCompleteSemaphores[currentFrame]),
                listOf(swapchainRecreator.currentSwapchain),
                listOf(currentImageIndex),
                null)

        vkQueuePresentKHR(queue, presentInfo)
        currentFrame = (currentFrame + 1) % MAX_FRAMES_IN_FLIGHT
    }

    fun waitQueue(disposeScope: () -> Unit) {
        vkEndCommandBuffer(currentCommandBuffer)

        val submitInfo = VkSubmitInfo(
                listOf(),
                listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                listOf(commandBuffers[currentImageIndex]),
                listOf())
        vkQueueSubmit(queue, listOf(submitInfo), null)

        vkQueueWaitIdle(queue)

        disposeScope()

        val usage = listOf(VkCommandBufferUsageFlagBits.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT)
        vkBeginCommandBuffer(currentCommandBuffer, VkCommandBufferBeginInfo(usage, null))
    }

    override fun dispose() {
        vkDeviceWaitIdle(device)

        renderPass.dispose()
        swapchainRecreator.dispose()
        commandPool.dispose()

        imageAvailableSemaphores.forEach { it.dispose() }
        renderCompleteSemaphores.forEach { it.dispose() }
        inFlightFences.forEach { it.dispose() }

        device.dispose()
        instance.dispose()
    }

    fun transitionImageLayout(
        image: VkImage,
        format: VkFormat,
        oldLayout: VkImageLayout,
        newLayout: VkImageLayout,
        currentCommandBuffer: VkCommandBuffer?
    ) {
        val srcAccessMask: List<VkAccessFlagBits>
        val dstAccessMask: List<VkAccessFlagBits>
        val sourceStage: List<VkPipelineStageFlagBits>
        val destinationStage: List<VkPipelineStageFlagBits>
        val aspectMask = mutableListOf<VkImageAspectFlagBits>()

        if (newLayout == VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL) {
            aspectMask.add(VkImageAspectFlagBits.VK_IMAGE_ASPECT_DEPTH_BIT)
            if (hasStencilComponent(format)) {
                aspectMask.add(VkImageAspectFlagBits.VK_IMAGE_ASPECT_STENCIL_BIT)
            }
        } else {
            aspectMask.add(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT)
        }

        if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL) {
            srcAccessMask = listOf()
            dstAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_TRANSFER_WRITE_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TRANSFER_BIT)
        } else if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL) {
            srcAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_TRANSFER_WRITE_BIT)
            dstAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_SHADER_READ_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TRANSFER_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT)
        } else if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL) {
            srcAccessMask = listOf()
            dstAccessMask = listOf(VkAccessFlagBits.VK_ACCESS_SHADER_READ_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT)
        } else if (oldLayout == VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED &&
                newLayout == VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL) {
            srcAccessMask = listOf()
            dstAccessMask = listOf(
                    VkAccessFlagBits.VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_READ_BIT,
                    VkAccessFlagBits.VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_WRITE_BIT)

            sourceStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT)
            destinationStage = listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_EARLY_FRAGMENT_TESTS_BIT)
        } else {
            throw Error("unsupported layout transition")
        }

        val barrier = VkImageMemoryBarrier(
                srcAccessMask,
                dstAccessMask,
                oldLayout,
                newLayout,
                VK_QUEUE_FAMILY_IGNORED,
                VK_QUEUE_FAMILY_IGNORED,
                image,
                VkImageSubresourceRange(aspectMask, 0, 1, 0, 1))

        submitSingleCommandBuffer(currentCommandBuffer) {
            vkCmdPipelineBarrier(
                    it, sourceStage, destinationStage,
                    listOf(), listOf(), listOf(), listOf(barrier))
        }
    }

    fun copyImageBuffer(size: Point, buffer: VkBuffer, image: VkImage) {
        val region = VkBufferImageCopy(
                0, 0, 0,
                VkImageSubresourceLayers(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT), 0, 0, 1),
                VkOffset3D(0, 0, 0),
                VkExtent3D(size.x, size.y, 1))
        vkCmdCopyBufferToImage(
                currentCommandBuffer,
                buffer,
                image,
                VkImageLayout.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL,
                listOf(region))
    }

    fun createDescriptorSets(
        device: VkDevice,
        descriptorPool: VkDescriptorPool,
        size: Int,
        layout: VkDescriptorSetLayout
    ): List<VkDescriptorSet> {

        val allocateInfo = VkDescriptorSetAllocateInfo(descriptorPool, size, List(size) { layout })
        return vkAllocateDescriptorSets(device, allocateInfo)
    }

    fun findSupportedFormat(
        candidates: List<VkFormat>,
        tiling: VkImageTiling,
        features: List<VkFormatFeatureFlagBits>
    ): VkFormat {
        val featuresValue = features.sumBy { it.value }
        return candidates.find { format ->
            val props = vkGetPhysicalDeviceFormatProperties(physicalDevice, format)
            if (tiling == VkImageTiling.VK_IMAGE_TILING_LINEAR &&
                    (props.linearTilingFeatures.sumBy { it.value } and featuresValue == featuresValue)) {
                return@find true
            } else if (tiling == VkImageTiling.VK_IMAGE_TILING_OPTIMAL &&
                    (props.optimalTilingFeatures.sumBy { it.value } and featuresValue == featuresValue)) {
                return@find true
            }
            return@find false
        } ?: throw Error("failed to find supported format")
    }

    fun findDepthFormat(): VkFormat {
        return findSupportedFormat(
                listOf(VkFormat.VK_FORMAT_D32_SFLOAT,
                        VkFormat.VK_FORMAT_D32_SFLOAT_S8_UINT,
                        VkFormat.VK_FORMAT_D24_UNORM_S8_UINT),
                VkImageTiling.VK_IMAGE_TILING_OPTIMAL,
                listOf(VkFormatFeatureFlagBits.VK_FORMAT_FEATURE_DEPTH_STENCIL_ATTACHMENT_BIT))
    }

    fun hasStencilComponent(format: VkFormat): Boolean {
        return format == VkFormat.VK_FORMAT_D32_SFLOAT_S8_UINT || format == VkFormat.VK_FORMAT_D24_UNORM_S8_UINT
    }

    fun createImageMemory(device: VkDevice, image: VkImage, properties: List<VkMemoryPropertyFlagBits>):
            Pair<VkDeviceMemory, Long> {
        val requirements = vkGetImageMemoryRequirements(device, image)
        val allocateInfo = VkMemoryAllocateInfo(
                requirements.size, findMemoryTypeIndex(requirements.memoryTypeBits, properties))
        return vkAllocateMemory(device, allocateInfo) to allocateInfo.allocationSize
    }

    fun submitSingleCommandBuffer(currentCommandBuffer: VkCommandBuffer?, scope: (commandBuffer: VkCommandBuffer) -> Unit) {
        val commandBuffer = currentCommandBuffer
                ?: VkCommandBufferAllocateInfo(commandPool, 0, 1).let {
                    vkAllocateCommandBuffers(device, it).first()
                }

        val prevBeginInfo = VkCommandBufferBeginInfo(
                listOf(VkCommandBufferUsageFlagBits.VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT), null)

        if (currentCommandBuffer == null) {
            vkBeginCommandBuffer(commandBuffer, prevBeginInfo)
        }

        try {
            scope(commandBuffer)
        } catch (e: Error) {
            if (currentCommandBuffer == null) {
                vkEndCommandBuffer(commandBuffer)
            }
            throw e
        }

        vkEndCommandBuffer(commandBuffer)
        vkQueueSubmit(queue, listOf(VkSubmitInfo(listOf(), listOf(), listOf(commandBuffer), listOf())), null)
        vkQueueWaitIdle(queue)

        if (currentCommandBuffer != null) {
            val nextBeginInfo = VkCommandBufferBeginInfo(
                    listOf(VkCommandBufferUsageFlagBits.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT), null)

            vkResetCommandBuffer(currentCommandBuffer, listOf())
            vkBeginCommandBuffer(currentCommandBuffer, nextBeginInfo)
        }
    }

    fun findMemoryTypeIndex(typeFilter: Int, memoryTypes: List<VkMemoryPropertyFlagBits>): Int {
        val properties = memoryTypes.sumBy { it.value }
        val supportedMemoryTypes = physicalDeviceMemoryProperties.memoryTypes
        for (i in 0 until supportedMemoryTypes.size) {
            val type = supportedMemoryTypes[i]
            if (typeFilter and (1 shl i) != 0 && (type.propertyFlags and properties) == properties) {
                return i
            }
        }
        throw VkInvalidStateError("memoryTypes")
    }

    fun createPipelineLayout(descriptorSets: List<VkDescriptorSetLayout>): VkPipelineLayout {
        val pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo(0, descriptorSets, listOf())
        return vkCreatePipelineLayout(device, pipelineLayoutCreateInfo)
    }

    private fun createCommandPool(device: VkDevice, queryFamilyIndex: Int): VkCommandPool {
        // TODO: type flags
        return vkCreateCommandPool(device, VkCommandPoolCreateInfo(0x00000002, queryFamilyIndex))
    }
}
