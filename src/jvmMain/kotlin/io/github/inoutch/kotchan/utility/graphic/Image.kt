package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.extension.toByteArray
import io.github.inoutch.kotchan.extension.toNative
import io.github.inoutch.kotchan.utility.memScoped
import io.github.inoutch.kotchan.utility.type.Point
import org.lwjgl.stb.STBImage

actual class Image private actual constructor(actual val byteArray: ByteArray, actual val size: Point) {
    actual companion object {
        actual fun load(byteArray: ByteArray) = memScoped {
            val w = allocInt()
            val h = allocInt()
            val channels = allocInt()

            val pixels = STBImage.stbi_load_from_memory(byteArray.toNative(this), w, h, channels, 0)
                    ?: throw Error("Failed to read image information: ${STBImage.stbi_failure_reason()}")

            Image(pixels.toByteArray(), Point(w.get(0), h.get(0)))
        }
    }
}
