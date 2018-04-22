package kotchan.ui.template

import kotchan.Engine
import kotchan.scene.drawable.Drawable
import utility.type.Rect
import utility.type.Vector2
import utility.type.Vector3
import utility.type.Vector4

class Template(val rect: Rect = Rect(Vector2(), Engine.getInstance().screenSize)) {
    companion object {
        fun createMargin(left: Float, right: Float, top: Float, bottom: Float) = Vector4(left, right, top, bottom)
    }

    private enum class HorizontalType {
        Left,
        Center,
        Right,
    }

    private enum class VerticalType {
        Top,
        Middle,
        Bottom,
    }

    private val drawables: MutableMap<TemplateType, Fragment> = mutableMapOf()

    private fun switchType(type: TemplateType) = when (type) {
        TemplateType.TopLeft -> Pair(HorizontalType.Left, VerticalType.Top)
        TemplateType.TopCenter -> Pair(HorizontalType.Center, VerticalType.Top)
        TemplateType.TopRight -> Pair(HorizontalType.Right, VerticalType.Top)
        TemplateType.MiddleLeft -> Pair(HorizontalType.Left, VerticalType.Middle)
        TemplateType.MiddleCenter -> Pair(HorizontalType.Center, VerticalType.Middle)
        TemplateType.MiddleRight -> Pair(HorizontalType.Right, VerticalType.Middle)
        TemplateType.BottomLeft -> Pair(HorizontalType.Left, VerticalType.Bottom)
        TemplateType.BottomCenter -> Pair(HorizontalType.Center, VerticalType.Bottom)
        TemplateType.BottomRight -> Pair(HorizontalType.Right, VerticalType.Bottom)
    }

    var visible = true
        set(value) {
            drawables.forEach { it.value.drawables.forEach { it.first.visible = value } }
            field = visible
        }

    fun add(type: TemplateType,
            appendType: TemplateAppendType,
            margin: Vector4,
            drawable: Drawable) {
        val pair = this.drawables
                .getOrPut(type, { Fragment(appendType) })
        pair.drawables.add(Pair(drawable, margin))
    }

    fun updatePositions() {
        drawables.forEach {
            val type = switchType(it.key)
            // calculation of overall drawables size
            val h = if (it.value.type == TemplateAppendType.Row) {
                it.value.drawables.sumByDouble { (it.first.size.y + it.second.z + it.second.w).toDouble() }.toFloat()
            } else 0.0f
            val w = if (it.value.type == TemplateAppendType.Column) {
                it.value.drawables.sumByDouble { (it.first.size.x + it.second.x + it.second.y).toDouble() }.toFloat()
            } else 0.0f

            val x = rect.origin.x + (rect.size.x - w) * h(type.first)
            val y = rect.origin.y + (rect.size.y - h) * v(type.second)
            val anchorPoint = Vector2(h(type.first), v(type.second))
            var position = Vector2(x, y)
            // calculation position
            it.value.drawables.forEach { pair ->
                val drawable = pair.first
                val margin = pair.second
                position += when (it.value.type) {
                    TemplateAppendType.Row -> Vector2(0.0f, margin.w + drawable.size.y * anchorPoint.y)
                    TemplateAppendType.Column -> Vector2(margin.x + drawable.size.x * anchorPoint.x, 0.0f)
                }
                val offset = when (it.value.type) {
                    TemplateAppendType.Row -> Vector2(margin.x - (margin.x + margin.y) * anchorPoint.x, 0.0f)
                    TemplateAppendType.Column -> Vector2(0.0f, margin.w - drawable.size.y * anchorPoint.y)
                }
                pair.first.position = Vector3(position + offset, 0.0f)
                position += when (it.value.type) {
                    TemplateAppendType.Row -> Vector2(0.0f, margin.y + drawable.size.y * (1.0f - anchorPoint.y))
                    TemplateAppendType.Column -> Vector2(margin.z + drawable.size.x * (1.0f - anchorPoint.x), 0.0f)
                }
                pair.first.anchorPoint = anchorPoint
            }
        }
    }

    private fun h(type: HorizontalType) = when (type) {
        Template.HorizontalType.Left -> 0.0f
        Template.HorizontalType.Center -> 0.5f
        Template.HorizontalType.Right -> 1.0f
    }

    private fun v(type: VerticalType) = when (type) {
        Template.VerticalType.Top -> 1.0f
        Template.VerticalType.Middle -> 0.5f
        Template.VerticalType.Bottom -> 0.0f
    }

    fun clear() {
        drawables.clear()
    }
}