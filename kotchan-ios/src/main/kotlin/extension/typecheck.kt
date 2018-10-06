package extension

import kotlinx.cinterop.*
import platform.Foundation.*

fun Any.isNSArray(): Boolean {
    return this is NSArray
}

fun Any.isNSDictionary(): Boolean {
    return this is NSDictionary
}

fun Any.isNSString(): Boolean {
    return this is NSString
}

fun Any.isNSNumber(): Boolean {
    return this is NSNumber
}