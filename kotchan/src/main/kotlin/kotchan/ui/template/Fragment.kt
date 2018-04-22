package kotchan.ui.template

import kotchan.scene.drawable.Drawable
import utility.type.Vector4

data class Fragment(val type: TemplateAppendType, val drawables: MutableList<Pair<Drawable, Vector4>> = mutableListOf())