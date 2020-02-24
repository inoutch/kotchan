package io.github.inoutch.kotchan.core.graphic.material

import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.StandardLabelShaderProgram

class StandardLabelMaterial private constructor(
        standardLabelShaderProgram: StandardLabelShaderProgram,
        private var camera: Camera,
        private var textures: List<Texture>,
        graphicsPipelineConfig: GraphicsPipelineConfig
): MaterialBase<StandardLabelShaderProgram>(standardLabelShaderProgram, graphicsPipelineConfig) {
    companion object {
        fun create(
                standardLabelShaderProgram: StandardLabelShaderProgram,
                camera: Camera,
                textures: List<Texture>,
                graphicsPipelineConfig: GraphicsPipelineConfig = GraphicsPipelineConfig()
        ): StandardLabelMaterial {
            return StandardLabelMaterial(
                    standardLabelShaderProgram,
                    camera,
                    textures,
                    graphicsPipelineConfig
            )
        }
    }

    override fun bind() {
        super.bind()
        shaderProgram.prepare(camera.combine, textures)
    }
}