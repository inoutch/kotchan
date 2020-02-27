package io.github.inoutch.kotchan.core.graphic.material

import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Standard3DShaderProgram
import io.github.inoutch.kotchan.math.Vector3F

class Standard3DMaterial private constructor(
        shaderProgram: Standard3DShaderProgram,
        private var camera: Camera,
        private var texture: Texture,
        private var lightPosition: Vector3F,
        private var lightColor: Vector3F,
        private var ambientStrength: Float,
        graphicsPipelineConfig: GraphicsPipelineConfig
) : MaterialBase<Standard3DShaderProgram>(shaderProgram, graphicsPipelineConfig) {

    companion object {
        fun create(
                standardShaderProgram: Standard3DShaderProgram,
                camera: Camera,
                texture: Texture,
                lightPosition: Vector3F,
                lightColor: Vector3F,
                ambientStrength: Float = 0.2f,
                graphicsPipelineConfig: GraphicsPipelineConfig = GraphicsPipelineConfig()
        ): Standard3DMaterial {
            return Standard3DMaterial(
                    standardShaderProgram,
                    camera,
                    texture,
                    lightPosition,
                    lightColor,
                    ambientStrength,
                    graphicsPipelineConfig
            )
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
        shaderProgram.prepare(camera.combine, texture, lightPosition, lightColor, ambientStrength)
    }
}
