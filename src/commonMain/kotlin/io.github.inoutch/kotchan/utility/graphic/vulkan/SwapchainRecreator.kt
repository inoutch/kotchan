package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.core.graphic.DeviceQueueFamilyIndices
import io.github.inoutch.kotchan.core.graphic.SwapchainSupportDetails
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.type.Point

class SwapchainRecreator(
        private val vk: VK,
        private val newExtent: Point,
        private val swapchainSupportDetails: SwapchainSupportDetails,
        private val deviceQueueFamilyIndices: DeviceQueueFamilyIndices) : Disposable {

    data class ResultBundle(
            val swapchainKHR: VkSwapchainKHR,
            val framebuffers: List<VkFramebuffer>,
            val commandBuffers: List<VkCommandBuffer>)

    var mustBeRecreate = false

    var extent: Point = Point()
        private set

    var currentSwapchain: VkSwapchainKHR
        private set

    var commandBuffers: List<VkCommandBuffer>
        private set

    var framebuffers: List<VkFramebuffer>
        private set

    private val surfaceFormat = swapchainSupportDetails.chooseSwapSurfaceFormat()

    private val commandPool: VkCommandPool

    private var swapchainImages: List<VkImage> = listOf()

    private var swapchainImageViews: List<VkImageView> = listOf()

    init {
        commandPool = createCommandPool(vk.device, deviceQueueFamilyIndices.graphicsQueueFamilyIndex)
        val result = recreate(newExtent, true)
        currentSwapchain = result.swapchainKHR
        framebuffers = result.framebuffers
        commandBuffers = result.commandBuffers
    }

    fun recreate(newExtent: Point, firstInit: Boolean = false): ResultBundle {
        // prepare
        this.extent = swapchainSupportDetails.chooseSwapExtent(newExtent)

        // about swapchain
        val swapchain = createSwapchain(currentSwapchain, extent)

        // create image views
        swapchainImages = vkGetSwapchainImagesKHR(vk.device, swapchain)
        swapchainImageViews = swapchainImages.map { createImageView(it) }

        // create frame buffers
        val framebuffers = swapchainImageViews.map { createFramebuffer(it, extent) }

        // create command buffers
        val commandBuffers = createRenderCommandBuffer(commandPool, framebuffers)

        if (!firstInit) {
            this.currentSwapchain.dispose()
            this.commandBuffers.forEach { it.dispose() }
            this.framebuffers.forEach { it.dispose() }

            this.currentSwapchain = swapchain
            this.commandBuffers = commandBuffers
            this.framebuffers = framebuffers
        }
        mustBeRecreate = false
        return ResultBundle(swapchain, framebuffers, commandBuffers)
    }

    override fun dispose() {
        commandPool.dispose()
        framebuffers.forEach { it.dispose() }
        swapchainImageViews.forEach { it.dispose() }
        currentSwapchain.dispose()
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
        // TODO: type flags
        return vkCreateCommandPool(device, VkCommandPoolCreateInfo(0x00000002, queryFamilyIndex))
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
