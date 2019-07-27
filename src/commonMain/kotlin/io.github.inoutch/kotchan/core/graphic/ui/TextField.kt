package io.github.inoutch.kotchan.core.graphic.ui

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.controller.keyboard.KeyboardListener
import io.github.inoutch.kotchan.core.controller.keyboard.keyboardController
import io.github.inoutch.kotchan.core.controller.touch.TouchType
import io.github.inoutch.kotchan.core.controller.touch.listener.ArgTouchListener
import io.github.inoutch.kotchan.core.controller.touch.listener.decision.RectTouchDecision
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.camera.Camera
import io.github.inoutch.kotchan.core.graphic.polygon.Polygon2D
import io.github.inoutch.kotchan.core.graphic.polygon.Sprite
import io.github.inoutch.kotchan.core.graphic.polygon.TextLabel
import io.github.inoutch.kotchan.core.resource.ResourceManager
import io.github.inoutch.kotchan.utility.font.BMFont
import io.github.inoutch.kotchan.utility.time.Timer
import io.github.inoutch.kotchan.utility.type.Mesh
import io.github.inoutch.kotchan.utility.type.Vector2
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4
import kotlin.math.PI
import kotlin.math.sin

class TextField(private val bmFont: BMFont,
                material: Material,
                size: Vector2,
                fontSize: Float,
                private val margin: Float,
                camera: Camera) : Polygon2D(Mesh(), null, size), Touchable {
    enum class EditTextState {
        General,
        Focused,
    }

    var state = EditTextState.General
        private set

    var text = ""
        set(value) {
            field = value
            val ret = calcDisplayText(displayFilter(text))
            textLabel.text = ret.second
            updateCursorPosition(ret.first)
            updatePlaceholder()
        }

    var placeholderText = ""
        set(value) {
            field = value
            val ret = calcDisplayText(value)
            placeholderTextLabel.text = ret.second
        }

    var filter = { text: String -> text }

    var displayFilter = { text: String -> text }

    var textColor: Vector4
        get() = textLabel.color
        set(value) {
            textLabel.color = value
        }

    var placeholderTextColor: Vector4
        get() = placeholderTextLabel.color
        set(value) {
            placeholderTextLabel.color = value
        }

    var backgroundColor: Vector4
        get() = background.color
        set(value) {
            background.color = value
        }

    var cursorColor: Vector4
        get() = cursor.color
        set(value) {
            cursor.color = value
        }

    private val textLabel = TextLabel(bmFont, "", fontSize)

    private val placeholderTextLabel = TextLabel(bmFont, "", fontSize)

    private val background = Sprite(material, size)

    private val cursor = Sprite(
            ResourceManager.resourceManager.material(KotchanCore.KOTCHAN_RESOURCE_MATERIAL_PLAIN),
            Vector2(fontSize * 0.07f, fontSize))

    private val fontRatio = fontSize / bmFont.common.lineHeight

    init {
        textLabel.anchorPoint = Vector2(0.0f, 0.5f)
        textLabel.position = Vector3(margin, size.y / 2.0f, 0.0f)

        placeholderTextLabel.anchorPoint = Vector2(0.0f, 0.5f)
        placeholderTextLabel.position = Vector3(margin, size.y / 2.0f, 0.0f)

        background.anchorPoint = Vector2.Zero
        background.position = Vector3(.0f, .0f, -0.1f)
        background.color = Vector4(0.0f, 0.0f, 0.0f, 0.3f)

        cursor.anchorPoint = Vector2(0.0f, 0.5f)
        cursor.position = Vector3(.0f, .0f, 0.1f)
        cursor.visible = false
        updateCursorPosition(0.0f)

        addChild(background)
        addChild(placeholderTextLabel)
        addChild(textLabel)
        addChild(cursor)

        updatePlaceholder()
    }

    override val touchListener = ArgTouchListener(camera) { index, _, type, check, chain ->
        if (index != 0 || type != TouchType.Began) {
            return@ArgTouchListener chain
        }
        if (check && chain && state == EditTextState.General) {
            onClickEditText()
        } else if (state == EditTextState.Focused) {
            onClickOverview()
        }
        return@ArgTouchListener true
    }.also { it.decision = RectTouchDecision { rect() } }

    override fun update(delta: Float) {
        cursor.visible = state == EditTextState.Focused && sin(Timer.milliseconds() * PI * 2.0f / 1000.0f) >= 0.0f
    }

    private fun onClickEditText() {
        state = EditTextState.Focused
        keyboardController.open(textLabel.text, object : KeyboardListener {
            override fun onInput(value: String) {
                val filteredText = filter(value)
                text = filteredText
                if (filteredText != value) {
                    keyboardController.text = filteredText
                }
            }

            override fun onReturn() {
                onClickOverview()
            }
        })
        updatePlaceholder()
    }

    private fun onClickOverview() {
        state = EditTextState.General
        keyboardController.close()
        updatePlaceholder()
    }

    private fun updateCursorPosition(textWidth: Float) {
        cursor.position = Vector3(margin + textWidth, size.y / 2.0f, 0.0f)
    }

    private fun updatePlaceholder() {
        placeholderTextLabel.visible = text.isBlank()
    }

    private fun calcDisplayText(text: String): Pair<Float, String> {
        var sum = 0.0f
        for (i in 1..text.length) {
            val n = text.length - i
            val char = bmFont.chars[text[n].toInt()] ?: continue
            val xAdvance = char.xAdvance * fontRatio
            if (sum + xAdvance > size.x - margin * 2.0f) {
                return Pair(sum, text.substring(n + 1, text.length))
            }
            sum += xAdvance
        }
        return Pair(sum, text)
    }
}
