package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

actual class VkQueue : Disposable {
    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun vkGetDeviceQueue(device: VkDevice, queueFamilyIndex: Int, queueIndex: Int): VkQueue {
    TODO()
}

actual fun vkQueueSubmit(queue: VkQueue, submitInfos: List<VkSubmitInfo>, fence: VkFence?) {
}

actual fun vkQueuePresentKHR(queue: VkQueue, presentInfo: VkPresentInfoKHR) {
}

actual fun vkQueueWaitIdle(queue: VkQueue) {
}
