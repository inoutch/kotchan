package io.github.inoutch.kotchan.core.graphic

import io.github.inoutch.kotchan.core.KotchanVk
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector4

class SwapchainRecreator(
        private val vk: KotchanVk,
        private val swapchainSupportDetails: SwapchainSupportDetails,
        private val deviceQueueFamilyIndices: DeviceQueueFamilyIndices) : Disposable {

    var mustBeRecreate = true

    var extent: Point = Point()
        private set

    var currentSwapchain: VkSwapchainKHR? = null
        private set

    var commandBuffers: List<VkCommandBuffer>? = null
        private set

    private val surfaceFormat = swapchainSupportDetails.chooseSwapSurfaceFormat()

    private val commandPool: VkCommandPool

    private var framebuffers: List<VkFramebuffer> = listOf()

    private var swapchainImages: List<VkImage> = listOf()

    private var swapchainImageViews: List<VkImageView> = listOf()

    private val vertexBuffer = VertexBuffer(vk, floatArrayOf(-0.5f, -0.5f, 0.5f, -0.5f, 0.0f, 0.5f))

    init {
        commandPool = createCommandPool(vk.device, deviceQueueFamilyIndices.graphicsQueueFamilyIndex)
    }

    fun recreate(newExtent: Point) {
        // prepare
        this.extent = swapchainSupportDetails.chooseSwapExtent(newExtent)

        // about swapchain
        val swapchain = createSwapchain(currentSwapchain, extent)

        // create image views
        swapchainImages = vkGetSwapchainImagesKHR(vk.device, swapchain)
        swapchainImageViews = swapchainImages.map { createImageView(it) }

        // create frame buffers
        framebuffers.forEach { it.dispose() }
        val framebuffers = swapchainImageViews.map { createFramebuffer(it, extent) }

        // create command buffers
        val commandBuffers = createRenderCommandBuffer(commandPool, framebuffers)

        commandBuffers.forEachIndexed { index, commandBuffer ->
            val usage = listOf(VkCommandBufferUsageFlagBits.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT)
            vkBeginCommandBuffer(commandBuffer, VkCommandBufferBeginInfo(usage, null))

            val renderPassBeginInfo = VkRenderPassBeginInfo(
                    vk.renderPass,
                    framebuffers[index],
                    VkRect2D(Point.ZERO, extent),
                    listOf(VkClearValue(Vector4.Zero, VkClearDepthStencilValue(0.0f, 0))))
            vkCmdBeginRenderPass(commandBuffer, renderPassBeginInfo, VkSubpassContents.VK_SUBPASS_CONTENTS_INLINE)

            vkCmdBindPipeline(commandBuffer, VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS, vk.graphicsPipeline)

            vkCmdBindVertexBuffers(commandBuffer, 0, listOf(vertexBuffer.buffer), listOf(0))

            vkCmdSetViewport(commandBuffer,
                    0,
                    listOf(VkViewport(0.0f, 0.0f, extent.x.toFloat(), extent.y.toFloat(), 0.0f, 1.0f)))

            vkCmdSetScissor(commandBuffer, 0, listOf(VkRect2D(Point.ZERO, extent)))

            vkCmdDraw(commandBuffer, 3, 1, 0, 0)

            vkCmdEndRenderPass(commandBuffer)

            vkEndCommandBuffer(commandBuffer)
        }

        this.currentSwapchain = swapchain
        this.commandBuffers = commandBuffers
        this.framebuffers = framebuffers
        mustBeRecreate = false
    }

    override fun dispose() {
        vertexBuffer.dispose()
        framebuffers.forEach { it.dispose() }
        swapchainImageViews.forEach { it.dispose() }
        currentSwapchain?.dispose()
    }

    private fun createSwapchain(
            oldSwapchainKHR: VkSwapchainKHR?,
            extent: Point): VkSwapchainKHR {
        val imageCount = swapchainSupportDetails.chooseImageCount()
        val presentMode = swapchainSupportDetails.chooseSwapPresentMode()

        val imageSharingMode: VkSharingMode
        val queueFamilyIndices: List<Int>

        if (deviceQueueFamilyIndices.graphicsQueueFamilyIndex != deviceQueueFamilyIndices.presentQueueFamilyIndex) {
            imageSharingMode = VkSharingMode.VK_SHARING_MODE_CONCURRENT
            queueFamilyIndices = listOf(
                    deviceQueueFamilyIndices.graphicsQueueFamilyIndex,
                    deviceQueueFamilyIndices.presentQueueFamilyIndex)
        } else {
            imageSharingMode = VkSharingMode.VK_SHARING_MODE_EXCLUSIVE
            queueFamilyIndices = listOf()
        }

        val createInfo = VkSwapchainCreateInfoKHR(
                0,
                vk.surface,
                imageCount,
                surfaceFormat.format,
                surfaceFormat.colorSpace,
                extent,
                1,
                listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT),
                imageSharingMode,
                queueFamilyIndices,
                swapchainSupportDetails.chooseTransform(),
                VkCompositeAlphaFlagBitsKHR.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR,
                presentMode,
                true,
                oldSwapchainKHR)

        return vkCreateSwapchainKHR(vk.device, createInfo)
                .also { oldSwapchainKHR?.dispose() }
    }

    private fun createImageView(image: VkImage): VkImageView {
        val createInfo = VkImageViewCreateInfo(
                0,
                image,
                VkImageViewType.VK_IMAGE_VIEW_TYPE_2D,
                surfaceFormat.format,
                VkComponentMapping(
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY, // use default color mapping
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY),
                VkImageSubresourceRange(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT),
                        0,
                        1,
                        0,
                        1))

        return vkCreateImageView(vk.device, createInfo)
    }

    private fun createFramebuffer(imageView: VkImageView, extent: Point): VkFramebuffer {
        val createInfo = VkFramebufferCreateInfo(
                0,
                vk.renderPass,
                listOf(imageView),
                extent.x,
                extent.y,
                1)

        return vkCreateFramebuffer(vk.device, createInfo)
    }

    private fun createCommandPool(device: VkDevice, queryFamilyIndex: Int): VkCommandPool {
        return vkCreateCommandPool(device, VkCommandPoolCreateInfo(0, queryFamilyIndex))
    }

    private fun createRenderCommandBuffer(
            commandPool: VkCommandPool,
            framebuffers: List<VkFramebuffer>): List<VkCommandBuffer> {
        val allocateInfo = VkCommandBufferAllocateInfo(
                commandPool,
                VkCommandBufferLevel.VK_COMMAND_BUFFER_LEVEL_PRIMARY.level,
                framebuffers.size)

        return vkAllocateCommandBuffers(vk.device, allocateInfo)
    }
}
