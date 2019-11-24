package io.github.inoutch.kotchan.ios.extension

import platform.Foundation.*

internal fun Int.toNSNumber(): NSNumber {
    return NSNumber.numberWithInt(this)
}

internal fun Float.toNSNumber(): NSNumber {
    return NSNumber.numberWithFloat(this)
}

@Suppress("CAST_NEVER_SUCCEEDS")
internal fun String.toNSString(): NSString {
    return this as NSString
}

internal fun <T> NSSet?.toList(): kotlin.collections.List<T> {
    if (this == null) return listOf()
    return objectEnumerator().toList()
}

@Suppress("UNCHECKED_CAST")
internal fun <T> NSEnumerator.toList(): List<T> {
    val items = mutableListOf<T>()
    var obj = nextObject()
    while (obj != null) {
        items += obj as T
        obj = nextObject()
    }
    return items
}
