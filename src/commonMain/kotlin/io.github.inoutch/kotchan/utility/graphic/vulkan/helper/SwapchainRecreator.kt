package io.github.inoutch.kotchan.utility.graphic.vulkan.helper

import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.type.Point

class SwapchainRecreator(
        private val vk: VK,
        newExtent: Point,
        private val swapchainSupportDetails: SwapchainSupportDetails,
        private val deviceQueueFamilyIndices: DeviceQueueFamilyIndices) : Disposable {

    data class ResultBundle(
            val swapchainKHR: VkSwapchainKHR,
            val framebuffers: List<VkFramebuffer>,
            val commandBuffers: List<VkCommandBuffer>,
            val depthResources: List<DepthResource>)

    var mustBeRecreate = false

    var extent: Point
        private set

    var currentSwapchain: VkSwapchainKHR
        private set

    var commandBuffers: List<VkCommandBuffer>
        private set

    var framebuffers: List<VkFramebuffer>
        private set

    private val surfaceFormat = swapchainSupportDetails.chooseSwapSurfaceFormat()

    var swapchainImages: List<VkImage> = listOf()
        private set

    private var swapchainImageViews: List<VkImageView> = listOf()

    var depthResources: List<DepthResource>
        private set

    init {
        extent = swapchainSupportDetails.chooseSwapExtent(newExtent)

        val result = recreate(newExtent, true)
        currentSwapchain = result.swapchainKHR
        framebuffers = result.framebuffers
        commandBuffers = result.commandBuffers
        depthResources = result.depthResources
    }

    fun recreate(newExtent: Point, firstInit: Boolean = false): ResultBundle {
        // prepare
        this.extent = swapchainSupportDetails.chooseSwapExtent(newExtent)

        // about swapchain
        val swapchain = createSwapchain(currentSwapchain, extent)

        // create image views
        swapchainImages = vkGetSwapchainImagesKHR(vk.device, swapchain)
        swapchainImageViews = swapchainImages.map {
            createImageView(it, surfaceFormat.format, listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT))
        }

        val depthResources = swapchainImageViews.map { createDepthResources(this.extent) }

        // create frame buffers
        val framebuffers = swapchainImageViews.mapIndexed { index, view ->
            createFramebuffer(view, depthResources[index].depthImageView, extent)
        }

        // create command buffers
        val commandBuffers = createRenderCommandBuffer(vk.commandPool, framebuffers)

        if (!firstInit) {
            this.currentSwapchain.dispose()
            this.commandBuffers.forEach { it.dispose() }
            this.framebuffers.forEach { it.dispose() }

            this.currentSwapchain = swapchain
            this.commandBuffers = commandBuffers
            this.framebuffers = framebuffers
        }
        mustBeRecreate = false
        return ResultBundle(swapchain, framebuffers, commandBuffers, depthResources)
    }

    override fun dispose() {
        depthResources.forEach {
            it.depthImage.dispose()
            it.depthImageView.dispose()
            it.depthImageMemory.dispose()
        }

        commandBuffers.forEach { it.dispose() }
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

    private fun createImageView(image: VkImage, surfaceFormat: VkFormat, aspectFlags: List<VkImageAspectFlagBits>): VkImageView {
        val createInfo = VkImageViewCreateInfo(
                0,
                image,
                VkImageViewType.VK_IMAGE_VIEW_TYPE_2D,
                surfaceFormat,
                VkComponentMapping(
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY, // use default color mapping
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY,
                        VkComponentSwizzle.VK_COMPONENT_SWIZZLE_IDENTITY),
                VkImageSubresourceRange(aspectFlags, 0, 1, 0, 1))

        return vkCreateImageView(vk.device, createInfo)
    }

    private fun createFramebuffer(swapchainImageView: VkImageView, depthImageView: VkImageView, extent: Point)
            : VkFramebuffer {
        val createInfo = VkFramebufferCreateInfo(
                0,
                vk.renderPass,
                listOf(swapchainImageView, depthImageView),
                extent.x, extent.y, 1)

        return vkCreateFramebuffer(vk.device, createInfo)
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

    data class DepthResource(val depthImage: VkImage,
                             val depthImageMemory: VkDeviceMemory,
                             val depthImageView: VkImageView)

    private fun createDepthResources(extent: Point): DepthResource {
        val depthFormat = vk.findDepthFormat()
        val depthImage = Helper.createImage(
                vk.device,
                extent,
                depthFormat,
                VkImageTiling.VK_IMAGE_TILING_OPTIMAL,
                listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_DEPTH_STENCIL_ATTACHMENT_BIT))
        val imageMemoryBundle = vk.createImageMemory(
                vk.device,
                depthImage,
                listOf(VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT))
        vkBindImageMemory(vk.device, depthImage, imageMemoryBundle.first, 0)
        val ret = createImageView(depthImage, depthFormat, listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_DEPTH_BIT))

        vk.transitionImageLayout(depthImage, depthFormat,
                VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL,
                null)
        return DepthResource(depthImage, imageMemoryBundle.first, ret)
    }
}
