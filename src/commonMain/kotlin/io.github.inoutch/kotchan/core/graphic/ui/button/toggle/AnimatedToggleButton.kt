package io.github.inoutch.kotchan.core.graphic.ui.button.toggle

import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.polygon.AnimatedSpriteAtlas
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas

class AnimatedToggleButton(
    material: Material,
    textureAtlas: TextureAtlas,
    camera: Camera,
    onAnimationSet: AnimationSet,
    offAnimationSet: AnimationSet,
    override val onToggleOn: () -> Unit,
    override val onToggleOff: () -> Unit
) : AnimatedSpriteAtlas(
        material,
        textureAtlas,
        AnimatedSpriteAtlas.Config(listOf(onAnimationSet, offAnimationSet))), ToggleButton {

    override val touchListener = ButtonTouchListener({ }, { }, camera) { click() }
            .apply { decision = RectTouchDecision { rect() } }

    override var state = true
        set(value) {
            run(if (value) 0 else 1)
            field = value
        }
}
