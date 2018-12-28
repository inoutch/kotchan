package io.github.inoutch.kotchan.utility.audio

import android.media.SoundPool;
import android.media.AudioAttributes
import io.github.inoutch.kotchan.android.KotchanActivity

actual class SE actual constructor(filepath: String) : SoundBase() {
    companion object {
        private const val STREAM_MAX = 32

        private val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

        private val soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(STREAM_MAX)
                .build()
    }

    override var volume: Float = 1.0f

    private val descriptor = KotchanActivity.assetFileDescriptor(filepath)

    private val sound = if (descriptor != null) soundPool.load(descriptor, 1) else soundPool.load(filepath, 1)

    actual override fun play() {
        soundPool.play(sound, volume, volume, 1, 0, 1.0f)
    }

    actual override fun stop() {
        soundPool.stop(sound)
    }

    actual override fun release() {
        soundPool.release()
    }
}