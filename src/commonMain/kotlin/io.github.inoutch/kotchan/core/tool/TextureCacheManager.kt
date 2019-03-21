package io.github.inoutch.kotchan.core.tool

import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.utility.Disposable

class TextureCacheManager : Disposable {

    private val cachesByPath = mutableMapOf<String, Texture>()

    fun load(filepath: String): Texture? {
        return cachesByPath[filepath]
                ?: Texture.load(filepath)
                ?: return null
    }

    // cache clear manually
    fun clearAll() {
        cachesByPath.forEach { it.value.dispose() }
        cachesByPath.clear()
    }

    override fun dispose() {
        clearAll()
    }
}
