package io.github.inoutch.kotchan.core.graphic.ui.button

import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.polygon.SpriteAtlas
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.core.graphic.ui.UI

class Button(material: Material,
             textureAtlas: TextureAtlas,
             private val normalName: String,
             private val pressedName: String,
             camera: Camera,
             onClick: () -> Unit) : SpriteAtlas(material, textureAtlas), UI {
    init {
        setAtlas(normalName)
    }

    override val touchListener = ButtonTouchListener({ setAtlas(normalName) }, { setAtlas(pressedName) }, camera)
    { onClick() }.apply { decision = RectTouchDecision { rect() } }
}
