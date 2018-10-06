package extension

import kotlinx.cinterop.*
import platform.Foundation.*

internal fun Int.toNSNumber(): NSNumber {
    return NSNumber.numberWithInt(this)
}

internal fun Float.toNSNumber(): NSNumber {
    return NSNumber.numberWithFloat(this)
}

internal fun String.toNSString(): NSString {
    return interpretObjCPointer(CreateNSStringFromKString(this))
}

internal fun <T> NSSet?.toList(): kotlin.collections.List<T> {
    if (this == null) return listOf()
    return objectEnumerator().toList()
}

internal fun <T> NSEnumerator.toList(): List<T> {
    val items = mutableListOf<T>()
    var obj = nextObject()
    while (obj != null) {
        items += obj.uncheckedCast<T>()
        obj = nextObject()
    }
    return items
}