package io.github.inoutch.kotchan.core.graphic.ui.button

import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.polygon.SpriteAtlas
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.core.graphic.ui.Touchable
import io.github.inoutch.kotchan.utility.type.Vector4

class ColorAtlasButton(material: Material,
                       textureAtlas: TextureAtlas,
                       camera: Camera,
                       onClick: () -> Unit) : SpriteAtlas(material, textureAtlas), Touchable {

    var normalColor = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        set(value) {
            field = value
            color = value
        }

    var pressedColor = Vector4(0.7f, 0.7f, 0.7f, 1.0f)

    override val touchListener = ButtonTouchListener(
            { color = normalColor },
            { color = pressedColor },
            camera) { onClick() }
            .apply { decision = RectTouchDecision { rect() } }

    init {
        color = normalColor
    }
}
