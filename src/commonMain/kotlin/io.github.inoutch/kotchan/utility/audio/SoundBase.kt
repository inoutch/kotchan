package io.github.inoutch.kotchan.utility.audio

abstract class SoundBase {

    abstract var volume: Float

    abstract fun play()

    abstract fun stop()

    abstract fun release()
}