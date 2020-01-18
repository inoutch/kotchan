package io.github.inoutch.kotchan.extension

import io.github.inoutch.kotlin.vulkan.utility.MutableProperty

class PropertyError(val reason: Int): Error("Failed to get property [Reason code: ${reason}]")

inline fun <reified T>getProperty(scope: (property: MutableProperty<T>) -> Any): T {
    val property = MutableProperty<T>()
    val reason = scope(property)
    return property.value ?: throw if (reason is Int) PropertyError(reason) else IllegalStateException()
}

inline fun <reified T>getProperties(scope: (property: MutableList<T>) -> Any): List<T> {
    val mutableList = mutableListOf<T>()
    val reason = scope(mutableList)
    if (mutableList.isEmpty()) {
        throw if (reason is Int) PropertyError(reason) else IllegalStateException()
    }
    return mutableList
}
