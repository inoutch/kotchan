package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.Disposable

expect class VkInstance() : Disposable

expect fun vkCreateInstance(createInfo: VkInstanceCreateInfo): VkInstance
