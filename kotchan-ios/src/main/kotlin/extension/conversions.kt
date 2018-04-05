package extension

import kotlinx.cinterop.*
import platform.Foundation.*


fun NSArray.toList(): List<ObjCObject> {
    val mutable = mutableListOf<ObjCObject>()
    for (i in 0 until this.count) {
        mutable.add(this[i])
    }
    return mutable
}

fun NSDictionary.toMap(): Map<String, ObjCObject> {
    val mutableMap = mutableMapOf<String, ObjCObject>()
    var key: NSString?
    do {
        key = this.keyEnumerator().nextObject()?.reinterpret() ?: break
        this[key]?.let { mutableMap[key.toString()] = it }
    } while (key != null)
    return mutableMap
}

fun String.toNSString(): NSString {
    return interpretObjCPointer(CreateNSStringFromKString(this))
}