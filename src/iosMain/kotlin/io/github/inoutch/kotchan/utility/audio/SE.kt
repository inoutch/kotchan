package io.github.inoutch.kotchan.utility.audio

import platform.AVFoundation.AVAudioPlayer
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile

actual class SE actual constructor(filepath: String) : SoundBase() {

    override var volume: Float
        set(value) {
            audio.volume = value
        }
        get() = audio.volume

    private val audio: AVAudioPlayer

    init {
        val data = NSData.dataWithContentsOfFile(filepath) ?: throw Error("$filepath is not found")
        audio = AVAudioPlayer(data, null)
    }

    actual override fun play() {
        audio.play()
    }

    actual override fun stop() {
        audio.stop()
    }

    actual override fun destroy() {}
}
