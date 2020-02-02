package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferBundle
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.extension.getProperties
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferBeginInfo
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkExtent2D
import io.github.inoutch.kotlin.vulkan.api.VkInstance
import io.github.inoutch.kotlin.vulkan.api.VkInstanceCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkPhysicalDevice
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkPresentInfoKHR
import io.github.inoutch.kotlin.vulkan.api.VkResult
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.VkSubmitInfo
import io.github.inoutch.kotlin.vulkan.api.VkSurface
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.extension.forEachIndexes
import io.github.inoutch.kotlin.vulkan.utility.MutableProperty

class VKContext(
    instanceCreateInfo: VkInstanceCreateInfo,
    private var windowSize: Vector2I,
    private val maxFrameInFlight: Int = 3,
    createSurface: (surface: MutableProperty<VkSurface>, instance: VkInstance) -> VkResult
) : Context, Disposer() {
    val instance: VkInstance

    val surface: VkSurface

    val physicalDevices: List<VKPhysicalDevice>

    val primaryPhysicalDevice: VKPhysicalDevice

    val primaryLogicalDevice: VKLogicalDevice

    val primaryGraphicCommandPool: VKCommandPool

    val swapchainRecreator: VKSwapchainRecreator

    var currentFrame = 0
        private set

    var currentSwapchainImageIndex = 0
        private set

    private val imageAvailableSemaphores: List<VKSemaphore>

    private val renderCompleteSemaphores: List<VKSemaphore>

    private val inFlightFences: List<VKFence>

    private var mustRecreateSwapchainInFrame = false

    private var currentCmd: ((commandBuffer: VKCommandBuffer, framebuffer: VKFramebuffer, swapchainImage: VKImage) -> Unit)? = null

    private val currentCmdQueue = mutableListOf<(commandBuffer: VKCommandBuffer, framebuffer: VKFramebuffer, swapchainImage: VKImage) -> Unit>()

    init {
        try {
            instance = getProperty { vk.createInstance(instanceCreateInfo, it).value }
            add { vk.destroyInstance(instance) }

            surface = getProperty { createSurface(it, instance).value }
            // TODO: Reserve to destroy surface

            physicalDevices = getProperties<VkPhysicalDevice> { vk.enumeratePhysicalDevices(instance, it).value }
                    .map { add(VKPhysicalDevice(it, surface)) }
            primaryPhysicalDevice = physicalDevices.first()
            primaryLogicalDevice = add(primaryPhysicalDevice.createDevice(
                    emptyList(), // TODO: Allow arbitrary values ​​to be passed
                    listOf("VK_KHR_swapchain")
            ))

            primaryGraphicCommandPool = add(primaryLogicalDevice.primaryGraphicQueue.createCommandPool())

            swapchainRecreator = add(VKSwapchainRecreator(
                    surface,
                    primaryLogicalDevice,
                    primaryGraphicCommandPool,
                    VkExtent2D(windowSize.x, windowSize.y)
            ))

            imageAvailableSemaphores = List(swapchainRecreator.current.swapchainImages.size) {
                add(primaryLogicalDevice.createSemaphore())
            }
            renderCompleteSemaphores = List(swapchainRecreator.current.swapchainImages.size) {
                add(primaryLogicalDevice.createSemaphore())
            }
            inFlightFences = List(swapchainRecreator.current.swapchainImages.size) {
                add(primaryLogicalDevice.createFence())
            }
        } catch (e: Error) {
            dispose()
            throw e
        }
    }

    override fun begin() {
        // Do noting
    }

    override fun end() {
        submit { commandBuffer, framebuffer, swapchainImage ->
            currentCmdQueue.forEach { it.invoke(commandBuffer, framebuffer, swapchainImage) }
        }
        currentCmdQueue.clear()

        present()
        currentFrame = (currentFrame + 1) % maxFrameInFlight
    }

    override fun resize(windowSize: Vector2I) {
        this.windowSize = windowSize
        mustRecreateSwapchainInFrame = true
    }

    override fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer {
        return VKVertexBuffer(primaryLogicalDevice, primaryGraphicCommandPool, vertices, bufferStorageMode)
    }

    override fun drawTriangles(batchBufferBundle: BatchBufferBundle) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTexture(image: Image): Texture {
        return VKTexture(primaryLogicalDevice, primaryGraphicCommandPool, image)
    }

    private fun present() {
        val currentRenderCompleteSemaphore = renderCompleteSemaphores[currentFrame]
        val presentInfo = VkPresentInfoKHR(
                VkStructureType.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR,
                listOf(currentRenderCompleteSemaphore.semaphore),
                listOf(swapchainRecreator.current.swapchain.swapchain),
                listOf(currentSwapchainImageIndex),
                null
        )
        val queuePresentResult = primaryLogicalDevice.primaryPresentQueue.queuePresentKHR(presentInfo)
        if (queuePresentResult == VkResult.VK_ERROR_OUT_OF_DATE_KHR || queuePresentResult == VkResult.VK_SUBOPTIMAL_KHR) {
            mustRecreateSwapchainInFrame = true
        }
    }

    private fun submit(
        cmdScope: ((
            commandBuffer: VKCommandBuffer,
            framebuffer: VKFramebuffer,
            swapchainImage: VKImage
        ) -> Unit)? = null
    ) {
        val currentCmd = this.currentCmd
        if (currentCmd == null && cmdScope == null) {
            // Do noting
            return
        }

        if (mustRecreateSwapchainInFrame) {
            primaryLogicalDevice.primaryGraphicQueue.queueWaitIdle()
            swapchainRecreator.recreate(VkExtent2D(windowSize.x, windowSize.y))
            mustRecreateSwapchainInFrame = false
            currentCmd?.let { cmd(it) }
        }

        val currentInFlightFence = inFlightFences[currentFrame]
        val currentImageAvailableSemaphore = imageAvailableSemaphores[currentFrame]
        val currentRenderCompleteSemaphore = renderCompleteSemaphores[currentFrame]

        primaryLogicalDevice.waitForFences(listOf(currentInFlightFence))
        primaryLogicalDevice.resetFences(listOf(currentInFlightFence))

        currentSwapchainImageIndex = swapchainRecreator.current.swapchain
                .acquireNextImageKHR(currentImageAvailableSemaphore, null)

        val current = swapchainRecreator.current
        val currentCommandBuffer = current.commandBuffers[currentSwapchainImageIndex]
        val currentFramebuffer = current.framebuffers[currentSwapchainImageIndex]
        val currentSwapchainImage = current.swapchainImages[currentSwapchainImageIndex]

        if (cmdScope != null) {
            currentCommandBuffer.resetCommandBuffer()
            cmd(currentCommandBuffer, currentFramebuffer, currentSwapchainImage, cmdScope)
        }

        val submitInfo = VkSubmitInfo(
                VkStructureType.VK_STRUCTURE_TYPE_SUBMIT_INFO,
                listOf(currentImageAvailableSemaphore.semaphore),
                listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                listOf(currentCommandBuffer.commandBuffer),
                listOf(currentRenderCompleteSemaphore.semaphore)
        )

        primaryLogicalDevice.primaryGraphicQueue.queueSubmit(listOf(submitInfo), currentInFlightFence)

        currentFrame = (currentFrame + 1) % maxFrameInFlight
    }

    private fun cmd(scope: (commandBuffer: VKCommandBuffer, framebuffer: VKFramebuffer, swapchainImage: VKImage) -> Unit) {
        currentCmd = scope
        swapchainRecreator.current.swapchainImages.size.forEachIndexes {
            cmd(
                    swapchainRecreator.current.commandBuffers[it],
                    swapchainRecreator.current.framebuffers[it],
                    swapchainRecreator.current.swapchainImages[it],
                    scope
            )
        }
    }

    private fun cmd(
        commandBuffer: VKCommandBuffer,
        framebuffer: VKFramebuffer,
        swapchainImage: VKImage,
        scope: (commandBuffer: VKCommandBuffer, framebuffer: VKFramebuffer, swapchainImage: VKImage) -> Unit
    ) {
        val usage = listOf(VkCommandBufferUsageFlagBits.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT)
        val beginInfo = VkCommandBufferBeginInfo(VkStructureType.VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO, usage, null)
        commandBuffer.resetCommandBuffer()
        commandBuffer.beginCommandBuffer(beginInfo)
        scope(commandBuffer, framebuffer, swapchainImage)
        commandBuffer.endCommandBuffer()
    }

    override fun dispose() {
        primaryLogicalDevice.deviceWaitIdle()
        super.dispose()
    }
}
