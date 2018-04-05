package extension

import kotlinx.cinterop.*
import platform.Foundation.*


fun Int.toNSNumber(): NSNumber {
    return NSNumber.numberWithInt(this)
}

fun Float.toNSNumber(): NSNumber {
    return NSNumber.numberWithFloat(this)
}

fun NSArray.toList(): List<ObjCObject> {
    val mutable = mutableListOf<ObjCObject>()
    for (i in 0 until this.count) {
        mutable.add(this[i])
    }
    return mutable
}

fun List<ObjCObject>.toNSArray(): NSArray {
    val mutable = NSMutableArray.arrayWithCapacity(this.size.toLong())
    this.forEach {
        mutable.addObject(it)
    }
    return mutable
}

fun NSDictionary.toMap(): Map<String, ObjCObject> {
    val mutableMap = mutableMapOf<String, ObjCObject>()
    val keys = this.allKeys
    for (i in 1..keys.count) {
        val key = keys[i - 1].reinterpret<NSString>()
        this[key]?.let { mutableMap[key.toString()] = it }
    }
    return mutableMap
}

fun Map<String, ObjCObject>.toNSDictionary(): NSDictionary {
    val mutable = NSMutableDictionary.dictionaryWithCapacity(this.size.toLong())
    this.forEach { (key, value) ->
        mutable[key.toNSString()] = value
    }
    return mutable
}

fun String.toNSString(): NSString {
    return interpretObjCPointer(CreateNSStringFromKString(this))
}