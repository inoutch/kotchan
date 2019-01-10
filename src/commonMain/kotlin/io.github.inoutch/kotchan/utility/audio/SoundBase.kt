package io.github.inoutch.kotchan.utility.audio

import io.github.inoutch.kotchan.core.destruction.StrictDestruction

abstract class SoundBase : StrictDestruction() {

    abstract var volume: Float

    abstract fun play()

    abstract fun stop()
}