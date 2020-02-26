package io.github.inoutch.kotchan.core.graphic.compatible.shader

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.graphic
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.Attribute
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.AttributeType
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.DescriptorSet
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform3F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTexture
import io.github.inoutch.kotchan.extension.toByteArray
import io.github.inoutch.kotchan.math.Matrix4F
import io.github.inoutch.kotchan.math.Vector3F

val standard3DVertCode = intArrayOf().toByteArray()

val standard3DFragCode = intArrayOf().toByteArray()

const val standard3DVertText = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

attribute vec4 point;
attribute vec4 color;
attribute vec2 texcoord;
attribute vec3 normal;

uniform mat4 uViewProjectionMatrix;
uniform mat4 uInvViewProjectionMatrix;
uniform vec3 uLightPosition;

varying vec4 vColor;
varying vec2 vTexcoord;
varying vec3 vNormal;
varying vec3 vLightPosition;
varying vec3 vFragPosition;

void main(void) {
    vColor = color;
    vTexcoord = texcoord;
    vNormal = normal;
    vLightPosition = (uInvViewProjectionMatrix * vec4(uLightPosition, 1.0)).xyz;
    gl_Position = uViewProjectionMatrix * point;
    vFragPosition = gl_Position.xyz;
}
"""

const val standard3DFragText = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

varying vec4 vColor;
varying vec2 vTexcoord;
varying vec3 vNormal;
varying vec3 vFragPosition;
varying vec3 vLightPosition;

uniform sampler2D uTexture0;
uniform vec3 uLightColor;

void main(void) {
    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * uLightColor;

    vec3 lightDir = normalize(vLightPosition - vFragPosition);
    float diff = max(dot(vNormal, lightDir), 0.0);
    vec3 diffuse = diff * uLightColor;

    vec3 lightEffect = ambient + diffuse;
    gl_FragColor = vColor * texture2D(uTexture0, vTexcoord) * vec4(lightEffect, 1.0);
}
"""

class Standard3DShaderProgram private constructor(
        shaderSource: ShaderSource = createShaderSource(),
        extraDescriptorSets: List<DescriptorSet> = emptyList()
) : ShaderProgram(
        shaderSource,
        listOf(*createDescriptorSets().toTypedArray(), *extraDescriptorSets.toTypedArray()),
        createAttributes()
) {
    companion object {
        fun create(
                shaderSource: ShaderSource = createShaderSource(),
                extraDescriptorSets: List<DescriptorSet> = emptyList()
        ): Standard3DShaderProgram {
            return Standard3DShaderProgram(shaderSource, extraDescriptorSets)
        }

        private fun createShaderSource(): ShaderSource {
            return ShaderSource(
                    standard3DVertText,
                    standard3DFragText,
                    standard3DVertCode,
                    standard3DFragCode
            )
        }

        private fun createDescriptorSets(): List<DescriptorSet> {
            return listOf(
                    graphic.createUniformMatrix4F(0, "uViewProjectionMatrix"),
                    graphic.createUniformMatrix4F(1, "uInvViewProjectionMatrix"),
                    graphic.createUniformTexture(2, "uTexture0"),
                    graphic.createUniform3F(3, "uLightPosition"),
                    graphic.createUniform3F(4, "uLightColor")
            )
        }

        private fun createAttributes(): List<Attribute> {
            return listOf(
                    Attribute(0, "point", 3, AttributeType.FLOAT),
                    Attribute(1, "color", 4, AttributeType.FLOAT),
                    Attribute(2, "texcoord", 2, AttributeType.FLOAT),
                    Attribute(3, "normal", 3, AttributeType.FLOAT)
            )
        }
    }

    private val viewProjectionMatrixUniform = descriptorSets[0] as UniformMatrix4F

    private val invViewProjectionMatrixUniform = descriptorSets[1] as UniformMatrix4F

    private val primaryTextureUniform = descriptorSets[2] as UniformTexture

    private val lightPositionUniform = descriptorSets[3] as Uniform3F

    private val lightColorUniform = descriptorSets[4] as Uniform3F

    fun prepare(
            mvpMatrix: Matrix4F,
            texture: Texture,
            lightPosition: Vector3F,
            lightColor: Vector3F
    ) {
        viewProjectionMatrixUniform.set(mvpMatrix)
        invViewProjectionMatrixUniform.set(mvpMatrix.inverseWithTranspose())
        primaryTextureUniform.set(texture)
        lightPositionUniform.set(lightPosition)
        lightColorUniform.set(lightColor)
    }
}
