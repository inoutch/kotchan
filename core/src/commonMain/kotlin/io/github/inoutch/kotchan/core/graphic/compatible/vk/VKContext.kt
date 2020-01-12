package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotlin.vulkan.api.VkInstance
import io.github.inoutch.kotlin.vulkan.api.VkInstanceCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkSurface
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.MutableProperty

class VKContext(
        instanceCreateInfo: VkInstanceCreateInfo,
        createSurface: (surface: MutableProperty<VkSurface>, instance: VkInstance) -> VkSurface
) : Context {
    private val disposer = Disposer()

    private val instance: VkInstance

    private val surface: VkSurface

    init {
        try {
            instance = getProperty { vk.createInstance(instanceCreateInfo, it) }
            disposer.add { vk.destroyInstance(instance) }

            surface = getProperty { createSurface(it, instance) }
            // TODO: Reserve to destroy surface
        } catch (e: Error) {
            dispose()
            throw e
        }
    }

    override fun dispose() {
        disposer.dispose()
    }
}
