package io.github.inoutch.kotchan.utility.audio

import android.media.MediaPlayer
import io.github.inoutch.kotchan.android.KotchanActivity
import io.github.inoutch.kotchan.android.audio.KotchanSoundManager

actual class BGM actual constructor(filepath: String) : SoundBase() {

    override var volume: Float = 1.0f
        set(value) {
            mediaPlayer.setVolume(value, value)
            field = value
        }

    actual val isPlaying: Boolean
        get() = mediaPlayer.isPlaying

    private val mediaPlayer = MediaPlayer()

    private val descriptor = KotchanActivity.assetFileDescriptor(filepath)

    private var actualPause = false

    init {
        if (descriptor != null) {
            mediaPlayer.setDataSource(descriptor)
        } else {
            mediaPlayer.setDataSource(filepath)
        }
        mediaPlayer.prepare()
        KotchanSoundManager.instance.add(this)
    }

    actual override fun play() {
        actualPause = false
        mediaPlayer.start()
    }

    actual override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.prepare()
        mediaPlayer.seekTo(0)
    }

    actual override fun destroy() {
        mediaPlayer.release()
        KotchanSoundManager.instance.remove(this)
    }

    actual fun reset() {
        mediaPlayer.reset()
    }

    actual fun pause() {
        actualPause = true
        mediaPlayer.pause()
    }

    actual fun loop(count: Int) {
        actualPause = false
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    fun pauseBySoundManager() {
        if (!actualPause) {
            mediaPlayer.pause()
        }
    }

    fun startBySoundManager() {
        if (!actualPause) {
            mediaPlayer.start()
        }
    }
}
