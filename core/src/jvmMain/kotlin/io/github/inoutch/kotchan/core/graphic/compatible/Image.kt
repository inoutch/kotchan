package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.vulkan.extension.toByteArray
import io.github.inoutch.kotlin.vulkan.extension.toNative
import io.github.inoutch.kotlin.vulkan.utility.memScoped
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.lwjgl.stb.STBImage

actual fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image> {
    return loadByteArrayAsync(byteArray)
}

fun loadByteArrayAsync(byteArray: ByteArray): Deferred<Image> = GlobalScope.async(Dispatchers.Unconfined) {
    memScoped {
        val width = allocInt()
        val height = allocInt()
        val channels = allocInt()
        // 1           grey
        // 2           grey, alpha
        // 3           red, green, blue
        // 4           red, green, blue, alpha

        val pixels = STBImage.stbi_load_from_memory(
                byteArray.toNative(this),
                width,
                height,
                channels,
                0
        )
        checkNotNull(pixels) { "Failed to read image information: ${STBImage.stbi_failure_reason()}" }
        Image(pixels.toByteArray(), Vector2I(width[0], height[0]))
    }
}
