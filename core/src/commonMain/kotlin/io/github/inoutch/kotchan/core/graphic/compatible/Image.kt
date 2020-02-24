package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.io.file.readBytesFromResourceWithErrorAsync
import io.github.inoutch.kotchan.math.Vector2I
import kotlinx.coroutines.Deferred

class Image(val byteArray: ByteArray, val size: Vector2I) {
    companion object {
        @ExperimentalStdlibApi
        suspend fun loadFromResourceWithError(filepath: String): Image {
            val bytes = file.readBytesFromResourceWithErrorAsync(filepath).await()
            return loadPNGByteArrayAsync(bytes).await()
        }

        suspend fun load(filepath: String): Image? {
            val bytes = file.readBytesAsync(filepath).await() ?: return null

            // TODO: Change the loading method by looking at the image extension
            return loadPNGByteArrayAsync(bytes).await()
        }
    }
}

expect fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image>
