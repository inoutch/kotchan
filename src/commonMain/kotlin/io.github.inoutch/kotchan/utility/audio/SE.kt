package io.github.inoutch.kotchan.utility.audio

expect class SE(filepath: String) : SoundBase {

    override fun play()

    override fun stop()

    override fun dispose()
}
