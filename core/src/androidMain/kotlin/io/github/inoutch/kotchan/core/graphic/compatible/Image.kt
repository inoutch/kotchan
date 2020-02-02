package io.github.inoutch.kotchan.core.graphic.compatible

import android.graphics.BitmapFactory
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.gl.extension.toByteArray
import java.nio.ByteBuffer
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

actual class Image private actual constructor(
    actual val byteArray: ByteArray,
    actual val size: Vector2I
) {
    actual companion object {
        actual fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image> = GlobalScope.async(Dispatchers.Unconfined) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val buffer = ByteBuffer.allocate(bitmap.byteCount)
            bitmap.copyPixelsToBuffer(buffer)
            Image(buffer.toByteArray(), Vector2I(bitmap.width, bitmap.height))
        }
    }
}
