package io.github.inoutch.kotchan.extension

import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.vulkan.api.VkExtent2D

fun Vector2I.toVKExtent2D(): VkExtent2D = VkExtent2D(x, y)
