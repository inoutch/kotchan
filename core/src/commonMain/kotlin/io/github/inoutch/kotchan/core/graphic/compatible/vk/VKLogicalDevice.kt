package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VK_SUBPASS_EXTERNAL
import io.github.inoutch.kotlin.vulkan.api.VkAccessFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkAttachmentDescription
import io.github.inoutch.kotlin.vulkan.api.VkAttachmentLoadOp
import io.github.inoutch.kotlin.vulkan.api.VkAttachmentReference
import io.github.inoutch.kotlin.vulkan.api.VkAttachmentStoreOp
import io.github.inoutch.kotlin.vulkan.api.VkBorderColor
import io.github.inoutch.kotlin.vulkan.api.VkBufferCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkBufferUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkCompareOp
import io.github.inoutch.kotlin.vulkan.api.VkCompositeAlphaFlagBitsKHR
import io.github.inoutch.kotlin.vulkan.api.VkDevice
import io.github.inoutch.kotlin.vulkan.api.VkDeviceSize
import io.github.inoutch.kotlin.vulkan.api.VkExtent2D
import io.github.inoutch.kotlin.vulkan.api.VkExtent3D
import io.github.inoutch.kotlin.vulkan.api.VkFenceCreateFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkFenceCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkFilter
import io.github.inoutch.kotlin.vulkan.api.VkFormat
import io.github.inoutch.kotlin.vulkan.api.VkImageCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout
import io.github.inoutch.kotlin.vulkan.api.VkImageTiling
import io.github.inoutch.kotlin.vulkan.api.VkImageType
import io.github.inoutch.kotlin.vulkan.api.VkImageUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkMemoryAllocateInfo
import io.github.inoutch.kotlin.vulkan.api.VkPipelineBindPoint
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkRenderPassCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkResult
import io.github.inoutch.kotlin.vulkan.api.VkSampleCountFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkSamplerAddressMode
import io.github.inoutch.kotlin.vulkan.api.VkSamplerCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkSamplerMipmapMode
import io.github.inoutch.kotlin.vulkan.api.VkSemaphoreCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkShaderModuleCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkSharingMode
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.VkSubpassDependency
import io.github.inoutch.kotlin.vulkan.api.VkSubpassDescription
import io.github.inoutch.kotlin.vulkan.api.VkSurface
import io.github.inoutch.kotlin.vulkan.api.VkSwapchainCreateInfoKHR
import io.github.inoutch.kotlin.vulkan.api.vk

class VKLogicalDevice(
    val physicalDevice: VKPhysicalDevice,
    val device: VkDevice
) : Disposer() {
    val primaryGraphicQueue: VKQueue by lazy {
        add(VKQueue(
                this,
                getProperty { vk.getDeviceQueue(device, physicalDevice.deviceQueueFamilyIndices.graphicsQueueFamilyIndex, 0, it) },
                physicalDevice.deviceQueueFamilyIndices.graphicsQueueFamilyIndex
        ))
    }

    val primaryPresentQueue: VKQueue by lazy {
        if (physicalDevice.deviceQueueFamilyIndices.graphicsQueueFamilyIndex == physicalDevice.deviceQueueFamilyIndices.presentQueueFamilyIndex) {
            primaryGraphicQueue
        } else {
            VKQueue(
                    this,
                    getProperty { vk.getDeviceQueue(device, physicalDevice.deviceQueueFamilyIndices.presentQueueFamilyIndex, 0, it) },
                    physicalDevice.deviceQueueFamilyIndices.presentQueueFamilyIndex
            ).also { add(it) }
        }
    }

    fun createSwapchain(surface: VkSurface, extent: VkExtent2D, oldSwapchain: VKSwapchain? = null): VKSwapchain {
        val imageCount = physicalDevice.swapchainSupportDetails.chooseImageCount()
        val presentMode = physicalDevice.swapchainSupportDetails.chooseSwapPresentMode()
        val preTransform = physicalDevice.swapchainSupportDetails.chooseTransform()
        val surfaceFormat = physicalDevice.swapchainSupportDetails.chooseSwapSurfaceFormat()

        val imageSharingMode: VkSharingMode
        val queueFamilyIndexes: List<Int>
        if (physicalDevice.deviceQueueFamilyIndices.graphicsQueueFamilyIndex != physicalDevice.deviceQueueFamilyIndices.presentQueueFamilyIndex) {
            imageSharingMode = VkSharingMode.VK_SHARING_MODE_CONCURRENT
            queueFamilyIndexes = listOf(
                    physicalDevice.deviceQueueFamilyIndices.graphicsQueueFamilyIndex,
                    physicalDevice.deviceQueueFamilyIndices.presentQueueFamilyIndex)
        } else {
            imageSharingMode = VkSharingMode.VK_SHARING_MODE_EXCLUSIVE
            queueFamilyIndexes = listOf(physicalDevice.deviceQueueFamilyIndices.graphicsQueueFamilyIndex)
        }

        val createInfo = VkSwapchainCreateInfoKHR(
                VkStructureType.VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO,
                emptyList(),
                surface,
                imageCount,
                surfaceFormat.format,
                surfaceFormat.colorSpace,
                extent,
                1,
                listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT, VkImageUsageFlagBits.VK_IMAGE_USAGE_TRANSFER_DST_BIT),
                imageSharingMode,
                queueFamilyIndexes,
                preTransform,
                VkCompositeAlphaFlagBitsKHR.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR,
                presentMode,
                true,
                oldSwapchain?.swapchain
        )
        return VKSwapchain(this, getProperty { vk.createSwapchainKHR(device, createInfo, it).value })
                .also { add(it) }
    }

    fun createRenderPass(): VKRenderPass {
        val surfaceFormat = physicalDevice.swapchainSupportDetails.chooseSwapSurfaceFormat()
        val depthFormat = physicalDevice.depthFormat
        val colorAttachment = VkAttachmentDescription(
                emptyList(),
                surfaceFormat.format,
                listOf(VkSampleCountFlagBits.VK_SAMPLE_COUNT_1_BIT),
                VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_CLEAR,
                VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_STORE,
                VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_DONT_CARE,
                VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_DONT_CARE,
                initialLayout = VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                finalLayout = VkImageLayout.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR
        )

        val depthAttachment = VkAttachmentDescription(
                emptyList(),
                depthFormat,
                listOf(VkSampleCountFlagBits.VK_SAMPLE_COUNT_1_BIT),
                VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_CLEAR,
                VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_STORE,
                VkAttachmentLoadOp.VK_ATTACHMENT_LOAD_OP_DONT_CARE,
                VkAttachmentStoreOp.VK_ATTACHMENT_STORE_OP_DONT_CARE,
                initialLayout = VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED,
                finalLayout = VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL
        )

        val colorAttachmentRef = VkAttachmentReference(0, VkImageLayout.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL)
        val depthAttachmentRef = VkAttachmentReference(1, VkImageLayout.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL)
        val subpass = VkSubpassDescription(
                emptyList(),
                VkPipelineBindPoint.VK_PIPELINE_BIND_POINT_GRAPHICS,
                listOf(),
                listOf(colorAttachmentRef),
                listOf(),
                depthAttachmentRef,
                listOf())

        val dependency = VkSubpassDependency(
                VK_SUBPASS_EXTERNAL,
                0,
                listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                listOf(),
                listOf(VkAccessFlagBits.VK_ACCESS_COLOR_ATTACHMENT_READ_BIT,
                        VkAccessFlagBits.VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT),
                listOf())

        val renderCreateInfo = VkRenderPassCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO,
                0,
                listOf(colorAttachment, depthAttachment),
                listOf(subpass),
                listOf(dependency))
        return VKRenderPass(this, getProperty { vk.createRenderPass(device, renderCreateInfo, it).value })
                .also { add(it) }
    }

    fun createImage(
        size: VkExtent2D,
        format: VkFormat,
        tiling: VkImageTiling,
        usage: List<VkImageUsageFlagBits>
    ): VKImage {
        val createInfo = VkImageCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO,
                emptyList(),
                VkImageType.VK_IMAGE_TYPE_2D,
                format,
                VkExtent3D(size.width, size.height, 1),
                1,
                1,
                VkSampleCountFlagBits.VK_SAMPLE_COUNT_1_BIT,
                tiling,
                usage,
                VkSharingMode.VK_SHARING_MODE_EXCLUSIVE,
                listOf(),
                VkImageLayout.VK_IMAGE_LAYOUT_PREINITIALIZED)
        return VKImage(this, getProperty { vk.createImage(device, createInfo, it) })
    }

    fun createDepthImage(extent: VkExtent2D): VKImage {
        return createImage(
                extent,
                physicalDevice.depthFormat,
                VkImageTiling.VK_IMAGE_TILING_OPTIMAL,
                listOf(VkImageUsageFlagBits.VK_IMAGE_USAGE_DEPTH_STENCIL_ATTACHMENT_BIT)
        )
    }

    fun allocateDeviceMemory(
        allocateSize: VkDeviceSize,
        memoryTypeIndex: Int
    ): VKDeviceMemory {
        val allocateInfo = VkMemoryAllocateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO,
                allocateSize,
                memoryTypeIndex
        )
        return VKDeviceMemory(this, getProperty { vk.allocateMemory(device, allocateInfo, it).value })
                .also { add(it) }
    }

    fun createSemaphore(): VKSemaphore {
        val createInfo = VkSemaphoreCreateInfo(VkStructureType.VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO)
        return VKSemaphore(this, getProperty { vk.createSemaphore(device, createInfo, it).value })
    }

    fun createFence(): VKFence {
        val createInfo = VkFenceCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_FENCE_CREATE_INFO,
                listOf(VkFenceCreateFlagBits.VK_FENCE_CREATE_SIGNALED_BIT)
        )
        return VKFence(this, getProperty { vk.createFence(device, createInfo, it).value })
    }

    fun waitForFences(fences: List<VKFence>, waitAll: Boolean = true, timeout: Long = Long.MAX_VALUE): VkResult {
        return vk.waitForFences(device, fences.map { it.fence }, waitAll, timeout)
    }

    fun resetFences(fences: List<VKFence>): VkResult {
        return vk.resetFences(device, fences.map { it.fence })
    }

    fun deviceWaitIdle(): VkResult {
        return vk.deviceWaitIdle(device)
    }

    fun createBuffer(size: Long, usage: List<VkBufferUsageFlagBits>): VKBuffer {
        val createInfo = VkBufferCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO,
                emptyList(),
                size,
                usage,
                VkSharingMode.VK_SHARING_MODE_EXCLUSIVE,
                null
        )
        return VKBuffer(this, getProperty { vk.createBuffer(device, createInfo, it).value })
                .also { add(it) }
    }

    fun createSampler(
        magFilter: VkFilter,
        minFilter: VkFilter,
        mipmapMode: VkSamplerMipmapMode,
        addressModeU: VkSamplerAddressMode,
        addressModeV: VkSamplerAddressMode
    ): VKSampler {
        val createInfo = VkSamplerCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_SAMPLER_CREATE_INFO,
                0,
                magFilter,
                minFilter,
                mipmapMode,
                addressModeU,
                addressModeV,
                VkSamplerAddressMode.VK_SAMPLER_ADDRESS_MODE_REPEAT,
                0.0f,
                false,
                0.0f,
                false,
                VkCompareOp.VK_COMPARE_OP_ALWAYS,
                0.0f,
                0.0f,
                VkBorderColor.VK_BORDER_COLOR_FLOAT_OPAQUE_BLACK,
                false
        )
        return VKSampler(this, getProperty { vk.createSampler(device, createInfo, it).value })
                .also { add(it) }
    }

    fun createShaderModule(code: ByteArray): VKShaderModule {
        val createInfo = VkShaderModuleCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO,
                0,
                code
        )
        return VKShaderModule(this, getProperty { vk.createShaderModule(device, createInfo, it) })
                .also { add(it) }
    }
}
