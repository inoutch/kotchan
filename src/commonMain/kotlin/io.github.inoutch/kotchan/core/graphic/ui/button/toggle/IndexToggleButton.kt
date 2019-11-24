package io.github.inoutch.kotchan.core.graphic.ui.button.toggle

import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.polygon.SpriteAtlas
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.core.graphic.ui.Touchable

class IndexToggleButton(
    material: Material,
    textureAtlas: TextureAtlas,
    camera: Camera,
    private val names: List<String>,
    private val onToggle: (index: Int) -> Unit
) : SpriteAtlas(material, textureAtlas), Touchable {
    var index = 0
        private set

    override val touchListener = ButtonTouchListener({ }, { }, camera) { click() }
            .apply { decision = RectTouchDecision { rect() } }

    init {
        setAtlas(names[index])
    }

    fun click() {
        index = (index + 1) % names.size
        setAtlas(names[index])
        onToggle(index)
    }
}
