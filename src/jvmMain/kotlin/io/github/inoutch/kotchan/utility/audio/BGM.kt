package io.github.inoutch.kotchan.utility.audio

import java.io.File
import javax.sound.sampled.*

actual class BGM actual constructor(filepath: String) : SoundBase() {
    actual companion object;

    override var volume: Float
        get() = (clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl).value
        set(value) {
            (clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl).value = value
        }

    actual val isPlaying: Boolean
        get() = clip.isRunning

    private val clip: Clip

    init {
        clip = createClip(filepath)
    }

    actual override fun play() {
        clip.start()
    }

    actual override fun stop() {
        clip.stop()
        clip.flush()
        clip.framePosition = 0
    }

    actual override fun dispose() {
        clip.close()
    }

    actual fun pause() {
        clip.stop()
    }

    actual fun loop(count: Int) {
        clip.loop(count)
    }

    actual fun reset() {
        clip.framePosition = 0
    }

    private fun createClip(filepath: String): Clip {
        return AudioSystem.getAudioInputStream(File(filepath)).use { ais ->
            val af = ais.format
            val dataLine = DataLine.Info(Clip::class.java, af)
            val c = AudioSystem.getLine(dataLine) as Clip

            c.open(ais)

            c
        }
    }
}
