package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.math.Vector2I
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class Image(val byteArray: ByteArray, val size: Vector2I) {
    companion object {
        fun loadFromFileAsync(filepath: String) = GlobalScope.async {
            val bytes = file.readBytesAsync(filepath).await() ?: return@async null

            // TODO: Change the loading method by looking at the image extension
            loadPNGByteArrayAsync(bytes).await()
        }
    }
}

expect fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image>
