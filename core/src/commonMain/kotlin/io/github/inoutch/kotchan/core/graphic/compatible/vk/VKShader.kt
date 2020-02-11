package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.graphic.compatible.shader.Shader
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderSource

class VKShader(logicalDevice: VKLogicalDevice, shaderSource: ShaderSource) : Shader() {
    val vertShaderModule: VKShaderModule

    val fragShaderModule: VKShaderModule

    init {
        try {
            vertShaderModule = logicalDevice.createShaderModule(shaderSource.spirvVertSource)
            add(vertShaderModule)

            fragShaderModule = logicalDevice.createShaderModule(shaderSource.spirvFragSource)
            add(fragShaderModule)
        } catch (e: Error) {
            dispose()
            throw e
        }
    }
}
