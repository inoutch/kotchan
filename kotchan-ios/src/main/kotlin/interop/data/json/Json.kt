package interop.data.json

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*

import extension.*

actual class Json {
    actual companion object {
        actual fun parse(json: String): JsonObject? {
            val data = json.toNSString().dataUsingEncoding(NSUTF8StringEncoding) ?: return null
            val jsonObject = memScoped {
                val errorVar = alloc<ObjCObjectVar<NSError?>>()
                val result = NSJSONSerialization.JSONObjectWithData(data, 0, errorVar.ptr)
                val error = errorVar.value
                if (error != null) {
                    return null
                }
                result!!
            }
            return parse(jsonObject)
        }

        actual fun write(jsonObject: JsonObject): String? {
            val obj = when {
                jsonObject.isMap() -> output(jsonObject)
                jsonObject.isList() -> output(jsonObject)
                else -> null
            } ?: return null
            val data = NSJSONSerialization.dataWithJSONObject(obj, NSJSONWritingPrettyPrinted, null) ?: return null
            return data.bytes!!.readBytes(data.length.toInt()).stringFromUtf8()
        }

        private fun output(obj: JsonObject): ObjCObject? {
            when {
                obj.isFloat() -> return obj.toFloat()?.toNSNumber()
                obj.isText() -> return obj.toText()?.toNSString()
                obj.isList() -> return obj.toList().mapNotNull { output(it) }.toNSArray()
                obj.isMap() -> {
                    val mutableMap = mutableMapOf<String, ObjCObject>()
                    obj.toMap().forEach {
                        mutableMap[it.key] = output(it.value) ?: return@forEach
                    }
                    return mutableMap.toNSDictionary()
                }
            }
            throw Error("output undefined type")
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