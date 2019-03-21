//package io.github.inoutch.kotchan.core.graphic.ui.button.toggle
//
//import io.github.inoutch.kotchan.core.controller.touch.listener.ButtonTouchListener
//import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
//import io.github.inoutch.kotchan.core.graphic.camera.Camera
//import io.github.inoutch.kotchan.core.graphic.view.Sprite
//import io.github.inoutch.kotchan.core.graphic.texture.TextureAtlas
//import io.github.inoutch.kotchan.utility.type.Vector2
//import io.github.inoutch.kotchan.utility.type.Vector3
//import io.github.inoutch.kotchan.utility.type.Vector4
//import kotlin.math.sin
//
//class HighlightToggleButton(
//        textureAtlas: TextureAtlas,
//        uiCamera: Camera,
//        normal: String,
//        highlight: String,
//        override val onToggleOn: () -> Unit,
//        override val onToggleOff: () -> Unit) : Sprite(textureAtlas), ToggleButton {
//
//    override var state = true
//        set(value) {
//            highlightSprite.visible = value
//            field = value
//        }
//
//    private val highlightSprite = Sprite(textureAtlas).also {
//        it.setAtlas(highlight)
//        it.anchorPoint = Vector2.Zero
//    }
//
//    private var time: Float = 0.0f
//
//    val touchListener = ButtonTouchListener({ }, { }, uiCamera) { click() }
//            .apply { decision = RectTouchDecision { rect() } }
//
//    init {
//        setAtlas(normal)
//        anchorPoint = Vector2.Zero
//        addChild(highlightSprite)
//        position = Vector3(0.0f, 0.0f, 1.0f)
//    }
//
//    override fun update(delta: Float) {
//        time += delta
//        highlightSprite.color = Vector4(1.0f, 1.0f, 1.0f, 0.6f + sin(time * 3.0f) * 0.4f)
//    }
//}
