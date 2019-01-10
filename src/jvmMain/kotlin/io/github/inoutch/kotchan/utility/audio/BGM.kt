package io.github.inoutch.kotchan.utility.audio

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration

actual class BGM actual constructor(filepath: String) : SoundBase() {

    override var volume: Float
        get() = mediaPlayer.volume.toFloat()
        set(value) {
            mediaPlayer.volume = value.toDouble()
        }

    actual val isPlaying: Boolean
        get() = mediaPlayer.status == MediaPlayer.Status.PLAYING

    private val mediaPlayer: MediaPlayer

    init {
        val media = Media("file://$filepath")
        mediaPlayer = MediaPlayer(media)
    }

    actual override fun play() {
        mediaPlayer.play()
    }

    actual override fun stop() {
        mediaPlayer.stop()
    }

    actual override fun destroy() {
        mediaPlayer.dispose()
    }

    actual fun pause() {
        mediaPlayer.pause()
    }

    actual fun loop(count: Int) {
        mediaPlayer.cycleCount = count
        mediaPlayer.play()
    }

    actual fun reset() {
        mediaPlayer.seek(Duration.ONE)
    }
}
