package io.github.inoutch.kotchan.utility.audio

expect class BGM(filepath: String) : SoundBase {

    val isPlaying: Boolean

    override fun play()

    override fun stop()

    override fun release()

    fun reset()

    fun pause()

    fun loop(count: Int)
}