package io.github.inoutch.kotchan.utility.audio

import platform.AVFoundation.AVAudioPlayer
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile

actual class BGM actual constructor(filepath: String) : SoundBase() {
    actual companion object;

    override var volume: Float
        set(value) {
            audio.volume = value
        }
        get() = audio.volume

    actual val isPlaying: Boolean
        get() = audio.isPlaying()

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
        audio.currentTime = 0.0
    }

    actual override fun dispose() {}

    actual fun reset() {
        audio.currentTime = 0.0
    }

    actual fun pause() {
        audio.pause()
    }

    actual fun loop(count: Int) {
        audio.numberOfLoops = count.toLong()
        audio.play()
    }
}
