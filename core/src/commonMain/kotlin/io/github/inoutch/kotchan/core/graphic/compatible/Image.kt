package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.math.Vector2I
import kotlinx.coroutines.Deferred

expect class Image private constructor(byteArray: ByteArray, size: Vector2I){
    companion object {
        fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image>
    }

    val size: Vector2I

    val byteArray: ByteArray
}
