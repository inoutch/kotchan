package io.github.inoutch.kotchan.core.graphic.material

import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Standard2DShaderProgram

class Standard2DMaterial private constructor(
        private var standardShaderProgram: Standard2DShaderProgram,
        private var camera: Camera,
        private var texture: Texture,
        graphicsPipelineConfig: GraphicsPipelineConfig = GraphicsPipelineConfig()
) : MaterialBase<Standard2DShaderProgram>(
        standardShaderProgram,
        graphicsPipelineConfig
) {
    companion object {
        fun create(
                standardShaderProgram: Standard2DShaderProgram,
                camera: Camera,
                texture: Texture,
                graphicsPipelineConfig: GraphicsPipelineConfig = GraphicsPipelineConfig()
        ): Standard2DMaterial {
            return Standard2DMaterial(standardShaderProgram, camera, texture, graphicsPipelineConfig)
        }
    }

    fun updateCamera(camera: Camera) {
        this.camera = camera
    }

    fun updateTexture(texture: Texture) {
        this.texture = texture
    }

    override fun bind() {
        super.bind()
        shaderProgram.prepare(camera.combine, texture)
    }
}
