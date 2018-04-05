package interop.data.json

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*

import extension.*

actual class Json {
    actual companion object {
        actual fun parse(json: String): JsonObject {
            val data = json.toNSString().dataUsingEncoding(NSUTF8StringEncoding)
                    ?: throw Error("only utf8")
            val value = NSJSONSerialization.JSONObjectWithData(data, NSJSONReadingAllowFragments, null)
                    ?: throw Error("not json format")
            return parse(value)
        }

        actual fun write(jsonObject: JsonObject): String {
            return "[]"
        }

        private fun parse(value: ObjCObject): JsonObject {
            when {
                value.isNSNumber() -> {
                    val nsnumber = value.reinterpret<NSNumber>()
                    return JsonObject(nsnumber.floatValue)
                }
                value.isNSString() -> {
                    val nsstring = value.reinterpret<NSNumber>()
                    return JsonObject(nsstring.toString())
                }
                value.isNSDictionary() -> {
                    val nsdictionary = value.reinterpret<NSDictionary>()
                    return JsonObject.createMap().also { j ->
                        nsdictionary.toMap().forEach { j.addAsMap(it.key, parse(it.value)) }
                    }
                }
                value.isNSArray() -> {
                    val nsarray = value.reinterpret<NSArray>()
                    return JsonObject.createList().also { j ->
                        nsarray.toList().forEach { j.addAsList(parse(it)) }
                    }
                }
                else -> throw Error("unknown json extension")
            }
        }
    }
}