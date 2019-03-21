package io.github.inoutch.kotchan.core.graphic.shader

import io.github.inoutch.kotchan.utility.Disposable

abstract class DescriptorSet(val binding: Int, val descriptorName: String) : Disposable
