package io.github.inoutch.kotchan.extension

import io.github.inoutch.kotlin.vulkan.utility.MutableProperty

inline fun <reified T>getProperty(scope: (property: MutableProperty<T>) -> Unit): T {
    val property = MutableProperty<T>()
    scope(property)
    return property.value ?: throw NullPointerException()
}
