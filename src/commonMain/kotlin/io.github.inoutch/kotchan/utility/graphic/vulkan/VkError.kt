package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkError(val err: Int) : Error("Vulkan error = $err")

data class VkNullError(val name: String) : Error("$name is null")

data class VkInvalidStateError(val name: String) : Error("$name is invalid state")
