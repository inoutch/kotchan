package kotchan.view.ui.template

import kotchan.view.drawable.Drawable
import utility.type.Vector4

data class Fragment(val type: TemplateAppendType, val drawables: MutableList<Pair<Drawable, Vector4>> = mutableListOf())