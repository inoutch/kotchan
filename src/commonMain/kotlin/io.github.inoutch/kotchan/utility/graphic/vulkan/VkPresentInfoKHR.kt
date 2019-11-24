package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkPresentInfoKHR(
    val waitSemaphores: List<VkSemaphore>,
    val swapchains: List<VkSwapchainKHR>,
    val imageIndices: List<Int>,
    val results: List<VkResult>?
)
