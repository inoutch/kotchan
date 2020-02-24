package io.github.inoutch.kotchan.core.graphic.compatible.shader

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.Attribute

abstract class Shader(val attributes: List<Attribute>) : Disposer()
