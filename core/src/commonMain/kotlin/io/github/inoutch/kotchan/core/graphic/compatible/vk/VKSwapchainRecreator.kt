package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotlin.vulkan.api.VkExtent2D
import io.github.inoutch.kotlin.vulkan.api.VkSurface

class VKSwapchainRecreator(
    private val surface: VkSurface,
    private val logicalDevice: VKLogicalDevice,
    private val commandPool: VKCommandPool,
    initialExtent: VkExtent2D
) : Disposer() {
    var current: VKSwapchainRecreatorProperties
        private set

    init {
        current = recreateProperties(initialExtent)
    }

    fun recreate(extent: VkExtent2D) {
        current = recreateProperties(extent, current)
    }

    private fun recreateProperties(extent: VkExtent2D, oldProperties: VKSwapchainRecreatorProperties? = null): VKSwapchainRecreatorProperties {
        val newExtent = logicalDevice.physicalDevice.swapchainSupportDetails.chooseSwapExtent(extent)
        val swapchain = logicalDevice.createSwapchain(surface, newExtent, oldProperties?.swapchain)
        val localDisposer = Disposer()
        try {
            val renderPass = localDisposer.add(logicalDevice.createRenderPass())

            val swapchainImages = swapchain.getSwapchainImages()
            val swapchainImageViews = swapchainImages.map {
                localDisposer.add(it.createImageView(logicalDevice.physicalDevice.swapchainSupportDetails.chooseSwapSurfaceFormat().format))
            }

            val depthResources = swapchainImages.map {
                val depthImage = localDisposer.add(logicalDevice.createDepthImage(newExtent))
                val depthImageDeviceMemory = localDisposer.add(depthImage.allocateDepthImageDeviceMemory(commandPool))
                val depthImageView = localDisposer.add(depthImage.createDepthImageView())
                VKSwapchainRecreatorDepthResources(depthImage, depthImageView, depthImageDeviceMemory)
            }
            val commandBuffers = commandPool.allocateCommandBuffer(swapchainImages.size)
            commandBuffers.forEach { add(it) }

            val framebuffers = swapchainImageViews.mapIndexed { index, imageView ->
                add(renderPass.createFramebuffer(imageView, depthResources[index].depthImageView, newExtent))
            }

            if (oldProperties != null) {
                dispose(oldProperties)
            }
            // DO NOT NEED to dispose localDisposer
            logicalDevice.removeAll()

            return add(VKSwapchainRecreatorProperties(
                    newExtent,
                    swapchain,
                    swapchainImages,
                    swapchainImageViews,
                    renderPass,
                    depthResources,
                    framebuffers,
                    commandBuffers
            ))
        } catch (e: Error) {
            localDisposer.dispose()
            throw e
        }
    }
}
