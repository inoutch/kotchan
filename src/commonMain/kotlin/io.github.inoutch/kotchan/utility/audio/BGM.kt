package io.github.inoutch.kotchan.utility.audio

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError

expect class BGM(filepath: String) : SoundBase {
    companion object

    val isPlaying: Boolean

    override fun play()

    override fun stop()

    override fun dispose()

    fun reset()

    fun pause()

    fun loop(count: Int)
}

fun BGM.Companion.loadFromResource(filepath: String): BGM {
    return BGM(instance.file.getResourcePathWithError(filepath))
}
