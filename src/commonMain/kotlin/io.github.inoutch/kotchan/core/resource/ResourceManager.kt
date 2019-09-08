package io.github.inoutch.kotchan.core.resource

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.core.tool.TexturePacker
import io.github.inoutch.kotchan.utility.font.BMFont
import kotlin.native.concurrent.ThreadLocal

class ResourceManager {
    @ThreadLocal
    companion object {
        val resourceManager = ResourceManager()
    }

    private val materials = mutableMapOf<String, Material>()

    private val shaderPrograms = mutableMapOf<String, ShaderProgram>()

    private val textureBundles = mutableMapOf<String, TexturePacker.Bundle>()

    private val textures = mutableMapOf<String, Texture>()

    private val bmFonts = mutableMapOf<String, BMFont>()

    fun material(name: String) = materials[name] ?: throw Error("material of $name is not found")

    fun shaderProgram(name: String) = shaderPrograms[name] ?: throw Error("shader program of $name is not found")

    fun textureBundle(name: String) = textureBundles[name] ?: throw Error("texture bundle of $name is not found")

    fun texture(name: String) = textures[name] ?: throw Error("texture of $name is not found")

    fun bmFont(name: String) = bmFonts[name] ?: throw Error("bmFont of $name is not found")

    fun delegate(name: String, material: Material) {
        if (materials[name] != null) {
            throw Error("material name is already used [name = $name]")
        }
        if (materials.values.contains(material)) {
            throw Error("material is already delegated [name = $name]")
        }
        materials[name] = material
    }

    fun delegate(name: String, shaderProgram: ShaderProgram) {
        if (shaderPrograms[name] != null) {
            throw Error("shader program name is already used [name = $name]")
        }
        if (shaderPrograms.values.contains(shaderProgram)) {
            throw Error("shader program is already delegated [name = $name]")
        }
        shaderPrograms[name] = shaderProgram
    }

    fun delegate(name: String, textureBundle: TexturePacker.Bundle) {
        if (textureBundles[name] != null) {
            throw Error("texture bundle name is already used [name = $name]")
        }
        if (textureBundles.values.contains(textureBundle)) {
            throw Error("texture bundle is already delegated [name = $name]")
        }
        if (textures.values.contains(textureBundle.texture)) {
            throw Error("texture of bundle is already delegated [name = $name]")
        }
        textureBundles[name] = textureBundle
    }

    fun delegate(name: String, texture: Texture) {
        if (textures[name] != null) {
            throw Error("texture name is already used [name = $name]")
        }
        if (textures.values.contains(texture)) {
            throw Error("texture is already delegated [name = $name]")
        }
        if (textureBundles.values.find { it.texture == texture } != null) {
            throw Error("texture is already delegated [name = $name]")
        }
        textures[name] = texture
    }

    fun delegate(name: String, bmFont: BMFont) {
        if (bmFonts[name] != null) {
            throw Error("bmFont name is already used [name = $name]")
        }
        bmFonts[name] = bmFont
    }

    fun disposeAll() {
        materials.values.forEach { it.dispose() }
        shaderPrograms.values.forEach { it.dispose() }
        bmFonts.values.forEach { it.dispose() }
        materials.clear()
        shaderPrograms.clear()
        bmFonts.clear()
    }
}
