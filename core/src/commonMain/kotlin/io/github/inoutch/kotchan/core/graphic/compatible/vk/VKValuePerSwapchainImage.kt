package io.github.inoutch.kotchan.core.graphic.compatible.vk

class VKValuePerSwapchainImage<T>(
        private val indexManager: VKSwapchainImageIndexManager,
        private val values: List<T>
) {
    val value: T
        get() = values[indexManager.index]
}
