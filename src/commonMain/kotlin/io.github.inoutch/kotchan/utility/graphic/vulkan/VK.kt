package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.core.graphic.DeviceQueueFamilyIndices
import io.github.inoutch.kotchan.core.graphic.SwapchainSupportDetails
import io.github.inoutch.kotchan.core.shader.triangleFragCode
import io.github.inoutch.kotchan.core.shader.triangleVertCode
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.helper.*
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Version

class VK(appName: String,
         private val actualWindowSize: Point,
         private val physicalDeviceLayerNames: List<String>,
         private val physicalDeviceExtensionNames: List<String>,
         private val deviceLayerNames: List<String>,
         private val deviceExtensionNames: List<String>,
         surfaceCreateCallback: (instance: VkInstance) -> VkSurface) : Disposable {

    companion object {
        const val MAX_FRAMES_IN_FLIGHT = 2
    }

    val device: VkDevice

    val physicalDeviceMemoryProperties: VkPhysicalDeviceMemoryProperties

    val swapchainSupportDetails: SwapchainSupportDetails

    val swapchainRecreator: SwapchainRecreator

    val surface: VkSurface

    val queue: VkQueue

    val renderPass: VkRenderPass

    // vars
    val graphicsPipeline: VkPipeline

    var currentCommandBuffer: VkCommandBuffer
        private set

    var currentFrameBuffer: VkFramebuffer
        private set

    private val commandBuffers: List<VkCommandBuffer>
        get() = swapchainRecreator.commandBuffers

    private val instance: VkInstance

    private val physicalDevice: VkPhysicalDevice

    private val physicalDevices: List<VkPhysicalDevice>

    private val deviceQueueFamilyIndices: DeviceQueueFamilyIndices

    private val surfaceFormat: VkSurfaceFormatKHR

    // Temporary
    private val vertShaderModule: VkShaderModule

    private val fragShaderModule: VkShaderModule

    private val pipelineLayout: VkPipelineLayout

    private val imageAvailableSemaphores: List<VkSemaphore>

    private val renderCompleteSemaphores: List<VkSemaphore>

    private var currentFrame = 0

    private val inFlightFences: List<VkFence>

    private var currentImageIndex = 0

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
        device = createDevice(physicalDevice, deviceQueueFamilyIndices, deviceLayerNames, deviceExtensionNames)

        swapchainSupportDetails = SwapchainSupportDetails.querySwapchainSupport(physicalDevice, surface)
        surfaceFormat = swapchainSupportDetails.chooseSwapSurfaceFormat()

        //
        vertShaderModule = createShaderModule(device, triangleVertCode)
        fragShaderModule = createShaderModule(device, triangleFragCode)

        // Command Pool Configurations =================================================================================

        queue = vkGetDeviceQueue(device, deviceQueueFamilyIndices.graphicsQueueFamilyIndex, 0)

        renderPass = createRenderPass(device, surfaceFormat)

        pipelineLayout = createPipelineLayout(device)

        graphicsPipeline = createGraphicsPipeline(device, renderPass, pipelineLayout, vertShaderModule, fragShaderModule)

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
//        vkDeviceWaitIdle(device)

        imageAvailableSemaphores.forEach { it.dispose() }
        renderCompleteSemaphores.forEach { it.dispose() }
        inFlightFences.forEach { it.dispose() }

        graphicsPipeline.dispose()
        pipelineLayout.dispose()

        vertShaderModule.dispose()
        fragShaderModule.dispose()

        device.dispose()

        instance.dispose()
    }
}
