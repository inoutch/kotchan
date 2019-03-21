package io.github.inoutch.kotchan.utility.graphic.vulkan

@ExperimentalUnsignedTypes
fun VkStencilOpState.copyToNative(native: vulkan.VkStencilOpState) {
    native.failOp = failOp.value.toUInt()
    native.passOp = passOp.value.toUInt()
    native.depthFailOp = depthFailOp.value.toUInt()
    native.compareOp = compareOp.value.toUInt()
    native.compareMask = compareMask.toUInt()
    native.writeMask = writeMask.toUInt()
    native.reference = reference.toUInt()
}
