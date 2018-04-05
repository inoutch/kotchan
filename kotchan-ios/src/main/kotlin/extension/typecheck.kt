package extension

import kotlinx.cinterop.*
import platform.Foundation.*

fun ObjCObject.isNSArray(): Boolean {
    return this.reinterpret<NSArray>().isKindOfClass(NSArray)
}

fun ObjCObject.isNSDictionary(): Boolean {
    return this.reinterpret<NSDictionary>().isKindOfClass(NSDictionary)
}

fun ObjCObject.isNSString(): Boolean {
    return this.reinterpret<NSString>().isKindOfClass(NSString)
}

fun ObjCObject.isNSNumber(): Boolean {
    return this.reinterpret<NSNumber>().isKindOfClass(NSNumber)
}