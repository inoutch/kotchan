package io.github.inoutch.kotchan.utility.audio

import io.github.inoutch.kotchan.utility.Disposable

abstract class SoundBase : Disposable {

    abstract var volume: Float

    abstract fun play()

    abstract fun stop()
}
