package io.github.inoutch.kotchan.utility.graphic.vulkan

expect class VkQueue

expect fun vkGetDeviceQueue(device: VkDevice, queueFamilyIndex: Int, queueIndex: Int): VkQueue

expect fun vkQueueSubmit(queue: VkQueue, submitInfos: List<VkSubmitInfo>, fence: VkFence?)

expect fun vkQueuePresentKHR(queue: VkQueue, presentInfo: VkPresentInfoKHR)

expect fun vkQueueWaitIdle(queue: VkQueue)
