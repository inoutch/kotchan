package io.github.inoutch.kotchan.android.audio

import io.github.inoutch.kotchan.utility.audio.BGM

class KotchanSoundManager {
    private data class BGMWrapper(val bgm: BGM, var skip: Boolean)

    companion object {
        val instance = KotchanSoundManager()
    }

    private val bgms: MutableList<BGMWrapper> = mutableListOf()

    fun add(bgm: BGM) {
        bgms.add(BGMWrapper(bgm, true))
    }

    fun playAll() {
        bgms.forEach {
            if (!it.skip) {
                it.bgm.play()
                it.skip = true
            }
        }
    }

    fun pauseAll() {
        bgms.forEach {
            if (it.bgm.isPlaying) {
                it.skip = false
                it.bgm.pause()
            }
        }
    }

    fun remove(bgm: BGM) {
        bgms.removeIf { it.bgm == bgm }
    }
}