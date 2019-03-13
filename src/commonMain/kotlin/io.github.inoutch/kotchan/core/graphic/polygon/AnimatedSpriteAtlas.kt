package io.github.inoutch.kotchan.core.graphic.polygon

import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.utility.Updatable

open class AnimatedSpriteAtlas(
        material: Material,
        textureAtlas: TextureAtlas,
        val config: Config) : SpriteAtlas(material, textureAtlas), Updatable {

    data class AnimationSet(val ids: List<Int>, val interval: Float, val count: Int = -1)

    data class Config(val animations: List<AnimationSet>,
                      val defaultAnimationId: Int = 0)

    var animationStateId = config.defaultAnimationId
        private set

    private var currentAnimationIndex = 0

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
        val m = (delta * 1000).toInt()
        val i = (m % (config.animations[animationStateId].interval * 1000).toInt()) / 1000

        if (currentAnimationIndex != i) {
            config.animations[i]
            if (currentAnimationIndex > i) {
                done?.invoke()
            }
            currentAnimationIndex = i
        }
    }
}
