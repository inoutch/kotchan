package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.utility.Updatable

open class AnimatedSpriteAtlas(
        material: Material?,
        textureAtlas: TextureAtlas,
        val config: Config) : SpriteAtlas(material, textureAtlas), Updatable {

    data class AnimationSet(val ids: List<Int>, val intervalSec: Float, val count: Int = -1)

    data class Config(val animations: List<AnimationSet>,
                      val defaultAnimationId: Int = 0)

    var animationStateId = config.defaultAnimationId
        private set

    private var currentAnimationIndex = 0

    private var elapsedMilliseconds = 0

    private var done: (() -> Unit)? = null

    init {
        run(config.defaultAnimationId)
    }

    fun run(animationId: Int, done: (() -> Unit)? = null) {
        currentAnimationIndex = 0
        animationStateId
        this.done = done
    }

    // can run sequential as once
    fun runSequence(animationId: Int) {
        if (animationStateId != animationId && animationId >= 0 && animationId < config.animations.size) {
            animationStateId = animationId
            run(animationId)
        }
    }

    override fun update(delta: Float) {
        elapsedMilliseconds += (delta * 1000.0f).toInt()
        val animation = config.animations.getOrNull(animationStateId) ?: return
        val i = (elapsedMilliseconds / (animation.intervalSec * 1000).toInt()) % animation.ids.size

        if (currentAnimationIndex != i) {
            setAtlas(animation.ids[i])
            if (currentAnimationIndex == animation.ids.size - 1) {
                done?.invoke()
            }
            currentAnimationIndex = i
        }
    }
}
