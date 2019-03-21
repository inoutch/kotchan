package io.github.inoutch.kotchan.utility.audio

import javafx.scene.media.AudioClip

actual class SE actual constructor(filepath: String) : SoundBase() {

    override var volume: Float
        set(value) {
            clip.volume = value.toDouble()
        }
        get() = clip.volume.toFloat()

    private val clip = AudioClip("file://$filepath")

    actual override fun play() {
        clip.play()
    }

    actual override fun stop() {
        clip.stop()
    }

    actual override fun dispose() {}
}
