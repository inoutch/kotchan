import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.polygon.SpriteAtlas
import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4
import kotlin.math.sin

class HighlightToggleButton(material: Material,
                            textureAtlas: TextureAtlas,
                            camera: Camera,
                            name: String,
                            highlightName: String,
                            override val onToggleOn: () -> Unit,
                            override val onToggleOff: () -> Unit) : SpriteAtlas(material, textureAtlas), ToggleButton {

    override val touchListener = ButtonTouchListener({ }, { }, camera) { click() }
            .apply { decision = RectTouchDecision { rect() } }

    override var state = true
        set(value) {
            highlightSpriteAtlas.visible = value
            field = value
        }

    private val highlightSpriteAtlas = SpriteAtlas(material, textureAtlas)

    private var highlightCircle = 0.0f

    init {
        setAtlas(name)
        highlightSpriteAtlas.setAtlas(highlightName)
        highlightSpriteAtlas.position = Vector3(0.0f, 0.0f, -0.1f)
        addChild(highlightSpriteAtlas)
        state = false
    }

    override fun update(delta: Float) {
        if (state) {
            highlightCircle += delta * 3.0f
            highlightSpriteAtlas.color = Vector4(1.0f, 1.0f, 1.0f, (sin(highlightCircle) + 1.0f) / 1.7f + 0.3f)
        }
    }
}
