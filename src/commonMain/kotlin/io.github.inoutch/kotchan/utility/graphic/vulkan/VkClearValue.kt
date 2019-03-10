package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.type.Vector4

class VkClearValue private constructor(val color: Vector4?, val depthStencil: VkClearDepthStencilValue?) {
    constructor(color: Vector4) : this(color, null)

    constructor(depthStencil: VkClearDepthStencilValue) : this(null, depthStencil)
}
