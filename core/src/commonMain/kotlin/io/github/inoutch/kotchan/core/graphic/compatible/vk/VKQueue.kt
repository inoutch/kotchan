package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkCommandPoolCreateFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkCommandPoolCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkPresentInfoKHR
import io.github.inoutch.kotlin.vulkan.api.VkQueue
import io.github.inoutch.kotlin.vulkan.api.VkResult
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.VkSubmitInfo
import io.github.inoutch.kotlin.vulkan.api.vk

class VKQueue(
        val logicalDevice: VKLogicalDevice,
        val queue: VkQueue,
        val queueFamilyIndex: Int
) : Disposer() {
    fun createCommandPool(): VKCommandPool {
        val createInfo = VkCommandPoolCreateInfo(
                VkStructureType.VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO,
                listOf(VkCommandPoolCreateFlagBits.VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT),
                queueFamilyIndex
        )
        return add(VKCommandPool(
                logicalDevice,
                this,
                getProperty { vk.createCommandPool(logicalDevice.device, createInfo, it) }
        ))
    }

    fun queueWaitIdle(): VkResult {
        return vk.queueWaitIdle(queue)
    }

    fun queueSubmit(submitInfos: List<VkSubmitInfo>, fence: VKFence): VkResult {
        return vk.queueSubmit(queue, submitInfos, fence.fence)
    }

    fun queuePresentKHR(presentInfo: VkPresentInfoKHR): VkResult {
        return vk.queuePresentKHR(queue, presentInfo)
    }
}
