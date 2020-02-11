package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.math.Vector2I
import kotlinx.coroutines.Deferred

class Image(val byteArray: ByteArray, val size: Vector2I)

expect fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image>
