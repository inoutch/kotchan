package io.github.inoutch.kotchan.core.view.ui.button

import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.view.camera.Camera
import io.github.inoutch.kotchan.core.view.drawable.DrawableGroup
import io.github.inoutch.kotchan.core.view.drawable.Sprite
import io.github.inoutch.kotchan.core.view.texture.TextureAtlas
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4
import kotlin.math.sin

class HighlightToggleButton(
        textureAtlas: TextureAtlas,
        uiCamera: Camera,
        normal: String,
        highlight: String,
        override val onToggleOn: () -> Unit,
        override val onToggleOff: () -> Unit) : DrawableGroup(), ToggleButton {

    override var state = true
        set(value) {
            highlightSprite.visible = value
            field = value
        }

    private val normalSprite = Sprite(textureAtlas).also {
        it.setAtlas(normal)
        it.anchorPoint = Vector2.Zero
    }

    private val highlightSprite = Sprite(textureAtlas).also {
        it.setAtlas(highlight)
        it.anchorPoint = Vector2.Zero
    }

    private var time: Float = 0.0f

    override val nodes = listOf(Node(highlightSprite, Vector3.Zero), Node(normalSprite, Vector3.Zero))

    override var size = normalSprite.size

    val touchListener = ButtonTouchListener({ }, { }, uiCamera) { click() }
            .apply { decision = RectTouchDecision { normalSprite.rect() } }

    override fun update(delta: Float) {
        time += delta
        highlightSprite.color = Vector4(1.0f, 1.0f, 1.0f, 0.6f + sin(time * 3.0f) * 0.4f)
    }
}