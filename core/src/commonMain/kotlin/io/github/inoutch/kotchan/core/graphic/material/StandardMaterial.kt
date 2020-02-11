package io.github.inoutch.kotchan.core.graphic.material

import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.StandardShaderProgram

class StandardMaterial private constructor(
        private var standardShaderProgram: StandardShaderProgram,
        private var camera: Camera,
        private var texture: Texture,
        graphicsPipelineConfig: GraphicsPipelineConfig = GraphicsPipelineConfig()
) : MaterialBase<StandardShaderProgram>(
        standardShaderProgram,
        graphicsPipelineConfig
) {
    companion object {
        fun create(
                standardShaderProgram: StandardShaderProgram,
                camera: Camera,
                texture: Texture
        ): StandardMaterial {
            return StandardMaterial(standardShaderProgram, camera, texture)
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
